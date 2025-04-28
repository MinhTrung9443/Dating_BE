package vn.iotstar.DatingApp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import vn.iotstar.DatingApp.Model.Request.ResetPasswordRequest;
import vn.iotstar.DatingApp.Model.Response.ApiResponse;
import vn.iotstar.DatingApp.Model.Response.AuthResponse;
import vn.iotstar.DatingApp.Service.AuthenticationService;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;
	
	// REGISTER
	/***
	 	Endpoint: POST /api/auth/register/request-otp
		Mục đích: Bắt đầu quá trình đăng ký bằng cách yêu cầu gửi mã OTP đến số điện thoại.
		Request Body: { "phone_number": "+84..." }
		Success Response (200 OK): { "message": "OTP sent successfully..." } (Có thể kèm thời gian chờ gửi lại)
		Error Responses: 400 (Sai định dạng SĐT), 409 (SĐT đã đăng ký hoàn chỉnh), 500 (Lỗi gửi OTP).
	 */
	@PostMapping("/register/request-otp")
	public ResponseEntity<ApiResponse> requestOtp(@RequestParam("email") String email) {
		authenticationService.requestRegistrationOtp(email);
		return ResponseEntity.ok(ApiResponse.builder()
				.message("Đã gửi OTP").status(200).data(email)
											.build());
	}
	
	/***
	 	Endpoint: POST /api/v1/auth/register/verify-otp
		Mục đích: Xác thực mã OTP người dùng nhập. Không tạo tài khoản hoàn chỉnh ngay.
		Request Body: { "phone_number": "+84...", "otp": "123456" }
		Success Response (200 OK): { "message": "OTP verified...", "verification_token": "temp_token_for_password_set" } (Quan trọng: Trả về token tạm thời, không phải token đăng nhập)
		Error Responses: 400 (Thiếu trường, sai định dạng OTP), 401 (OTP sai hoặc hết hạn), 404 (Không tìm thấy OTP cho SĐT này).
	 */
	@PostMapping("/register/verify-otp")
	public ResponseEntity<ApiResponse> verifyOtp(@RequestParam("email") String email, @RequestParam("otpCode") String otpCode) {
		authenticationService.verifyRegistrationOtp(email, otpCode);
		return ResponseEntity.ok(ApiResponse.builder()
				.message("Đã xác minh OTP").status(200).data(email)
											.build());
	}
	
	/***
		Endpoint: POST /api/v1/auth/register/set-password
		Mục đích: Đặt mật khẩu lần đầu sau khi OTP đã được xác thực, hoàn tất tạo tài khoản và đăng nhập.
		Request Body: { "verification_token": "temp_token_from_verify", "password": "...", "confirm_password": "..." }
		Success Response (201 Created / 200 OK): { "message": "Registration complete.", "access_token": "jwt_access", "refresh_token": "jwt_refresh", "user": {...} } (Quan trọng: Trả về token đăng nhập)
		Error Responses: 400 (Mật khẩu không khớp, mật khẩu yếu), 401/404 (Verification token không hợp lệ/hết hạn), 409 (Lỗi tạo tài khoản, ví dụ SĐT bất ngờ đã tồn tại đầy đủ).
	 */
	@PostMapping("/register/set-password")
	public ResponseEntity<ApiResponse> setPassword(@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("confirmedPassword") String confirmedPassword) {
		// khác pass
		if(!password.equals(confirmedPassword)) {
			return ResponseEntity.ok(ApiResponse.builder()
					.message("Mat khau khong khop").status(400).data(null)
												.build());
		}
		
		AuthResponse res = authenticationService.setPasswordAndRegister(email, password);
		
		return ResponseEntity.ok(ApiResponse.builder()
				.message("Đã lưu tài khoản").status(200).data(res)
											.build());
	}
	
	
	// LOGIN
	/***
		Đăng nhập bằng Email/Mật khẩu
		Endpoint: POST /api/v1/auth/login/email
		Mục đích: Xác thực người dùng bằng email và mật khẩu.
		Request Body: { "email": "...", "password": "..." }
		Success Response (200 OK): { "message": "Login successful", "access_token": "...", "refresh_token": "...", "user": {...} }
		Error Responses: 400 (Thiếu trường), 401 (Sai thông tin đăng nhập), 404 (Không tìm thấy người dùng).
 	*/
	@PostMapping("/login/email")
	public ResponseEntity<ApiResponse> login(String email, String password) {
		
		AuthResponse res = authenticationService.login(email, password);
		
		return ResponseEntity.ok(ApiResponse.builder()
				.message("Đã xác minh OTP").status(200).data(res)
				.build());
	}
	
	/***
	 	Endpoint: POST /api/v1/auth/login/social
		Mục đích: Xác thực người dùng thông qua token từ nhà cung cấp mạng xã hội, đăng nhập hoặc tạo tài khoản nếu chưa có.
		Request Body: { "provider": "google", "token": "provider_token..." }
		Success Response (200 OK / 201 Created): { "message": "Login/Registration successful", "access_token": "...", "refresh_token": "...", "user": {...} }
		Error Responses: 400 (Provider không hợp lệ, thiếu token), 401 (Token provider không hợp lệ), 500 (Lỗi xác thực với provider).
	 */
	
	// FORGOT PASSWORD
	/***
	 	Endpoint: POST /api/v1/auth/password/request-reset-otp
		Mục đích: Yêu cầu gửi mã OTP để bắt đầu quá trình đặt lại mật khẩu (qua Email hoặc SĐT).
		Request Body: { "identifier": "user@example.com" } (Hoặc "+84...")
		Success Response (200 OK): { "message": "Password reset OTP sent..." }
		Error Responses: 400 (Sai định dạng identifier), 404 (Identifier không tồn tại trong hệ thống), 500 (Lỗi gửi OTP).
	 */
	@PostMapping("/password/request-reset-otp")
    public ResponseEntity<ApiResponse> requestPasswordReset(String email) {
        authenticationService.requestPasswordResetOtp(email);
        // Luôn trả về OK để tránh lộ thông tin email/SĐT có tồn tại hay không (tùy chính sách bảo mật)
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Nếu thông tin bạn cung cấp tồn tại trong hệ thống, một mã OTP đặt lại mật khẩu đã được gửi.")
                .status(200)
                .data(null)
                .build());
    }
	
	/***
	  	Endpoint: POST /api/v1/auth/password/reset
		Mục đích: Xác thực OTP đặt lại mật khẩu và cập nhật mật khẩu mới cho người dùng.
		Request Body: { "identifier": "...", "otp": "...", "new_password": "...", "confirm_password": "..." }
		Success Response (200 OK): { "message": "Password reset successfully. Please login." } (Không trả về token đăng nhập ở đây, yêu cầu người dùng đăng nhập lại)
		Error Responses: 400 (Mật khẩu không khớp, yếu, sai định dạng OTP), 401 (OTP sai hoặc hết hạn), 404 (Không tìm thấy OTP cho identifier này).
	 */
	@PostMapping("/password/reset")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordDto) {
        authenticationService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Đặt lại mật khẩu thành công. Vui lòng đăng nhập lại.")
                .status(200)
                .data(resetPasswordDto)
                .build());
    }
	
	// LOGOUT
	/***
		Endpoint: POST /api/v1/auth/logout
		Mục đích: Vô hiệu hóa token hiện tại (ít nhất là refresh token).
		Authentication: Yêu cầu Authorization: Bearer <access_token> trong Header.
		Request Body (Optional): { "refresh_token": "..." } (Nếu cần client gửi lên để vô hiệu hóa)
		Success Response (200 OK): { "message": "Logged out successfully" }
		Error Responses: 401 (Unauthorized).
	 */
}