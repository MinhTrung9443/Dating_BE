package vn.iotstar.DatingApp.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Dto.MatchFeedDto;
import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.Request.MessageRequest;
import vn.iotstar.DatingApp.Model.Response.ApiResponse;
import vn.iotstar.DatingApp.Service.AccountService;
import vn.iotstar.DatingApp.Service.MatchService;
import vn.iotstar.DatingApp.Service.UsersService;

@RestController
@RequestMapping("/api/users")
public class MatchController {
//	private static final Logger logger = LoggerFactory.getLogger(MatchController.class);
//
	@Autowired
	private MatchService matchService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UsersService userService;

	/**
	 * API lấy danh sách người dùng được đề xuất ghép đôi
	 *
	 * @return Danh sách người dùng được đề xuất
	 */
	@GetMapping("/suggestions")
	public ResponseEntity<List<Users>> getSuggestedMatches() {

	    try {
	        List<Users> suggestions = matchService.getSuggestedMatches();
	        return ResponseEntity.ok(suggestions);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.ok(List.of()); // Trả về danh sách rỗng thay vì lỗi
	    }
	}

	/**
	 * API lấy danh sách match đã thành công
	 *
	 * @return Danh sách match
	 */
	@GetMapping("/matches")
	public ResponseEntity<ApiResponse> getMatches() {
	    List<MatchFeedDto> matches = matchService.getMatchesForFeed(getCurrentUser());
	    return ResponseEntity.ok(ApiResponse.builder()
	    		.message("List matched")
	    		.status(200)
	    		.data(matches)
	    		.build());
	}

	/**
	 * API xem profile của người dùng khác
	 *
	 * @param id ID của người dùng cần xem profile
	 * @return Thông tin profile
	 */
	@GetMapping("/{id}/profile")
	public ResponseEntity<Users> viewProfile(@PathVariable Long id) {
	    Users user = matchService.viewProfile(id);
	    return ResponseEntity.ok(user);
	}

	/**
	 * API "like" một người dùng khác
	 *
	 * @param id ID của người dùng được like
	 * @return Kết quả like
	 */
	@PostMapping("/{id}/like")
	public ResponseEntity<String> likeUser(@PathVariable Long id) {
		boolean isMatched = matchService.likeUser(id);
		if (isMatched) {
		    return ResponseEntity.ok("Đã match với người dùng có ID: " + id);
		} else {
		    return ResponseEntity.ok("Đã like người dùng có ID: " + id);
		}
	}

	/**
	 * API "dislike" một người dùng khác
	 *
	 * @param id ID của người dùng được dislike
	 * @return Kết quả dislike
	 */
	@PostMapping("/{id}/dislike")
	public ResponseEntity<String> dislikeUser(@PathVariable Long id) {
		matchService.dislikeUser(id);
		return ResponseEntity.ok("Đã dislike người dùng có ID: " + id);
	}

	/**
	 * API "rewind" lại hành động dislike
	 *
	 * @param id ID của người dùng đã dislike trước đó
	 * @return Kết quả rewind
	 */
	@PostMapping("/{id}/rewind")
	public ResponseEntity<String> rewindDislike(@PathVariable Long id) {
	    boolean success = matchService.rewindDislike(id);
	    if (success) {
	        return ResponseEntity.ok("Đã rewind dislike cho người dùng có ID: " + id);
	    } else {
	        return ResponseEntity.badRequest().body("Không tìm thấy lịch sử dislike cho người dùng có ID: " + id);
	    }
	}

	/**
	 * API gửi tin nhắn cho người dùng khác
	 *
	 * @param id ID của người dùng nhận tin nhắn
	 * @param messageRequest Nội dung tin nhắn
	 * @return Tin nhắn đã gửi
	 */
	@PostMapping("/{id}/message")
	public ResponseEntity<Message> sendMessage(@PathVariable Long id, @RequestBody MessageRequest messageRequest) {
	    Message sentMessage = matchService.sendMessage(id, messageRequest.getContent());
	    return ResponseEntity.ok(sentMessage);
	}

	/**
	 * API xem danh sách người đã like mình (Tính năng Gold của Tinder)
	 *
	 * @return Danh sách người đã like mình
	 */
	@GetMapping("/my-likers")
	public ResponseEntity<List<Users>> getMyLikers() {
	    List<Users> likers = matchService.getMyLikers();
	    return ResponseEntity.ok(likers);
	}

	/**
	 * API đếm số người đã like mình
	 *
	 * @return Số lượng người đã like
	 */
	@GetMapping("/pending-likes/count")
	public ResponseEntity<Map<String, Integer>> countPendingLikes() {
	    int count = matchService.countPendingLikes();
	    return ResponseEntity.ok(Map.of("count", count));
	}

	/**
	 * API lấy danh sách lượt like gần đây
	 *
	 * @param limit Số lượng lượt like cần lấy
	 * @return Danh sách lượt like
	 */
	@GetMapping("/recent-likes")
	public ResponseEntity<List<MatchList>> getRecentLikes(
	        @RequestParam(defaultValue = "10") int limit) {
	    List<MatchList> recentLikes = matchService.getRecentLikes(limit);
	    return ResponseEntity.ok(recentLikes);
	}

	/**
	 * API lấy danh sách người đã dislike
	 *
	 * @return Danh sách người đã dislike
	 */
	@GetMapping("/disliked")
	public ResponseEntity<List<Users>> getDislikedUsers() {
	    List<Users> dislikedUsers = matchService.getDislikedUsers();
	    return ResponseEntity.ok(dislikedUsers);
	}

	// Helper method để lấy user hiện tại
    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }
        String userEmail = authentication.getName();
        Account currentAcc = accountService.findAccountByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản cho email: " + userEmail));
        return userService.findByAccount(currentAcc)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng cho tài khoản: " + userEmail));
    }
}
