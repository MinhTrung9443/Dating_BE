package vn.iotstar.DatingApp.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Model.AccountUserDetails;
import vn.iotstar.DatingApp.Model.Auth.LoginAccountModel;
import vn.iotstar.DatingApp.Model.Auth.RegisterAccountModel;
import vn.iotstar.DatingApp.Model.Response.LoginResponse;
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
	public ResponseEntity<Account> register(@RequestBody RegisterAccountModel registerUser) {
		Account registeredUser = authenticationService.signup(registerUser);
		return ResponseEntity.ok(registeredUser);
	}
	
	@PostMapping(path="/login")
	@Transactional
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginAccountModel loginUser) {
		Account authenticationUser = authenticationService.authenticate(loginUser);
		
		//changing here
		String jwtToken = jwtService.generateToken(new AccountUserDetails(authenticationUser));
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		
		return ResponseEntity.ok(loginResponse);
	}
}