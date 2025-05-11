package vn.iotstar.DatingApp.Controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.MessageModel;
import vn.iotstar.DatingApp.Repository.MatchListRepository;
import vn.iotstar.DatingApp.Repository.UsersRepository;
import vn.iotstar.DatingApp.Service.IMessageService;



@Controller
public class ChatController {
	@Autowired
	private IMessageService messService;
	@Autowired
	UsersRepository userRepo;
	@Autowired
	MatchListRepository matchlistRepo;
	@Autowired
	private SimpMessagingTemplate messTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * gửi tin nhắn
	 *
	 *
	 */
	@MessageMapping("/sendPrivateMessage")
	public void sendPrivateMessage(@Payload MessageModel receiveMessage)
	{
		Message message = new Message();
		BeanUtils.copyProperties(receiveMessage, message);
		message.setSentAt(new Date());

		Users user1 = userRepo.findById(receiveMessage.getFromUser()).get();
		Users user2 = userRepo.findById(receiveMessage.getToUser()).get();
		message.setFromUser(user1);
		message.setToUser(user2);
		Optional<MatchList> matchList1 = matchlistRepo.findByUser1AndUser2(user1, user2);
		Optional<MatchList> matchList2 = matchlistRepo.findByUser1AndUser2(user2, user1);
		if (matchList1.isPresent())
			message.setMatchlist(matchList1.get());
		else 
			message.setMatchlist(matchList2.get());

		messService.save(message);

		if (messTemplate == null) {
		    System.out.println("messTemplate is null!");
		} else {
			String privateChannel = getPrivateChannel(message.getToUser().getId());
		    System.out.println("Gửi tin nhắn tới: " + privateChannel);
		    messTemplate.convertAndSend(privateChannel, receiveMessage);
		 // Gửi notify frame tới người nhận
            //sendNotification(message.getToUser().getId(), "Bạn có tin nhắn mới từ " + user1.getName(), "NOTIFY");
		}
	}

	/**
	 * Hàm tạo frame thông báo (chức năng chưa hoàn thiện)
	 *
	 *
	 */
	private void sendNotification(Long userId, String notifyContent, String notifyType) {
	    try {
	        // Tạo JSON cho notify
	        Map<String, String> notifyData = Map.of(
	            "notifyContent", notifyContent,
	            "notifyType", notifyType
	        );
	        // Sử dụng instance objectMapper
	        String jsonData = objectMapper.writeValueAsString(notifyData);

	        // Tạo notify frame theo định dạng NOTIFY\n\n{json}\0
	        String notifyFrame = "NOTIFY\n\n" + jsonData + "\0";

	        // Gửi notify frame tới kênh riêng của người dùng
	        String notifyChannel = getPrivateChannel(userId);
	        messTemplate.convertAndSend(notifyChannel, notifyFrame);
	        System.out.println("Gửi notify frame tới: " + notifyChannel + ", nội dung: " + notifyFrame);
	    } catch (Exception e) {
	        System.err.println("Lỗi gửi notify frame: " + e.getMessage());
	    }
	}
	/**
	 * tạo private channel : kênh cá nhân của người dùng
	 *
	 *
	 */
	private String getPrivateChannel(Long userID)
	{
		return "/topic/private/"+ userID;
	}
}
