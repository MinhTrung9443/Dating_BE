package vn.iotstar.DatingApp.Controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Service.IMessageService;
import vn.iotstar.DatingApp.Service.IUserService;

@RestController
@RequestMapping("/api/message")
public class MessageController {
	@Autowired
	IMessageService messService;
	@Autowired
	IUserService userService;
	@GetMapping("/getLast")
	public ResponseEntity<?> getLastMessage(Long user1, Long user2)
	{
		Optional<Message> Mess = messService.findLatestMessage(user1, user2);
		if (Mess.isPresent())
		{
			return ResponseEntity.ok(Mess.get());
		}
		
		return ResponseEntity.ok(null);
	}
	
	// them ham lay danh sach tin nhan với so luong co dinh, neu nguoi dung luot ve tin nhan cu hon thi  moi hien dan dan
	@GetMapping("/getMessages")
	public ResponseEntity<?> getMessages(
	        @RequestParam("user1") Long user1,
	        @RequestParam("user2") Long user2,
	        @RequestParam("limit") int limit,
	        @RequestParam("offset") int offset) {

	    List<Message> messages = messService.findMessages(user1, user2, limit, offset);
	    return ResponseEntity.ok(messages);
	}
}
