package vn.iotstar.DatingApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;

@Repository
public interface MatchListRepository extends JpaRepository<MatchList, Long> {
//	List<MatchList> findAllByUser1AndStatus(Users user1, String status);
	 @Query("SELECT m FROM MatchList m WHERE (m.user1 = :user OR m.user2 = :user) AND m.status = :status")
	List<MatchList> findAllByUser1OrUser2AndStatus(Users user, String status);

	// STATUS: Pending, Matched, Rejected, Block
	/**
     * Find all users who liked the current user (users who initiated a match with the current user)
     * Assuming status = "LIKE" indicates a like
     *
     * @param userId ID of the current user
     * @return List of users who liked the current user
     */
    @Query("SELECT m.user1 FROM MatchList m WHERE m.user2.id = :userId AND m.status = 'PENDING'")
    List<Users> findUsersWhoLikedMe(@Param("userId") Long userId);

    /**
     * Find all users whom the current user has liked (matches initiated by the current user)
     * Assuming status = "LIKE" indicates a like
     *
     * @param userId ID of the current user
     * @return List of users liked by the current user
     */
    @Query("SELECT m.user2 FROM MatchList m WHERE m.user1.id = :userId AND m.status = 'LIKE'")
    List<Users> findUsersILiked(@Param("userId") Long userId);

    // find matchlist between user1 and user2
    Optional<MatchList> findByUser1AndUser2(Users user1, Users user2);

    Optional<MatchList> findByUser1AndUser2AndStatus(Users user1, Users user2, String status);

    List<MatchList> findByUser1(Users user1);

    List<MatchList> findByUser2(Users user2);

    List<MatchList> findByUser1AndStatus(Users user1, String status);

    List<MatchList> findByUser2AndStatus(Users user2, String status);

    List<MatchList> findByUser1OrUser2(Users user1, Users user2);

    List<MatchList> findByUser1AndStatusOrderByUpdatedAtDesc(Users user1, String status);

    List<MatchList> findByUser2AndStatusOrderByUpdatedAtDesc(Users user2, String status);

    int countByUser2AndStatus(Users user2, String status);

    int countByUser1AndStatus(Users user1, String status);
}
