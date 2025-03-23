package vn.iotstar.DatingApp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	List<Users> findBySexualOrientation(String sexualOrientation);
	List<Users> findByInterests(String interests);
	
}
