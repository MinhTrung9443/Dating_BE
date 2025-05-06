package vn.iotstar.DatingApp.Service;

import java.sql.Date;
import java.util.Optional;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.Users;

public interface UsersService {

	Optional<Users> findByAccount(Account account);

	Integer calculateAge(Date dob);

}
