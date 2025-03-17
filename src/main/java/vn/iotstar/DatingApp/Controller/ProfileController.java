package vn.iotstar.DatingApp.Controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Image;
import vn.iotstar.DatingApp.Entity.Users;

@RestController
@RequestMapping("/profile")
public class ProfileController {
	@PostMapping("/get")
	public ResponseEntity<Users> getProfile(@RequestBody String userId)
	{
		Users user = new Users();
		
		
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/updateProfile")
	public ResponseEntity<?> updateProfile(@RequestBody Users userInfo)
	{
		
		
		
		return ResponseEntity.ok("Update thanh cong");
	}
	
	@PostMapping("/removeImage")
	public ResponseEntity<?> removeImage(@RequestBody Image image)
	{
		return ResponseEntity.ok("Xoa thanh cong");
	}
	
	@PostMapping("/addImage")
	public ResponseEntity<?> addImage(@RequestBody Image image)
	{
		return ResponseEntity.ok("Them anh thanh cong");
	}
}
