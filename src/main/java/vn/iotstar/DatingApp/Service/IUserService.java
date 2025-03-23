package vn.iotstar.DatingApp.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Users;

@Service
public interface IUserService {

	Optional<Users> findById(Long id);

	<S extends Users> S save(S entity);

	List<Users> findByInterests(String interests);

	List<Users> findBySexualOrientation(String sexualOrientation);

}
