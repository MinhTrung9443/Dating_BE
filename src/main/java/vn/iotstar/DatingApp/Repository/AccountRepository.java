package vn.iotstar.DatingApp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	// find user account by email
	Optional<Account> findAccountByEmail(String email);
}
