package vn.iotstar.DatingApp.Service;

public interface OtpService {

	boolean verifyOtp(String identifier, String otpFromRequest);

	String generateAndStoreOtp(String identifier);
}
