package vn.iotstar.DatingApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	// New method to find users by their account
	Optional<Users> findByAccount(Account account);

	// filter user based on search criteria when creating account
	@Query(value = "SELECT * FROM Users u WHERE "
			+ "(:minAge IS NULL OR DATEDIFF(YEAR, u.birthday, GETDATE()) >= :minAge) AND "
			+ "(:maxAge IS NULL OR DATEDIFF(YEAR, u.birthday, GETDATE()) <= :maxAge) AND "
			+ "(:zodiacSign IS NULL OR u.zodiac_sign = :zodiacSign) AND "
			+ "(:personalityType IS NULL OR u.personality_type = :personalityType) AND "
			+ "(:interests IS NULL OR u.interests LIKE '%' + :interests + '%') AND "
			+ "(:latitude IS NULL OR :longitude IS NULL OR "
			+ "(u.latitude IS NOT NULL AND u.longitude IS NOT NULL AND "
			+ "u.latitude BETWEEN :latitude - 1 AND :latitude + 1 AND "
			+ "u.longitude BETWEEN :longitude - 1 AND :longitude + 1 AND " + "6371 * 2 * ASIN(SQRT("
			+ "POWER(SIN(RADIANS(:latitude - u.latitude) / 2), 2) + "
			+ "COS(RADIANS(u.latitude)) * COS(RADIANS(:latitude)) * "
			+ "POWER(SIN(RADIANS(:longitude - u.longitude) / 2), 2))) <= :maxDistance))", nativeQuery = true)
	List<Users> filterUsers(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge,
			@Param("zodiacSign") String zodiacSign, @Param("personalityType") String personalityType,
			@Param("interests") String interests, @Param("latitude") Double latitude,
			@Param("longitude") Double longitude, @Param("maxDistance") Double maxDistance);
	List<Users> findBySexualOrientation(String sexualOrientation);
	@Query("SELECT u FROM Users u WHERE u.interests LIKE %:interests%")
	List<Users> findByInterests(@Param("interests") String interests);
	List<Users> findByZodiacSign(String zodiacSign);
}
