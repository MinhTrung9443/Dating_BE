package vn.iotstar.DatingApp.Service;

import java.util.Optional;

import vn.iotstar.DatingApp.Entity.SearchCriteria;
import vn.iotstar.DatingApp.Entity.Users;

public interface SearchCriteriaService {

	Optional<SearchCriteria> findByUsers(Users users);

}
