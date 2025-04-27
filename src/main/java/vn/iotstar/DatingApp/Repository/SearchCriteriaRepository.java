package vn.iotstar.DatingApp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.SearchCriteria;
import vn.iotstar.DatingApp.Entity.Users;

@Repository
public interface SearchCriteriaRepository extends JpaRepository<SearchCriteria, Long>{
	Optional<SearchCriteria> findByUsers(Users users);
}
