package vn.iotstar.DatingApp.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.iotstar.DatingApp.Entity.OtpCode;
import vn.iotstar.DatingApp.Repository.OtpCodeRepository;

@Service
public class OtpServiceImpl implements OtpService {

	private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
	private static final long OTP_VALIDITY_MINUTES = 5;
	@Autowired
	private OtpCodeRepository otpCodeRepository;

	@Override
	@Transactional
	public String generateAndStoreOtp(String identifier) {

		otpCodeRepository.deleteByIdentifier(identifier);
		logger.debug("Deleted existing OTPs for identifier: {} and purpose: {}", identifier);

		String otp = generateRandomOtp();
		LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES);

		OtpCode newOtpCode = OtpCode.builder().identifier(identifier).otpCode(otp).expiresAt(expiresAt)
				.build();

		otpCodeRepository.save(newOtpCode);
		logger.info("Generated and stored new OTP for identifier: {} and purpose: {}", identifier);
		logger.debug("Generated OTP: {}", otp);

		return otp;
	}

	@Override
	@Transactional
	public boolean verifyOtp(String identifier, String otpFromRequest) {
		LocalDateTime currentTime = LocalDateTime.now();
		logger.debug("Verifying OTP for identifier: {}, purpose: {}", identifier);

		Optional<OtpCode> otpOptional = otpCodeRepository
				.findTopByIdentifierAndExpiresAtAfterOrderByCreatedAtDesc(identifier, currentTime);

		if (otpOptional.isEmpty()) {
			logger.warn("No valid OTP found for identifier: {} and purpose: {}", identifier);
			return false;
		}

		OtpCode storedOtp = otpOptional.get();

		if (storedOtp.getOtpCode().equals(otpFromRequest)) {
			otpCodeRepository.delete(storedOtp);
			logger.info("OTP verified successfully and deleted for identifier: {} and purpose: {}", identifier);
			return true;
		} else {
			logger.warn("Incorrect OTP provided for identifier: {} and purpose: {}", identifier);
			return false;
		}
	}

	private String generateRandomOtp() {
		return String.format("%06d", new java.util.Random().nextInt(999999));
	}
}
