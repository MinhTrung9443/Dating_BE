package vn.iotstar.DatingApp.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.MessageModel;

@Service
public interface IMessageService {

	<S extends Message> S save(S entity);

	List<Message> findByFromUserAndToUser(Long fromUser, Long toUser, int size, int page);

	Optional<Message> findLatestMessage(Long fromUser, Long receiverId);

	List<MessageModel> findMessages(Users user1, Users user2, int limit, int offset);

}
