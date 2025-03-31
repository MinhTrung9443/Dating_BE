package vn.iotstar.DatingApp.Service;

import java.util.List;

import vn.iotstar.DatingApp.Entity.Users;

public interface FilterService {
	List<Users> filterUsers(Integer minAge, Integer maxAge, String zodiacSign, 
            String personalityType, String interests, 
            Double latitude, Double longitude, Double maxDistance);
}
