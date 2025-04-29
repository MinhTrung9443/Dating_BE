package vn.iotstar.DatingApp.Repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByMatchlist(MatchList matchlist);
    
    List<Message> findByFromUserAndToUser(Users fromUser, Users toUser);
    
    List<Message> findByFromUserOrToUserOrderBySentAtDesc(Users fromUser, Users toUser);
    
    List<Message> findByToUserAndIsReadFalse(Users toUser);
    
    int countByToUserAndIsReadFalse(Users toUser);	List<Message> findByFromUserAndToUser(Long fromUser, Long toUser,Pageable page);
	@Query("SELECT m FROM Message m " +
	           "WHERE (m.fromUser = :fromUser AND m.toUser = :toUser) " +
	           "OR (m.fromUser = :toUser AND m.toUser = :fromUser) " +
	           "ORDER BY m.sentAt DESC LIMIT 1")
	Optional<Message> findLatestMessage(Long fromUser, Long receiverId);
	
    @Query("SELECT m FROM Message m " +
           "WHERE (m.fromUser = :user1 AND m.toUser = :user2) " +
           "OR (m.fromUser = :user2 AND m.toUser = :user1) " +
           "ORDER BY m.sentAt DESC")
    Page<Message> findMessagesBetweenUsers(
            @Param("user1") Users user1,
            @Param("user2") Users user2,
            Pageable pageable);
}
