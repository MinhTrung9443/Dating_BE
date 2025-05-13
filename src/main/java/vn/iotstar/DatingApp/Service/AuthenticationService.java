package vn.iotstar.DatingApp.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.Image;
import vn.iotstar.DatingApp.Entity.SearchCriteria;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.AccountUserDetails;
import vn.iotstar.DatingApp.Model.Request.ResetPasswordRequest;
import vn.iotstar.DatingApp.Model.Response.AuthResponse;
import vn.iotstar.DatingApp.Repository.AccountRepository;
import vn.iotstar.DatingApp.Repository.OtpCodeRepository;
import vn.iotstar.DatingApp.Repository.UsersRepository;

@Service
public class AuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private MailService emailService; // Service gửi email
	@Autowired
	private OtpService otpService; // Service quản lý OTP
	@Autowired
	private OtpCodeRepository otpRepository;
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;

	// REGISTER
	public void requestRegistrationOtp(String email) {
		logger.info("Processing OTP request for registration: {}", email);

		// 1. Kiểm tra email tồn tại và đã xác thực chưa
		if (accountRepository.findAccountByEmail(email).isPresent()) { // Giả sử có trường isEnabled
			throw new RuntimeException("Email này đã được đăng ký và kích hoạt.");
		}

		// 2. Tạo và gửi OTP
		String otp = otpService.generateAndStoreOtp(email);

		// 3. Gửi Email (nên @Async trong EmailService)
		try {
			emailService.sendRegisterOtp(email, otp);
			logger.info("Registration OTP sent successfully to {}", email);
		} catch (Exception e) {
			logger.error("Failed to send registration OTP to {}: {}", email, e.getMessage());
			// Có thể throw exception cụ thể hơn hoặc để ControllerAdvice xử lý
			throw new RuntimeException("Không thể gửi mã OTP. Vui lòng thử lại.", e);
		}
	}

	public void verifyRegistrationOtp(String email, String otp) {

		logger.info("Verifying registration OTP for email: {}", email);
		boolean isOtpValid = otpService.verifyOtp(email, otp);

		if (!isOtpValid) {
			logger.warn("Invalid or expired OTP provided for email: {}", email);
			throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn.");
		}

		logger.info("OTP verified successfully for email: {}", email);
		otpRepository.deleteByIdentifier(email);
		logger.debug("Deleted old verification tokens for email: {}", email);

	}

	@Transactional // Quan trọng
	public AuthResponse setPasswordAndRegister(String email, String password) {
		logger.info("Setting password and completing registration.");
		// 4. Tạo Account mới
		Account account = Account.builder()
				.email(email).password(encoder.encode(password))
				.role("USER").status(true)
				.build();
		Account savedAccount = accountRepository.save(account);
		
		SearchCriteria userSearchDefault = new SearchCriteria(18,50,50.0);
		Image userImageDefault = new Image();
		userImageDefault.setImage("https://www.tenforums.com/geek/gars/images/2/types/thumb_15951118880user.png");
		
		// 4. Tạo User mới (Hoặc Account)
		// Đảm bảo bạn dùng đúng Entity Class và Builder/Constructor của bạn
		Users newUser = Users.builder() // Hoặc new Account(...)
				.account(account)
				.searchCriteria(userSearchDefault)
				.build();
		userSearchDefault.setUsers(newUser);
		userImageDefault.setUser(newUser);
		usersRepository.save(newUser); // Lưu user mới
		
		logger.info("New user registered successfully with email: {}", email);


		// 6. Tạo JWT tokens (Access & Refresh)
		var jwtAccessToken = jwtService.generateToken(new AccountUserDetails(savedAccount)); // Tạo access token
		logger.info("Generated JWT tokens for user {}", email);

		// 7. Tạo và trả về AuthenticationResponse
		return AuthResponse.builder()
				.token(jwtAccessToken)
				.expiresIn(jwtService.getExpirationTime())
				.build();
	}

	// LOGIN
	/**
     * Đăng nhập bằng email và mật khẩu.
     * @param email Email người dùng.
     * @param password Mật khẩu người dùng.
     * @return AuthResponse chứa token và user info.
     * @throws InvalidCredentialsException Nếu thông tin đăng nhập không đúng.
     */
    public AuthResponse login(String email, String password) {
        logger.info("Processing login request for email: {}", email);
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            // Nếu không có exception ném ra ở đây, nghĩa là xác thực thành công

            // 2. Lấy thông tin User/Account đã được xác thực
            // Principal trả về chính là đối tượng UserDetails được load bởi UserDetailsService
            Account authenticatedAccount = accountRepository.findAccountByEmail(email).get(); // Ép kiểu về Entity của bạn
            logger.info("User {} authenticated successfully", authenticatedAccount.getEmail());

            // 3. Tạo JWT Tokens (Access & Refresh)
            var jwtAccessToken = jwtService.generateToken(new AccountUserDetails(authenticatedAccount));

            // 6. Tạo và trả về AuthResponse
            return AuthResponse.builder()
                    .expiresIn(jwtService.getExpirationTime())
                    .token(jwtAccessToken)
                    .build();

        } catch (AuthenticationException e) {
            // Bắt exception từ authenticationManager.authenticate()
            logger.warn("Authentication failed for email {}: {}", email, e.getMessage());
            // Ném ra exception cụ thể hơn để ControllerAdvice xử lý
            throw new RuntimeException("Thông tin đăng nhập không chính xác.");
        } catch (Exception e) {
            // Bắt các lỗi không mong muốn khác
             logger.error("An unexpected error occurred during login for email {}: {}", email, e.getMessage(), e);
             throw new RuntimeException("Đã xảy ra lỗi trong quá trình đăng nhập.", e);
        }
    }

    // FORGOT PASSWORD
    public void requestPasswordResetOtp(String identifier) {
        logger.info("Processing password reset OTP request for identifier: {}", identifier);

        // 1. Tìm user bằng identifier (Giả sử identifier là email)
        //    Nếu bạn hỗ trợ cả SĐT, cần logic để phân biệt và tìm kiếm phù hợp
       accountRepository.findAccountByEmail(identifier)
                .orElseThrow(() -> {
                    logger.warn("Password reset OTP request for non-existent identifier: {}", identifier);
                    return new UsernameNotFoundException("Không tìm thấy tài khoản nào với thông tin cung cấp.");
                });


        // 2. Tạo và lưu OTP (Sử dụng OtpPurpose.PASSWORD_RESET)
        String otp = otpService.generateAndStoreOtp(identifier);

        // 3. Gửi OTP (qua email hoặc sms tùy vào identifier)
        try {
            emailService.sendOtp(identifier, otp);
            logger.info("Password reset OTP sent successfully to {}", identifier);
        } catch (Exception e) {
            logger.error("Failed to send password reset OTP to {}: {}", identifier, e.getMessage(), e);
            throw new RuntimeException("Không thể gửi mã OTP đặt lại mật khẩu. Vui lòng thử lại.", e);
        }
    }

    public void resetPassword(ResetPasswordRequest requestDto) {
        String identifier = requestDto.getIdentifier();
        String otp = requestDto.getOtp();
        logger.info("Processing password reset for identifier: {}", identifier);

        // 1. Kiểm tra mật khẩu khớp
        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không khớp.");
        }

        // 2. Xác thực OTP (Sử dụng OtpPurpose.PASSWORD_RESET)
        boolean isValidOtp = otpService.verifyOtp(identifier, otp);
        if (!isValidOtp) {
            logger.warn("Invalid or expired password reset OTP provided for identifier: {}", identifier);
            throw new RuntimeException("Mã OTP không hợp lệ hoặc đã hết hạn.");
        }
        // OtpService đã xóa OTP nếu hợp lệ

        // 3. Tìm User/Account (Lúc này gần như chắc chắn sẽ tìm thấy nếu OTP đúng)
        Account account = accountRepository.findAccountByEmail(identifier) // Hoặc findByIdentifier
                .orElseThrow(() -> {
                     // Trường hợp này rất hiếm nếu OTP hợp lệ, có thể là lỗi logic/race condition
                     logger.error("CRITICAL: Valid password reset OTP verified but account not found for identifier: {}", identifier);
                     return new UsernameNotFoundException("Không tìm thấy tài khoản tương ứng.");
                });

        // 4. Cập nhật mật khẩu mới (đã hash)
        account.setPassword(encoder.encode(requestDto.getNewPassword()));
        accountRepository.save(account); // Lưu thay đổi mật khẩu
        logger.info("Password reset successfully for user {}", identifier);

    }
}
