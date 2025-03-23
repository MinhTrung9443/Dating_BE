package vn.iotstar.DatingApp.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Service.IUserService;

@RestController
@RequestMapping("/searchCard")
public class SearchCardController {
	@Autowired
	IUserService userService;
	@PostMapping("/find/SexualOrientation")
	public ResponseEntity<?> findBySexualOrientation(@RequestBody String SexualOrientation)
	{
		List<Users> listUser = new ArrayList<>();
		listUser = userService.findBySexualOrientation(SexualOrientation);
		return ResponseEntity.ok(listUser);
		
	}
	
	@PostMapping("/find/interests")
	public ResponseEntity<?> findByInterests(@RequestBody String interests)
	{
		List<Users> listUser = new ArrayList<>();
		listUser = userService.findByInterests(interests);
		return ResponseEntity.ok(listUser);
		
	}
}
