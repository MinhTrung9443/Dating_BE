package vn.iotstar.DatingApp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	
	@Query("SELECT u FROM Users u WHERE YEAR(CURRENT_DATE) - YEAR(u.birthday) BETWEEN :minAge AND :maxAge")
    List<Users> findAllUsersByRangeAge(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
	
	// New method to find users by hobbies
    @Query("SELECT u FROM Users u WHERE u.interests LIKE %:hobby%")
    List<Users> findByHobbyContaining(@Param("hobby") String hobby);
    
}
