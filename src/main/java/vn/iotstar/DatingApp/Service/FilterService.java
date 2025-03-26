package vn.iotstar.DatingApp.Service;

import java.util.List;

import vn.iotstar.DatingApp.Entity.Users;

public interface FilterService {
	List<Users> filterUsersByAge(int minAge, int maxAge);

	List<Users> filterUsersByHobbies(List<String> hobbies, String currentEmail);
}
