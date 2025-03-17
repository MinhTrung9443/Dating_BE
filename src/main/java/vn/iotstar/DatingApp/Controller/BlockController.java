package vn.iotstar.DatingApp.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/block")
public class BlockController {
	
	@PostMapping("/")
	public ResponseEntity<?> blockUser(@RequestBody MatchList blockUser) {
		// kiem tra coi co match chua, chua thi them moi
		
		
		// da match roi goi ham update
		
		return ResponseEntity.ok("Block");
	}
	
	@PostMapping("/getAll")
	public ResponseEntity<List<Users>> getAllBlockuser(@RequestBody Users user)
	{
		List<Users> listBlockUser = new ArrayList<>();
		
		return ResponseEntity.ok(listBlockUser);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<?> unBlockUser(@RequestBody MatchList unBlockUser){
		
		
		return ResponseEntity.ok("oke");
	}
	
	
	
}
