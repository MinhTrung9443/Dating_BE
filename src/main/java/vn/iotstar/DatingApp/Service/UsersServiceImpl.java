package vn.iotstar.DatingApp.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService{
	
	@Autowired
	private UsersRepository userRepository;

	@Override
	public Optional<Users> findByAccount(Account account) {
		return userRepository.findByAccount(account);
	}
	
	
}
