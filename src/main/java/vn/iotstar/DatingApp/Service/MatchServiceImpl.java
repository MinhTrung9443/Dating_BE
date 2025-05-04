package vn.iotstar.DatingApp.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.iotstar.DatingApp.Dto.MatchFeedDto;
import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.AccountRepository;
import vn.iotstar.DatingApp.Repository.MatchListRepository;
import vn.iotstar.DatingApp.Repository.MessageRepository;
import vn.iotstar.DatingApp.Repository.UsersRepository;

@Service
public class MatchServiceImpl implements MatchService {
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private MatchListRepository matchListRepository;
	@Autowired
	private MessageRepository messageRepository;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * Xử lý khi người dùng hiện tại "like" một người dùng khác
	 *
	 * @param targetUserId ID của người dùng được like
	 * @return true nếu match thành công (cả hai đã like nhau), false nếu chỉ là like đơn phương
	 */
	@Override
	@Transactional
	public boolean likeUser(Long targetUserId) {
		System.out.println("User " + targetUserId + " liked.");

		// Lấy thông tin người dùng hiện tại
		Users currentUser = getCurrentUser();
		Users targetUser = usersRepository.findById(targetUserId)
				.orElseThrow(() -> new RuntimeException("Target user not found"));

		// Kiểm tra nếu like chính mình
		if (currentUser.getId().equals(targetUserId)) {
			throw new RuntimeException("Cannot like yourself");
		}

		// Kiểm tra xem người dùng đích đã like hiện tại chưa
		Optional<MatchList> reverseMatch = matchListRepository.findByUser1AndUser2(targetUser, currentUser);
		boolean isMatched = false;

		if (reverseMatch.isPresent()) {
			// TH2: Nếu người dùng đích đã like người dùng hiện tại, tạo match
			MatchList existingMatch = reverseMatch.get();

			if ("LIKE".equals(existingMatch.getStatus()) || "PENDING".equals(existingMatch.getStatus())) {
				existingMatch.setStatus("MATCHED");
				existingMatch.setUpdatedAt(new Date());
				matchListRepository.save(existingMatch);

				// Tạo hoặc cập nhật bản ghi like của người dùng hiện tại
				Optional<MatchList> currentMatch = matchListRepository.findByUser1AndUser2(currentUser, targetUser);
				if (currentMatch.isPresent()) {
					MatchList match = currentMatch.get();
					match.setStatus("MATCHED");
					match.setUpdatedAt(new Date());
					matchListRepository.save(match);
				} else {
					MatchList newMatch = new MatchList();
					newMatch.setUser1(currentUser);
					newMatch.setUser2(targetUser);
					newMatch.setStatus("MATCHED");
					newMatch.setCreatedAt(new Date());
					newMatch.setUpdatedAt(new Date());
					matchListRepository.save(newMatch);
				}

				isMatched = true;
				System.out.println(
						"It's a match! User " + currentUser.getId() + " and User " + targetUserId + " matched!");
			}
		} else {
			// Kiểm tra xem đã có trạng thái dislike hay chưa
			Optional<MatchList> existingMatch = matchListRepository.findByUser1AndUser2(currentUser, targetUser);

			if (existingMatch.isPresent()) {
				MatchList match = existingMatch.get();
				// Nếu đã dislike trước đó, cập nhật thành like
				match.setStatus("LIKE");
				match.setUpdatedAt(new Date());
				matchListRepository.save(match);
			} else {
				// Tạo bản ghi LIKE mới
				MatchList newMatch = new MatchList();
				newMatch.setUser1(currentUser);
				newMatch.setUser2(targetUser);
				newMatch.setStatus("LIKE");
				newMatch.setCreatedAt(new Date());
				newMatch.setUpdatedAt(new Date());
				matchListRepository.save(newMatch);
			}
		}

		return isMatched;
	}

	@Override
	@Transactional
    public void dislikeUser(Long targetUserId) {
        System.out.println("User " + targetUserId + " disliked.");

        // Lấy thông tin người dùng hiện tại
        Users currentUser = getCurrentUser();
        Users targetUser = usersRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        // Kiểm tra nếu dislike chính mình
        if (currentUser.getId().equals(targetUserId)) {
            throw new RuntimeException("Cannot dislike yourself");
        }

        // Kiểm tra xem đã có bản ghi nào chưa
        Optional<MatchList> existingMatch = matchListRepository.findByUser1AndUser2(currentUser, targetUser);

        if (existingMatch.isPresent()) {
            // Cập nhật bản ghi hiện có thành DISLIKE
            MatchList match = existingMatch.get();
            match.setStatus("DISLIKE");
            match.setUpdatedAt(new Date());
            matchListRepository.save(match);
        } else {
            // Tạo bản ghi DISLIKE mới
            MatchList newMatch = new MatchList();
            newMatch.setUser1(currentUser);
            newMatch.setUser2(targetUser);
            newMatch.setStatus("DISLIKE");
            newMatch.setCreatedAt(new Date());
            newMatch.setUpdatedAt(new Date());
            matchListRepository.save(newMatch);
        }
    }

