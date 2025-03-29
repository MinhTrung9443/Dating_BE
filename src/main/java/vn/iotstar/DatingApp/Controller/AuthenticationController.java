package vn.iotstar.DatingApp.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Model.AccountUserDetails;
import vn.iotstar.DatingApp.Model.Auth.LoginRequest;
import vn.iotstar.DatingApp.Model.Auth.RegisterRequest;
import vn.iotstar.DatingApp.Model.Response.AuthResponse;
import vn.iotstar.DatingApp.Service.AuthenticationService;
import vn.iotstar.DatingApp.Service.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	private final JwtService jwtService;
	private final AuthenticationService authenticationService;
	
	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/signup")
	@Transactional
	public ResponseEntity<Account> register(@RequestBody RegisterRequest registerUser) {
		Account registeredUser = authenticationService.signup(registerUser);
		return ResponseEntity.ok(registeredUser);
	}
	
	@PostMapping(path="/login")
	@Transactional
	public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequest loginUser) {
		Account authenticationUser = authenticationService.authenticate(loginUser);
		
		//changing here
		String jwtToken = jwtService.generateToken(new AccountUserDetails(authenticationUser));
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setToken(jwtToken);
		authResponse.setExpiresIn(jwtService.getExpirationTime());
		
		return ResponseEntity.ok(authResponse);
	}
}