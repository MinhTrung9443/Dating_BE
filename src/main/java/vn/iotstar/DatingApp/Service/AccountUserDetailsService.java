package vn.iotstar.DatingApp.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Model.AccountUserDetails;
import vn.iotstar.DatingApp.Repository.AccountRepository;

@Service
public class AccountUserDetailsService implements UserDetailsService{
	@Autowired
	private AccountRepository accountRepo;
	
	public  AccountUserDetailsService(AccountRepository accountRepo) {
		this.accountRepo = accountRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<Account> user = accountRepo.findAccountByEmail(username);
		
		return user.map(AccountUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("user not found: " + username));

	}
}
