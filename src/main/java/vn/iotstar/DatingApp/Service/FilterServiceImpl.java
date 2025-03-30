package vn.iotstar.DatingApp.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.AccountRepository;
import vn.iotstar.DatingApp.Repository.UsersRepository;

@Service
public class FilterServiceImpl implements FilterService {

	private static final Logger logger = LoggerFactory.getLogger(FilterServiceImpl.class);

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private AccountRepository accountRepository;

	// by distance (latitude, longtitude)
	// từ address + quantity od distance (using map location)
	// suy ra, destination address
	// find user having address = destination address
	// https://jxausea.medium.com/spring-boot-integrates-geodesy-to-implement-distance-calculation-92680c008417

	// by age
	// find user have the same age like filter's age
	@Override
	public List<Users> filterUsersByAge(int minAge, int maxAge) {
		logger.info("Filtering users by age range: {} to {}", minAge, maxAge);

		List<Users> filteredUsers = usersRepository.findAllUsersByRangeAge(minAge, maxAge);

		logger.info("Found {} users matching age range criteria", filteredUsers.size());
		logger.debug("Filtered users by age: {}", filteredUsers);

		return filteredUsers;
	}

	// by hobbies
	@Override
	public List<Users> filterUsersByHobbies(List<String> hobbies, String currentEmail) {
		logger.info("Filtering users by hobbies: {} for user email: {}", hobbies, currentEmail);
		try {
			Set<Users> resultSet = new HashSet<>();
			// Vòng lặp qua từng hobby trong danh sách
			for (String hobby : hobbies) {
				logger.info("Searching for users with hobby: {}", hobby);
				// Gọi repository method để tìm users cho mỗi hobby
				List<Users> usersWithHobby = usersRepository.findByHobbyContaining(hobby);
				logger.info("Found {} users with hobby '{}'", usersWithHobby.size(), hobby);
				// Thêm kết quả vào Set
				resultSet.addAll(usersWithHobby);
			}
			logger.info("Total unique users found across all hobbies: {}", resultSet.size());
			// Loại bỏ người dùng hiện tại khỏi kết quả
			List<Users> filteredUsers = resultSet.stream()
					.filter(user -> user.getAccount() != null && !user.getAccount().getEmail().equals(currentEmail))
					.collect(Collectors.toList());
			logger.info("After excluding current user, returning {} users matching hobbies criteria",
					filteredUsers.size());
			logger.debug("Filtered users by hobbies: {}", filteredUsers);
			return filteredUsers;
		} catch (Exception e) {
			logger.error("Error while filtering users by hobbies: {}", e.getMessage(), e);
			return List.of(); // Trả về danh sách rỗng khi có lỗi
		}
	}

	// by distance
	@Override
	public List<Users> filterUsersByDistance(double maxDistance, String currentEmail) {
	    logger.info("Starting filterUsersByDistance with maxDistance={} for user email={}", maxDistance, currentEmail);

	    Optional<Account> currentAccount = accountRepository.findAccountByEmail(currentEmail);
	    if (currentAccount.isEmpty()) {
	        logger.warn("Account not found for email: {}", currentEmail);
	        return new ArrayList<>();
	    }
	    logger.debug("Found account with id={} for email={}", currentAccount.get().getId(), currentEmail);
	    
	    Optional<Users> currentUser = usersRepository.findByAccount(currentAccount.get());
	    if (currentUser.isEmpty()) {
	        logger.warn("User profile not found for account id={}", currentAccount.get().getId());
	        return new ArrayList<>();
	    }
	    
	    if (currentUser.get().getLatitude() == null || currentUser.get().getLongitude() == null) {
	        logger.warn("User with id={} has no location data", currentUser.get().getId());
	        return new ArrayList<>();
	    }
	    
	    double userLat = currentUser.get().getLatitude();
	    double userLng = currentUser.get().getLongitude();
	    Long userId = currentUser.get().getId();
	    
	    logger.info("Current user (id={}) location: lat={}, lng={}", userId, userLat, userLng);
	    
	    List<Users> allUsers = usersRepository.findAll();
	    logger.debug("Found {} total users in database", allUsers.size());

	    List<Users> filteredUsers = allUsers.stream()
	            .filter(user -> user.getId() != null && !user.getId().equals(userId)) // Exclude current user
	            .filter(user -> user.getLatitude() != null && user.getLongitude() != null)
	            .filter(user -> {
	                double distance = calculateDistance(userLat, userLng, user.getLatitude(), user.getLongitude());
	                logger.debug("User id={} is {} km away from current user", user.getId(), distance);
	                return distance <= maxDistance;
	            })
	            .collect(Collectors.toList());

	    logger.info("Filter by distance completed. Found {} users within {}km", filteredUsers.size(), maxDistance);
	    return filteredUsers;
	}
    
	// Calculate distance of 2 points by Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; 
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; 
    }
}