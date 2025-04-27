package vn.iotstar.DatingApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;

@Repository
public interface MatchListRepository extends JpaRepository<MatchList, Long> {
//	List<MatchList> findAllByUser1AndStatus(Users user1, String status);
	 @Query("SELECT m FROM MatchList m WHERE (m.user1 = :user OR m.user2 = :user) AND m.status = :status")
	List<MatchList> findAllByUser1OrUser2AndStatus(Users user, String status);
	Optional<MatchList> findByUser1AndUser2(Users user1,Users user2);
}
