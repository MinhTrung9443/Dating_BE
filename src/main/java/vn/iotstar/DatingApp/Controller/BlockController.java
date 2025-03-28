package vn.iotstar.DatingApp.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.MatchListModel;
import vn.iotstar.DatingApp.Service.IBlockService;
import vn.iotstar.DatingApp.Service.IUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/block")
public class BlockController {
	@Autowired
	IUserService userService;
	@Autowired
	IBlockService blockService;
	@PostMapping("/")
	public ResponseEntity<?> blockUser(@RequestBody MatchListModel blockUser) {
		Users user1 = userService.findById(blockUser.getUser1()).get();
		Users user2 = userService.findById(blockUser.getUser2()).get();
		Optional<MatchList> matchList = blockService.findByUser1AndUser2(user1, user2);
		if (!matchList.isPresent())
		{
			MatchList block = new MatchList();
			BeanUtils.copyProperties(blockUser, block);
			block.setUser1(user1);
			block.setUser2(user2);
			block.setCreatedAt(new Date());
			blockService.save(block);
		}
		// da match roi goi ham update
		else {
			MatchList block = matchList.get();
			block.setStatus("BLOCK");
			block.setCreatedAt(new Date());
			blockService.save(block);
		}
		
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/getAll")
	public ResponseEntity<?> getAllBlockuser(Long userId)
	{
		Users user = userService.findById(userId).get();
		List<MatchList> listMatchBlockUser = blockService.findAllByUser1AndStatus(user,"BLOCK");
		
		return ResponseEntity.ok(listMatchBlockUser);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<?> unBlockUser(@RequestBody MatchListModel unBlockUser){
		Users user1 = userService.findById(unBlockUser.getUser1()).get();
		Users user2 = userService.findById(unBlockUser.getUser2()).get();
		Optional<MatchList> matchList = blockService.findByUser1AndUser2(user1, user2);
		if (matchList.isPresent())
		{
			blockService.delete(matchList.get());
			return ResponseEntity.ok(null);
		}
		
		return ResponseEntity.badRequest().body(null);
	}
	
	
	
}
