package vn.iotstar.DatingApp.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Message;

@Service
public interface IMessageService {

	<S extends Message> S save(S entity);

	List<Message> findByFromUserAndToUser(Long fromUser, Long toUser, int size, int page);

	Optional<Message> findLatestMessage(Long fromUser, Long receiverId);

	List<Message> findMessages(Long user1, Long user2, int limit, int offset);

}
