package vn.iotstar.DatingApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.Auth.LoginRequest;
import vn.iotstar.DatingApp.Model.Auth.RegisterRequest;
import vn.iotstar.DatingApp.Repository.AccountRepository;
import vn.iotstar.DatingApp.Repository.UsersRepository;

@Service
public class AuthenticationService {
	@Autowired
	private UsersRepository usersRepo;
	
	private final AccountRepository accountRepository;
	
	private final AuthenticationManager authenticationManager;
	
	private final PasswordEncoder passwordEncoder;
	
	public AuthenticationService(
			AccountRepository accountRepository,
			AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder
			) {
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.accountRepository = accountRepository;
	}
	
	@Transactional
	public Account signup(RegisterRequest input) {
		Account user = new Account();

		user.setEmail(input.getEmail());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		// set role to USER
		user.setRole("USER");
		
		// infor -> ??? check
		Users info = Users.builder()
						.phone(input.getPhone())
						.name(input.getName())
						.gender(input.getGender())
						.sexualOrientation(input.getSexualOrientation())
						.birthday(input.getBirthday())
						.biography(input.getBiography())
						.height(input.getHeight())
						.weight(input.getWeight())
						.zodiacSign(input.getZodiacSign())
						.personalityType(input.getPersonalityType())
						.interests(input.getInterests())
						.address(input.getAddress())
						.job(input.getJob())
						.account(user)
						.build();
		
		usersRepo.save(info);
		
		return accountRepository.save(user);
	}
	
	public Account authenticate(LoginRequest input) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
				);
		return accountRepository.findAccountByEmail(input.getEmail())
				.orElseThrow();
	}
}
