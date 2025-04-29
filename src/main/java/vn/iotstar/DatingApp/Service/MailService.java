package vn.iotstar.DatingApp.Service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {

	void sendRegisterOtp(String email, String otp);

	void sendOtp(String email, String otp);

}
