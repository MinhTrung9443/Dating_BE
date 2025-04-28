package vn.iotstar.DatingApp.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	public ResponseEntity<?> findBySexualOrientation(String SexualOrientation)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<Users> listUser = new ArrayList<>();
		listUser = userService.findBySexualOrientation(SexualOrientation);
		return ResponseEntity.ok(listUser);
		
	}
	
	@PostMapping("/find/interests")
	public ResponseEntity<?> findByInterests(String interests)
	{
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			List<Users> listUser = new ArrayList<>();
			listUser = userService.findByInterests(interests);
			return ResponseEntity.ok(listUser);
		} catch (Exception e) {
			return ResponseEntity.ok(List.of());
		}
		
	}
	
	@PostMapping("/find/zodiacSign")
	public ResponseEntity<?> findByZodiacSign(String zodiacSign)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<Users> listUser = new ArrayList<>();
		listUser = userService.findByZodiacSign(zodiacSign);
		return ResponseEntity.ok(listUser);
		
	}
	
	// search xong con tinh toan lai sap xep theo vi tri
}
