package vn.iotstar.DatingApp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Image;
import vn.iotstar.DatingApp.Entity.Users;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	List<Image> findAllByUser(Users user);
}
