package vn.iotstar.DatingApp.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;

@Service
public interface IBlockService {

	<S extends MatchList> S save(S entity);


	Optional<MatchList> findByUser1AndUser2(Users user1, Users user2);

	void delete(MatchList entity);


	List<MatchList> findAllByUser1AndStatus(Users user1, String status);

}
