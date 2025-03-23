package vn.iotstar.DatingApp.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Service.IBlockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/block")
public class BlockController {
	@Autowired
	IBlockService blockService;
	@PostMapping("/")
	public ResponseEntity<?> blockUser(@RequestBody MatchList blockUser) {
		// kiem tra coi co match chua, chua thi them moi
		Optional<MatchList> matchList = blockService.findByUser1AndUser2(blockUser.getUser1(), blockUser.getUser2());
		if (!matchList.isPresent())
		{
			blockService.save(blockUser);
		}
		// da match roi goi ham update
		else {
			blockUser.setStatus("BLOCK");
			blockService.save(blockUser);
		}
		
		return ResponseEntity.ok("Block");
	}
	
	@PostMapping("/getAll")
	public ResponseEntity<?> getAllBlockuser(@RequestBody Users user)
	{
		List<MatchList> listMatchBlockUser = blockService.findAllByUser1(user);
		
		return ResponseEntity.ok(listMatchBlockUser);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<?> unBlockUser(@RequestBody MatchList unBlockUser){
		
		Optional<MatchList> matchList = blockService.findByUser1AndUser2(unBlockUser.getUser1(), unBlockUser.getUser2());
		if (matchList.isPresent())
		{
			blockService.delete(matchList.get());
			return ResponseEntity.ok("oke");
		}
		
		return ResponseEntity.badRequest().body("ERROR");
	}
	
	
	
}
