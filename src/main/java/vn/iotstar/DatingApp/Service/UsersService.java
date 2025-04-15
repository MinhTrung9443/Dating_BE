package vn.iotstar.DatingApp.Service;

import java.util.Optional;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.Users;

public interface UsersService {

	Optional<Users> findByAccount(Account account);

}
