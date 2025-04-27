package vn.iotstar.DatingApp.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Image;
import vn.iotstar.DatingApp.Entity.Users;

@Service
public interface IImageService {

	void delete(Image entity);

	<S extends Image> S save(S entity);

	Optional<Image> findById(Long id);

	List<Image> findAllById(Users user);

}
