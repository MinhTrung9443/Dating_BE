package vn.iotstar.DatingApp.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService{
	@Autowired
	private AccountRepository accountRepo;
	@Autowired 
	private PasswordEncoder passEncoder;
	
	@Override
	public Account addNewAccount(Account newAccount) {
		// encode pass
		newAccount.setPassword(passEncoder.encode(newAccount.getPassword()));
		// save newAccount to DB
		accountRepo.save(newAccount);
		return newAccount;
	}

	@Override
	public Optional<Account> findAccountByEmail(String email) {
		return accountRepo.findAccountByEmail(email);
	}
	
	
}
