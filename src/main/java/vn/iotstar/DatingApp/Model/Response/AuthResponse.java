package vn.iotstar.DatingApp.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthResponse {
	private String token;
	private long expiresIn;

	public String getToken() {
		return token;
	}
}