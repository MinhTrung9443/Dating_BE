package vn.iotstar.DatingApp.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.MessageModel;
import vn.iotstar.DatingApp.Repository.MatchListRepository;
import vn.iotstar.DatingApp.Repository.MessageRepository;
import vn.iotstar.DatingApp.Service.IMessageService;

@Service
public class MessageService implements IMessageService{
	@Autowired 
	MessageRepository messageRepository;

	@Override
	public List<Message> findByFromUserAndToUser(Long fromUser, Long toUser, int size, int page) {
		Pageable pageable = PageRequest.of(page, size);
		return messageRepository.findByFromUserAndToUser(fromUser, toUser, pageable);
	}

	@Override
	public <S extends Message> S save(S entity) {
		return messageRepository.save(entity);
	}

	@Override
	public Optional<Message> findLatestMessage(Long fromUser, Long receiverId) {
		return messageRepository.findLatestMessage(fromUser, receiverId);
	}
	
	@Override
	public List<MessageModel> findMessages(Users user1, Users user2, int limit, int offset)
	{
		Pageable pageable = PageRequest.of(offset, limit);
		Page<Message> pageResult = messageRepository.findMessagesBetweenUsers(user1, user2, pageable);
		
		// Trả về danh sách Message từ pageResult
		List<Message> list = pageResult.getContent();
		List<MessageModel> listResponse = new ArrayList<>();
		for (Message mess : list)
		{
			MessageModel temp = new MessageModel();
			BeanUtils.copyProperties(mess, temp);
			temp.setFromUser(mess.getFromUser().getId());
			temp.setToUser(mess.getToUser().getId());
			System.out.println(temp);
			listResponse.add(temp);
		}
		return listResponse;
	}

}
