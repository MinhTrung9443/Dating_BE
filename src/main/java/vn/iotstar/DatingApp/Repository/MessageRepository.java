package vn.iotstar.DatingApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
	List<Message> findByFromUserAndToUser(Long fromUser, Long toUser,Pageable page);
	@Query("SELECT m FROM Message m " +
	           "WHERE (m.fromUser = :fromUser AND m.toUser = :toUser) " +
	           "OR (m.fromUser = :toUser AND m.toUser = :fromUser) " +
	           "ORDER BY m.sentAt DESC LIMIT 1")
	Optional<Message> findLatestMessage(Long fromUser, Long receiverId);
}
