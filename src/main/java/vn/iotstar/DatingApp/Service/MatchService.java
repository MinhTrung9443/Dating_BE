package vn.iotstar.DatingApp.Service;

import java.util.List;

import vn.iotstar.DatingApp.Dto.MatchFeedDto;
import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;

public interface MatchService {

	/**
	 * Xử lý khi người dùng hiện tại "like" một người dùng khác
	 *
	 * @param targetUserId ID của người dùng được like
	 * @return true nếu match thành công, false nếu chỉ là like đơn phương
	 */
	boolean likeUser(Long targetUserId);

	/**
	 * Xử lý khi người dùng hiện tại "dislike" một người dùng khác
	 *
	 * @param targetUserId ID của người dùng được dislike
	 */
	void dislikeUser(Long targetUserId);

	/**
	 * Gửi tin nhắn đến người dùng khác
	 *
	 * @param targetUserId ID của người dùng nhận tin nhắn
	 * @param content Nội dung tin nhắn
	 * @return Tin nhắn đã gửi
	 */
	Message sendMessage(Long targetUserId, String content);

	/**
	 * Thu hồi lại hành động dislike (chức năng rewind)
	 *
	 * @param targetUserId ID của người dùng đã bị dislike trước đó
	 * @return true nếu thu hồi thành công, false nếu không tìm thấy lịch sử dislike
	 */
	boolean rewindDislike(Long targetUserId);

	/**
	 * Xem thông tin profile của người dùng khác
	 *
	 * @param targetUserId ID của người dùng cần xem profile
	 * @return Thông tin người dùng
	 */
	Users viewProfile(Long targetUserId);

	/**
	 * Lấy danh sách người dùng đề xuất ghép đôi (chưa like/dislike)
	 *
	 * @return Danh sách người dùng đề xuất
	 */
	List<Users> getSuggestedMatches();

	/**
	 * Lấy danh sách match đã thành công
	 *
	 * @return Danh sách match
	 */
	List<MatchList> getMatches();

	/**
	 * Lấy danh sách người đã like mình mà chưa match
	 * (Tương đương với tính năng Gold của Tinder - xem ai đã like mình)
	 *
	 * @return Danh sách người dùng đã like mình
	 */
	List<Users> getMyLikers();

	/**
	 * Lấy danh sách lượt like gần đây của người dùng hiện tại
	 *
	 * @param limit Số lượng lượt like muốn lấy
	 * @return Danh sách lượt like gần đây
	 */
	List<MatchList> getRecentLikes(int limit);

	/**
	 * Lấy danh sách người dùng hiện tại đã dislike
	 *
	 * @return Danh sách người đã dislike
	 */
	List<Users> getDislikedUsers();

	/**
	 * Đếm số lượt like nhận được chưa match
	 *
	 * @return Số lượt like nhận được
	 */
	int countPendingLikes();

	List<MatchFeedDto> getMatchesForFeed(Users currentUser);
}
