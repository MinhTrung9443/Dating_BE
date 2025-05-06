package vn.iotstar.DatingApp.Service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.MatchListModel;
import vn.iotstar.DatingApp.Repository.MatchListRepository;
import vn.iotstar.DatingApp.Service.IBlockService;
import vn.iotstar.DatingApp.Service.IUserService;

@Service
public class BlockService implements IBlockService {
	@Autowired
	MatchListRepository matchListRepository;
	
	@Autowired
	IUserService userService;

	@Override
	public <S extends MatchList> S save(S entity) {
		return matchListRepository.save(entity);
	}



	@Override
	public List<MatchList> findAllByUser1AndStatus(Users user1, String status) {
		return matchListRepository.findAllByUser1OrUser2AndStatus(user1, status);
	}



	@Override
	public Optional<MatchList> findByUser1AndUser2(Users user1, Users user2) {
		return matchListRepository.findByUser1AndUser2(user1, user2);
	}

	@Override
	public void delete(MatchList entity) {
		matchListRepository.delete(entity);
	}
	
	@Override
	public int blockUser(@RequestBody MatchListModel blockUser) {
		try {
			Users user1 = userService.findById(blockUser.getUser1()).get();
			Users user2 = userService.findById(blockUser.getUser2()).get();
			Optional<MatchList> matchList = findByUser1AndUser2(user1, user2);
			if (!matchList.isPresent())
			{
				MatchList block = new MatchList();
				BeanUtils.copyProperties(blockUser, block);
				block.setUser1(user1);
				block.setUser2(user2);
				block.setCreatedAt(new Date());
				save(block);
			}
			// da match roi goi ham update
			else {
				MatchList block = matchList.get();
				block.setStatus("BLOCK");
				block.setCreatedAt(new Date());
				save(block);
			}
			
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
	/**
	 * API lấy danh sách người dùng bị blok
	 * 
	 * @return danh sách người bị block
	 */
	@Override
	public List<MatchList> getAllBlockuser(Long userId)
	{
		try {
			Users user = userService.findById(userId).get();
			List<MatchList> listMatchBlockUser = findAllByUser1AndStatus(user,"BLOCK");
			
			return listMatchBlockUser;
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
	}
	/**
	 * API bỏ block
	 * 
	 * 
	 */
	@Override
	public int unBlockUser(@RequestBody MatchListModel unBlockUser){
		
		try {
			Users user1 = userService.findById(unBlockUser.getUser1()).get();
			Users user2 = userService.findById(unBlockUser.getUser2()).get();
			Optional<MatchList> matchList = findByUser1AndUser2(user1, user2);
			if (matchList.isPresent())
			{
				delete(matchList.get());
				return 1;
			}
		} catch (Exception e) {
			return 0;
		}
		
		return 0;
	}

}
