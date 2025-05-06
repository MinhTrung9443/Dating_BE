package vn.iotstar.DatingApp.Service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Model.AccountUserDetails;
import vn.iotstar.DatingApp.Repository.AccountRepository;

@Service
public class AccountUserDetailsService implements UserDetailsService{

	private final AccountRepository accountRepo;

	public  AccountUserDetailsService(AccountRepository accountRepo) {
		this.accountRepo = accountRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Account account = accountRepo.findAccountByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Important: Set the username to the email to ensure it's returned by SecurityContextHolder
//        return org.springframework.security.core.userdetails.User
//                .withUsername(account.getEmail()) // Use email as the username
//                .password(account.getPassword())
//                .roles(account.getRole())
//                .build();
		return new AccountUserDetails(account);

	}
}