	@Override
	@Transactional
	public Message sendMessage(Long targetUserId, String content) {
	    // Lấy thông tin người dùng hiện tại
	    Users currentUser = getCurrentUser();
	    Users targetUser = usersRepository.findById(targetUserId)
	            .orElseThrow(() -> new RuntimeException("Target user not found"));

	    // Kiểm tra nếu nhắn tin cho chính mình
	    if (currentUser.getId().equals(targetUserId)) {
	        throw new RuntimeException("Cannot send message to yourself");
	    }

	    // Kiểm tra xem đã match chưa
	    Optional<MatchList> match1 = matchListRepository.findByUser1AndUser2AndStatus(currentUser, targetUser, "MATCHED");
	    Optional<MatchList> match2 = matchListRepository.findByUser1AndUser2AndStatus(targetUser, currentUser, "MATCHED");

	    if (match1.isEmpty() && match2.isEmpty()) {
	        throw new RuntimeException("Cannot send message without matching first");
	    }

	    // Lấy match object
	    MatchList matchList = match1.isPresent() ? match1.get() : match2.get();

	    // Tạo tin nhắn mới
	    Message message = new Message();
	    message.setFromUser(currentUser);
	    message.setToUser(targetUser);
	    message.setMessageContent(content);
	    message.setSentAt(new Date());
	    message.setLiked(false);
	    message.setRead(false);
	    message.setMatchlist(matchList);

	    return messageRepository.save(message);
	}

	@Override
	@Transactional
	public boolean rewindDislike(Long targetUserId) {
	    // Lấy thông tin người dùng hiện tại
	    Users currentUser = getCurrentUser();
	    Users targetUser = usersRepository.findById(targetUserId)
	            .orElseThrow(() -> new RuntimeException("Target user not found"));

	    // Kiểm tra nếu là chính mình
	    if (currentUser.getId().equals(targetUserId)) {
	        throw new RuntimeException("Cannot rewind dislike for yourself");
	    }

	    // Tìm bản ghi dislike gần nhất
	    Optional<MatchList> existingMatch = matchListRepository.findByUser1AndUser2AndStatus(
	            currentUser, targetUser, "DISLIKE");

	    if (existingMatch.isPresent()) {
	        MatchList match = existingMatch.get();
	        // Cập nhật trạng thái thành REWIND
	        match.setStatus("REWIND");
	        match.setUpdatedAt(new Date());
	        matchListRepository.save(match);
	        return true;
	    }

	    return false;
	}

	@Override
	@Transactional
	public Users viewProfile(Long targetUserId) {
	    // Lấy thông tin người dùng hiện tại
	    Users currentUser = getCurrentUser();
	    Users targetUser = usersRepository.findById(targetUserId)
	            .orElseThrow(() -> new RuntimeException("Target user not found"));

	    // Cập nhật trạng thái đã xem profile
	    Optional<MatchList> existingMatch = matchListRepository.findByUser1AndUser2(currentUser, targetUser);

	    if (existingMatch.isPresent()) {
	        MatchList match = existingMatch.get();
	        match.setProfileViewed(true);
	        match.setUpdatedAt(new Date());
	        matchListRepository.save(match);
	    }

	    return targetUser;
	}

