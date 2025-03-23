package vn.iotstar.DatingApp.Service;

import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Image;

@Service
public interface IImageService {

	void delete(Image entity);

	<S extends Image> S save(S entity);

}
