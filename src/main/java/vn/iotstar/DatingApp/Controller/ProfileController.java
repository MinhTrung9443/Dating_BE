package vn.iotstar.DatingApp.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Image;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Service.IImageService;
import vn.iotstar.DatingApp.Service.IUserService;
import vn.iotstar.DatingApp.Service.Impl.UserService;

@RestController
@RequestMapping("/profile")
public class ProfileController {
	@Autowired
	IUserService userService;
	@Autowired
	IImageService imageService;
	@PostMapping("/get")
	public ResponseEntity<?> getProfile(@RequestBody Long userId)
	{
		Users user;
		Optional<Users> optionalUser = userService.findById(userId);
		if (optionalUser.isPresent())
		{
			user = optionalUser.get();
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.badRequest().body("ERROR");
	}
	
	@PostMapping("/updateProfile")
	public ResponseEntity<?> updateProfile(@RequestBody Users userInfo)
	{
		userService.save(userInfo);
				
		return ResponseEntity.ok("Update thanh cong");
	}
	
	@PostMapping("/removeImage")
	public ResponseEntity<?> removeImage(@RequestBody Image image)
	{
		imageService.delete(image);
		return ResponseEntity.ok("Xoa thanh cong");
	}
	
	@PostMapping("/addImage")
	public ResponseEntity<?> addImage(@RequestBody Image image)
	{
		imageService.save(image);
		return ResponseEntity.ok("Them anh thanh cong");
	}
}
