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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/block")
public class BlockController {

	@Autowired
	IBlockService blockService;
	/**
	 * API block người dùng
	 * 
	 * 
	 */
	@PostMapping("/")
	public ResponseEntity<?> blockUser(@RequestBody MatchListModel blockUser) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		int result = blockService.blockUser(blockUser);
		
		return result == 1 ? ResponseEntity.ok(null) : ResponseEntity.badRequest().body(null);
	}
	/**
	 * API lấy danh sách người dùng bị blok
	 * 
	 * @return danh sách người bị block
	 */
	@PostMapping("/getAll")
	public ResponseEntity<?> getAllBlockuser(Long userId)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<MatchList> listMatchBlockUser = blockService.getAllBlockuser(userId);
		return ResponseEntity.ok(listMatchBlockUser);
	}
	/**
	 * API bỏ block
	 * 
	 * 
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> unBlockUser(@RequestBody MatchListModel unBlockUser){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		int result = blockService.unBlockUser(unBlockUser);
		
		return result == 1 ? ResponseEntity.ok(null) : ResponseEntity.badRequest().body(null);
	}
	
	
	
}
