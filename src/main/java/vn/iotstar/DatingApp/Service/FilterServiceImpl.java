package vn.iotstar.DatingApp.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.UsersRepository;

@Service
public class FilterServiceImpl implements FilterService {

	private static final Logger logger = LoggerFactory.getLogger(FilterServiceImpl.class);

	@Autowired
	private UsersRepository usersRepository;

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

	// by apperance
}