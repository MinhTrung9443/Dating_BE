package vn.iotstar.DatingApp.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResetPasswordRequest {
    private String identifier; // Email hoặc SĐT đã dùng để nhận OTP

    private String otp;

    private String newPassword;

    private String confirmPassword;
}
