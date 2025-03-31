package vn.iotstar.DatingApp.Service;

import java.util.List;

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

	@Override
	public List<Users> filterUsers(Integer minAge, Integer maxAge, String zodiacSign, String personalityType,
			String interests, Double latitude, Double longitude, Double maxDistance) {
		logger.info(
				"Filtering users with parameters: "
						+ "minAge={}, maxAge={}, zodiacSign={}, personalityType={}, interests={}, "
						+ "latitude={}, longitude={}, maxDistance={}",
				minAge, maxAge, zodiacSign, personalityType, interests, latitude, longitude, maxDistance);

		List<Users> users = usersRepository.filterUsers(minAge, maxAge, zodiacSign, personalityType, interests,
				latitude, longitude, maxDistance);

		logger.info("Query returned {} users", users.size());
		return users;
	}

}