package vn.iotstar.DatingApp.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.UsersRepository;
import vn.iotstar.DatingApp.Service.IUserService;

@Service
public class UserService implements IUserService{
	@Autowired
	UsersRepository userRepository;

	@Override
	public Optional<Users> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public <S extends Users> S save(S entity) {
		return userRepository.save(entity);
	}

	@Override
	public List<Users> findBySexualOrientation(String sexualOrientation) {
		return userRepository.findBySexualOrientation(sexualOrientation);
	}

	@Override
	public List<Users> findByInterests(String interests) {
		return userRepository.findByInterests(interests);
	}

	@Override
	public List<Users> findByZodiacSign(String zodiacSign) {
		return userRepository.findByZodiacSign(zodiacSign);
	}
	
	
}
