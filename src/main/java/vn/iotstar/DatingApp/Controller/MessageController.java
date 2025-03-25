package vn.iotstar.DatingApp.Controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Service.IMessageService;

@RestController
@RequestMapping("/api/message")
public class MessageController {
	@Autowired
	IMessageService messService;
	@GetMapping("/getLast")
	public ResponseEntity<?> getLastMessage(Long user1, Long user2)
	{
		Optional<Message> Mess = messService.findLatestMessage(user1, user2);
		if (Mess.isPresent())
		{
			return ResponseEntity.ok(Mess);
		}
		
		return ResponseEntity.ok(null);
	}
}
