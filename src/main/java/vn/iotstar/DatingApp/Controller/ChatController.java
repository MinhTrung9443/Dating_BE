package vn.iotstar.DatingApp.Controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Repository.MessageRepository;
import vn.iotstar.DatingApp.Service.IMessageService;



@Controller
public class ChatController {
	@Autowired
	private IMessageService messService;
	
	@Autowired
	private SimpMessagingTemplate messTemplate;
	
	@MessageMapping("/sendPrivateMessage")
	public void sendPrivateMessage(@Payload Message message)
	{
		message.setSentAt(new Date());
		messService.save(message);

		if (messTemplate == null) {
		    System.out.println("messTemplate is null!");
		} else {
			String privateChannel = getPrivateChannel(message.getToUser());
		    System.out.println("Gửi tin nhắn tới: " + privateChannel);
		    messTemplate.convertAndSend(privateChannel, message);

		}
	}
	
	private String getPrivateChannel(Long userID)
	{
		return "/topic/private/"+ userID;
	}
}
