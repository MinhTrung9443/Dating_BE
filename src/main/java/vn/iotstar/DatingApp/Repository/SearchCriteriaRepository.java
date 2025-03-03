package vn.iotstar.DatingApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.SearchCriteria;

@Repository
public interface SearchCriteriaRepository extends JpaRepository<SearchCriteria, Long>{

}