	@Override
	public List<Users> getSuggestedMatches() {
	    try {
	        // Lấy thông tin người dùng hiện tại
	        Users currentUser = getCurrentUser();

	        // Lấy danh sách tất cả người dùng
	        List<Users> allUsers = usersRepository.findAll();

	        // Lọc các người dùng đã like/dislike
	        List<MatchList> interactions = matchListRepository.findByUser1(currentUser);
	        List<Long> interactedUserIds = interactions.stream()
	                .map(match -> match.getUser2().getId())
	                .collect(Collectors.toList());

	        // Lọc ra những người chưa tương tác và khác chính mình
	        return allUsers.stream()
	                .filter(user -> !user.getId().equals(currentUser.getId()) && !interactedUserIds.contains(user.getId()))
	                .collect(Collectors.toList());
	    } catch (Exception e) {
	        // Ghi log lỗi và trả về danh sách trống
	        System.err.println("Lỗi khi lấy danh sách gợi ý: " + e.getMessage());
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	@Override
	public List<MatchList> getMatches() {
	    // Lấy thông tin người dùng hiện tại
	    Users currentUser = getCurrentUser();

	    // Tìm tất cả các match đã thành công
	    List<MatchList> matchesAsUser1 = matchListRepository.findByUser1AndStatus(currentUser, "MATCHED");
	    List<MatchList> matchesAsUser2 = matchListRepository.findByUser2AndStatus(currentUser, "MATCHED");

	    // Gộp lại và loại bỏ trùng lặp
	    List<MatchList> allMatches = new ArrayList<>(matchesAsUser1);
	    allMatches.addAll(matchesAsUser2);

	    return allMatches;
	}

	@Override
	public List<Users> getMyLikers() {
	    // Lấy thông tin người dùng hiện tại
	    Users currentUser = getCurrentUser();

	    // Tìm các bản ghi mà người khác đã like mình nhưng chưa match
	    List<MatchList> pendingLikes = matchListRepository.findByUser2AndStatus(currentUser, "LIKE");

	    // Lấy thông tin user từ các bản ghi trên
	    return pendingLikes.stream()
	            .map(MatchList::getUser1)
	            .collect(Collectors.toList());
	}

	@Override
	public List<MatchList> getRecentLikes(int limit) {
	    // Lấy thông tin người dùng hiện tại
	    Users currentUser = getCurrentUser();

	    // Tìm các like gần đây của người dùng hiện tại
	    List<MatchList> recentLikes = matchListRepository.findByUser1AndStatusOrderByUpdatedAtDesc(
	            currentUser, "LIKE");

	    // Giới hạn số lượng kết quả trả về
	    if (recentLikes.size() > limit) {
	        return recentLikes.subList(0, limit);
	    }

	    return recentLikes;
	}

	@Override
	public List<Users> getDislikedUsers() {
	    // Lấy thông tin người dùng hiện tại
	    Users currentUser = getCurrentUser();

	    // Tìm các bản ghi dislike
	    List<MatchList> dislikes = matchListRepository.findByUser1AndStatus(currentUser, "DISLIKE");

	    // Lấy thông tin user từ các bản ghi trên
	    return dislikes.stream()
	            .map(MatchList::getUser2)
	            .collect(Collectors.toList());
	}

	@Override
	public int countPendingLikes() {
	    // Lấy thông tin người dùng hiện tại
	    Users currentUser = getCurrentUser();

	    // Đếm số lượt like nhận được mà chưa match
	    return matchListRepository.countByUser2AndStatus(currentUser, "LIKE");
	}

	/**
	 * Lấy thông tin người dùng hiện tại
	 *
	 * @return Thông tin người dùng hiện tại
	 */
	private Users getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		Account acc = accountRepository.findAccountByEmail(username)
		        .orElseThrow(() -> new RuntimeException("Account not found"));
		return usersRepository.findByAccount(acc)
		        .orElseThrow(() -> new RuntimeException("Current user not found"));
	}
	
	@Override
	public List<MatchFeedDto> getMatchesForFeed(Users currentUser) {
        // Get matches where current user is either user1 or user2 and status is MATCHED
        List<MatchList> matchesAsUser1 = matchListRepository.findByUser1AndStatus(currentUser, "MATCHED");
        List<MatchList> matchesAsUser2 = matchListRepository.findByUser2AndStatus(currentUser, "MATCHED");
        
        // Combine both lists and convert to DTOs
        return Stream.concat(matchesAsUser1.stream(), matchesAsUser2.stream())
                .map(this::convertToMatchFeedDto)
                .collect(Collectors.toList());
    }
    
    private MatchFeedDto convertToMatchFeedDto(MatchList matchList) {
        Users currentUser = getCurrentUser();
        Users otherUser = matchList.getUser1().equals(currentUser) ? matchList.getUser2() : matchList.getUser1();
        
        return MatchFeedDto.builder()
                .id(matchList.getId().toString())
                .name(otherUser.getName())
                .pictureUrl(getFirstUserImage(otherUser))
                .location(otherUser.getAddress())
                .date(dateFormat.format(matchList.getCreatedAt()))
                .isNewMatch(isNewMatch(matchList.getCreatedAt()))
                .build();
    }
    
    private String getFirstUserImage(Users user) {
        if (user.getImages() == null || user.getImages().isEmpty()) {
            return ""; // or default image URL
        }
        return user.getImages().get(0).getImage();
    }
	
	// Helper tìm User hoặc ném Exception
//    private Users findUserByIdOrThrow(Long userId) {
//        return usersRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
//    }
    
    //----- Mapper for MatchFeed
    private MatchFeedDto toMatchFeedDto(MatchList matchList) {
        // Determine which user is the other user in the match (assuming current user is user1)
        Users otherUser = matchList.getUser2();
        
        return new MatchFeedDto(
            matchList.getId().toString(),
            otherUser.getName(),
            otherUser.getImages().get(0).getImage(), // Assuming Users entity has getPicture() method
            otherUser.getAddress(), // Assuming Users entity has getLocation() method
            dateFormat.format(matchList.getCreatedAt()),
            isNewMatch(matchList.getCreatedAt())
        );
    }
    
    private boolean isNewMatch(Date matchDate) {
        // Consider a match as "new" if it's within the last 7 days
        long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000L;
        return (System.currentTimeMillis() - matchDate.getTime()) < oneWeekInMillis;
    }
}

