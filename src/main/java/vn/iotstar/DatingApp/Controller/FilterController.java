package vn.iotstar.DatingApp.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.SearchCriteria;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Service.AccountService;
import vn.iotstar.DatingApp.Service.FilterService;
import vn.iotstar.DatingApp.Service.FilterServiceImpl;
import vn.iotstar.DatingApp.Service.SearchCriteriaService;
import vn.iotstar.DatingApp.Service.UsersService;

@RestController
@RequestMapping("/api/users")
public class FilterController {
	private static final Logger logger = LoggerFactory.getLogger(FilterServiceImpl.class);
	@Autowired
	private FilterService filterService;
	@Autowired
	private UsersService userService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private SearchCriteriaService searchService;

	@GetMapping("/filter")
    public ResponseEntity<List<Users>> filterUsers() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		Account cuurrentAcc = accountService.findAccountByEmail(userEmail).get();
		
		Users currentUser = userService.findByAccount(cuurrentAcc).get();
		SearchCriteria filterRequest = searchService.findByUsers(currentUser).get();
        
        List<Users> users = filterService.filterUsers(filterRequest.getMinAge(), 
                filterRequest.getMaxAge(), 
                filterRequest.getZodiacSign(),
                filterRequest.getPersonalityType(), 
                filterRequest.getInterests(),
                currentUser.getLatitude(), 
                currentUser.getLongitude(), 
                filterRequest.getDistance());
        logger.info("Found {} users matching the criteria", users.size());
        return ResponseEntity.ok(users);
    }
}
