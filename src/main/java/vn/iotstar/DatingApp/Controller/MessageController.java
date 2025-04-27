package vn.iotstar.DatingApp.Controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Message;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.MessageModel;
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
	/**
	 * API lấy danh sách đã match 
	 * 
	 * 
	 */
	@GetMapping("/getListMatch")
	public ResponseEntity<?> getListMatch(Long user1)
	
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users user = userService.findById(user1).get();
		List<MatchList> listmatch = matchRepo.findAllByUser1OrUser2AndStatus(user, "MATCH");
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
		
		return ResponseEntity.ok(listItem);
	}
	/**
	 * API lấy danh sách tin nhắn giữa người dùng, một lần lấy 20 tin nhắn
	 * 
	 * 
	 */
	
	// them ham lay danh sach tin nhan với so luong co dinh, neu nguoi dung luot ve tin nhan cu hon thi  moi hien dan dan
	@GetMapping("/getMessages")
	public ResponseEntity<?> getMessages(
			
	        @RequestParam("user1") Long user1,
	        @RequestParam("user2") Long user2,
	        @RequestParam("limit") int limit,
	        @RequestParam("offset") int offset) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users u1 = userService.findById(user1).get();
		Users u2 = userService.findById(user2).get();
	    List<MessageModel> messages = messService.findMessages(u1, u2, limit, offset);
	    System.out.println(messages.size());
	    return ResponseEntity.ok(messages);
	}
}
