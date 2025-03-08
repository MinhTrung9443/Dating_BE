package vn.iotstar.DatingApp.Service;

import java.util.Optional;

import vn.iotstar.DatingApp.Entity.Account;

public interface AccountService {
	Account addNewAccount(Account newAccount);

	Optional<Account> findAccountByEmail(String email);
}
