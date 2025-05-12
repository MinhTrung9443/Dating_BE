package vn.iotstar.DatingApp.Controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.MatchListModel;
import vn.iotstar.DatingApp.Repository.AccountRepository;
import vn.iotstar.DatingApp.Service.IBlockService;
import vn.iotstar.DatingApp.Service.IUserService;


@RestController
@RequestMapping("/api/block")
public class BlockController {
	@Autowired
	IUserService userService;
	@Autowired
	IBlockService blockService;
	@Autowired
	AccountRepository accountRepository;
	/**
	 * API block người dùng
	 *
	 *
	 */
	@PostMapping("/")
	public ResponseEntity<?> blockUser(@RequestBody MatchListModel blockUser) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }
		String username = authentication.getName();

		Account acc = accountRepository.findAccountByEmail(username).get();
		Users user1 = userService.findByAccount(acc).get();
		Users user2 = userService.findById(blockUser.getUser2()).get();
		Optional<MatchList> matchList1 = blockService.findByUser1AndUser2(user1, user2);
		Optional<MatchList> matchList2 = blockService.findByUser1AndUser2(user2, user1);
		if (!matchList1.isPresent() && !matchList2.isPresent())
		{
			MatchList block = new MatchList();
			BeanUtils.copyProperties(blockUser, block);
			block.setUser1(user1);
			block.setUser2(user2);
			block.setCreatedAt(new Date());
			blockService.save(block);
		}
		// da match roi goi ham update
		else if (matchList1.isPresent()){
			MatchList block = matchList1.get();
			block.setStatus("BLOCK");
			block.setCreatedAt(new Date());
			blockService.save(block);
		}
		else {
			MatchList block = matchList2.get();
			block.setStatus("BLOCK");
			block.setCreatedAt(new Date());
			blockService.save(block);
		}

		return ResponseEntity.ok(null);
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
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }
		Users user = userService.findById(userId).get();
		List<MatchList> listMatchBlockUser = blockService.findAllByUser1AndStatus(user,"BLOCK");

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
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }
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
