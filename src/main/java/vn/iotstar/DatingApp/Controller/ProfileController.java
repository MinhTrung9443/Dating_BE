package vn.iotstar.DatingApp.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	/**
	 * API lấy thông tin người dùng
	 * 
	 * 
	 */
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
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
	
	/**
	 * API Cập nhật thông tin người dùng
	 * 
	 * 
	 */
	@PostMapping("/updateProfile")
	public ResponseEntity<?> updateProfile(@RequestBody UserModel userInfo) throws ParseException			
	{
		Date date = formatter.parse(userInfo.getBirthday());
		Optional<Users> user = userService.findById(userInfo.getId());
		if (user.isPresent()) {
			Users updateUser = user.get();
			BeanUtils.copyProperties(userInfo, updateUser);
			updateUser.setBirthday(date);
			userService.save(updateUser);
			return ResponseEntity.ok(null);
		}
		return ResponseEntity.badRequest().body(null);
	}
	/**
	 * API xóa ảnh trong profile
	 * 
	 * 
	 */
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
	/**
	 * API thêm ảnh vào profile
	 * 
	 * 
	 */
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
	/**
	 * API lấy tất cả ảnh trong profile của người dùng
	 * 
	 * 
	 */
	@PostMapping("/getAllImage")
	public ResponseEntity<?> getAllImage(Long userId)
	{
		List<Image> listImage = new ArrayList<>();
		Optional<Users> user = userService.findById(userId);
		listImage = user.get().getImages();
		return ResponseEntity.ok(listImage);
	}
	/**
	 * API lấy thông tin về mục tiêu tìm kiếm
	 * 
	 * 
	 */
	
	@GetMapping("/getSearch")
	public ResponseEntity<?> getSearch(Long userId){
		Users user = userService.findById(userId).get();
		return ResponseEntity.ok(user.getSearchCriteria());
	}
	
}
