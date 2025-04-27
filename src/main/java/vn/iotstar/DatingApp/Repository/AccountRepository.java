package vn.iotstar.DatingApp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	// find user account by email
	@Query("SELECT a FROM Account a WHERE a.email = :email")
    Optional<Account> findAccountByEmail(@Param("email") String email);
}
