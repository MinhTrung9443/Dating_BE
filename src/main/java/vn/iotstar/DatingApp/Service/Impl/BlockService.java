package vn.iotstar.DatingApp.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.MatchList;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.MatchListRepository;
import vn.iotstar.DatingApp.Service.IBlockService;

@Service
public class BlockService implements IBlockService {
	@Autowired
	MatchListRepository matchListRepository;

	@Override
	public <S extends MatchList> S save(S entity) {
		return matchListRepository.save(entity);
	}

	@Override
	public List<MatchList> findAllByUser1(Users user1) {
		return matchListRepository.findAllByUser1(user1);
	}

	public Optional<MatchList> findByUser1AndUser2(Users user1, Users user2) {
		return matchListRepository.findByUser1AndUser2(user1, user2);
	}

	@Override
	public void delete(MatchList entity) {
		matchListRepository.delete(entity);
	}
	
	

}
