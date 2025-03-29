package vn.iotstar.DatingApp.Model.Auth;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	// for login
	private String email;
    private String password;
    
    // user infor
    private String name;
    private String phone;
    private String gender;
    private String sexualOrientation;
    private Date birthday;
    private String biography;
    private double height;
    private int weight;
    private String zodiacSign;
    private String personalityType;
    private String interests;
    private String address;
    private String job;
}
