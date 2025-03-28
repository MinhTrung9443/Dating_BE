package vn.iotstar.DatingApp.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Image;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.ImageModel;
import vn.iotstar.DatingApp.Model.UserModel;
import vn.iotstar.DatingApp.Service.IImageService;
import vn.iotstar.DatingApp.Service.IUserService;

@RestController
@RequestMapping("/profile")
public class ProfileController {
	@Autowired
	IUserService userService;
	@Autowired
	IImageService imageService;
	@PostMapping("/get")
	public ResponseEntity<?> getProfile(Long userId)
	{
		Users user;
		Optional<Users> optionalUser = userService.findById(userId);
		if (optionalUser.isPresent())
		{
			user = optionalUser.get();
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.badRequest().body(null);
	}
	
	@PostMapping("/updateProfile")
	public ResponseEntity<?> updateProfile(@RequestBody UserModel userInfo)			// con loi kieu du lieu Date
	{
		System.out.println(userInfo.toString());
		Optional<Users> user = userService.findById(userInfo.getId());
		if (user.isPresent()) {
			Users updateUser = user.get();
			BeanUtils.copyProperties(userInfo, updateUser);
			userService.save(updateUser);
			return ResponseEntity.ok(null);
		}
		return ResponseEntity.badRequest().body(null);
	}
	
	@PostMapping("/removeImage")
	public ResponseEntity<?> removeImage(@RequestBody ImageModel image)
	{
		Image delImage = new Image();
		delImage = imageService.findById(image.getId()).get();
		delImage.setUser(null);
		imageService.save(delImage);
		imageService.delete(delImage);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/addImage")
	public ResponseEntity<?> addImage(@RequestBody ImageModel image)
	{
		Image newImage = new Image();
		BeanUtils.copyProperties(image, newImage);
		Optional<Users> user = userService.findById(image.getUserId());
		newImage.setUser(user.get());
		imageService.save(newImage);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/getAllImage")
	public ResponseEntity<?> getAllImage(Long userId)
	{
		List<Image> listImage = new ArrayList<>();
		Optional<Users> user = userService.findById(userId);
		listImage = user.get().getImages();
		return ResponseEntity.ok(listImage);
	}
}
