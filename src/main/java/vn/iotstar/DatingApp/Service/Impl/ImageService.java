package vn.iotstar.DatingApp.Service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Image;
import vn.iotstar.DatingApp.Repository.ImageRepository;
import vn.iotstar.DatingApp.Service.IImageService;

@Service
public class ImageService implements IImageService {
	@Autowired
	ImageRepository imageRepository;

	@Override
	public <S extends Image> S save(S entity) {
		return imageRepository.save(entity);
	}

	@Override
	public void delete(Image entity) {
		imageRepository.delete(entity);
	}

	@Override
	public Optional<Image> findById(Long id) {
		return imageRepository.findById(id);
	}
	

}
