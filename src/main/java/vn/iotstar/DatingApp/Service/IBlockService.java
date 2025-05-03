package vn.iotstar.DatingApp.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.MatchListModel;

@Service
public interface IBlockService {

	<S extends MatchList> S save(S entity);


	Optional<MatchList> findByUser1AndUser2(Users user1, Users user2);

	void delete(MatchList entity);


	List<MatchList> findAllByUser1AndStatus(Users user1, String status);


	/**
	 * API bỏ block
	 * 
	 * 
	 */
	int unBlockUser(@RequestBody MatchListModel unBlockUser);


	/**
	 * API lấy danh sách người dùng bị blok
	 * 
	 * @return danh sách người bị block
	 */
	List<MatchList> getAllBlockuser(Long userId);


	int blockUser(@RequestBody MatchListModel blockUser);

}
