package vn.iotstar.DatingApp.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.SearchCriteria;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.SearchCriteriaRepository;

@Service
public class SearchCriteriaServiceImpl implements SearchCriteriaService {
	@Autowired
	private SearchCriteriaRepository searchRepo;

	@Override
	public Optional<SearchCriteria> findByUsers(Users users) {
		return searchRepo.findByUsers(users);
	}
	
}
