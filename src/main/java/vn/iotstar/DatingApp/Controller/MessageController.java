package vn.iotstar.DatingApp.Controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.Response.MessageItem;
import vn.iotstar.DatingApp.Repository.MatchListRepository;
import vn.iotstar.DatingApp.Service.IMessageService;
import vn.iotstar.DatingApp.Service.IUserService;

@RestController
@RequestMapping("/api/message")
public class MessageController {
	@Autowired
	IMessageService messService;
	@Autowired
	IUserService userService;
	@Autowired
	MatchListRepository matchRepo;
	@GetMapping("/getListMatch")
	public ResponseEntity<?> getListMatch(Long user1)
	{
		Users user = userService.findById(user1).get();
		List<MatchList> listmatch = matchRepo.findAllByUser1AndStatus(user, "MATCH");
		List<MessageItem> listItem = new ArrayList<>();
		for (MatchList m : listmatch) {
			MessageItem messItem = new MessageItem();
			messItem.setSenderId(m.getUser2().getId().intValue());
			messItem.setName(m.getUser2().getName());
			messItem.setPicture(m.getUser2().getImages().get(0).getImage());
			messItem.setCount(m.getMessages().size());
			if (messItem.getCount() != 0)
			{
				messItem.setContent(m.getMessages().get(m.getMessages().size()).getMessageContent());
			}
			listItem.add(messItem);
		}
		
		return ResponseEntity.ok(listItem);
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
