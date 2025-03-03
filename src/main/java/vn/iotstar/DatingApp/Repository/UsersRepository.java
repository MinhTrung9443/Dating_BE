package vn.iotstar.DatingApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{

}
