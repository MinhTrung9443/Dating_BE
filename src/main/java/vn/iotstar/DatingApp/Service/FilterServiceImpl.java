package vn.iotstar.DatingApp.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.UsersRepository;

@Service
public class FilterServiceImpl implements FilterService{
	
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
	public List<Users> filterUsersByAge(int minAge, int maxAge)
	{	
		return usersRepository.findAllUsersByRangeAge(minAge, maxAge);
	}
	
	
	// by hobbies
	
	// by apperance
}
