package vn.iotstar.DatingApp.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Message;
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
	

}
