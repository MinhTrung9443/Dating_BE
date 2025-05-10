package vn.iotstar.DatingApp.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.MessageModel;
import vn.iotstar.DatingApp.Model.Response.MessageItem;
import vn.iotstar.DatingApp.Repository.MatchListRepository;
import vn.iotstar.DatingApp.Repository.MessageRepository;
import vn.iotstar.DatingApp.Repository.UsersRepository;
import vn.iotstar.DatingApp.Service.IMessageService;

@Service
public class MessageService implements IMessageService{
	@Autowired
	MessageRepository messageRepository;
	@Autowired
	UsersRepository usersRepository;
	@Autowired
	MatchListRepository matchListRepository;
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
	
	public List<MessageItem> getListMessageItem(Long user1){
		try {
			Users user = usersRepository.findById(user1).get();
			List<MatchList> listmatch = matchListRepository.findAllByUser1OrUser2AndStatus(user, "MATCHED");
			List<MessageItem> listItem = new ArrayList<>();
			System.out.println("id user1 truyen toi" + user1 + "listmath : " + listmatch.get(0).getUser1().getId());
			for (MatchList m : listmatch) {
				MessageItem messItem = new MessageItem();
				if (m.getUser1().getId() == user1)
				{
					messItem.setSenderId(m.getUser2().getId().intValue());
					messItem.setName(m.getUser2().getName());
					messItem.setPicture(m.getUser2().getImages().get(0).getImage());
				}
				else
				{
					messItem.setSenderId(m.getUser1().getId().intValue());
					messItem.setName(m.getUser1().getName());
					messItem.setPicture(m.getUser1().getImages().get(0).getImage());
				}
				messItem.setCount(m.getMessages().size());
				if (messItem.getCount() != 0)
				{
					messItem.setContent(m.getMessages().get(m.getMessages().size()-1).getMessageContent());
				}
				listItem.add(messItem);
			}
			
			return listItem;
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
	}
}
