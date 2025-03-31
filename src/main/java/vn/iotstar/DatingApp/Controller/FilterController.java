package vn.iotstar.DatingApp.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Service.FilterService;
import vn.iotstar.DatingApp.Service.FilterServiceImpl;

@RestController
@RequestMapping("/api/users")
public class FilterController {
	private static final Logger logger = LoggerFactory.getLogger(FilterServiceImpl.class);
	@Autowired
	private FilterService filterService;


	@GetMapping("/filter")
    public ResponseEntity<List<Users>> filterUsers(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String zodiacSign,
            @RequestParam(required = false) String personalityType,
            @RequestParam(required = false) String interests,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double maxDistance) {
        
        List<Users> users = filterService.filterUsers(minAge, maxAge, zodiacSign, 
                                                      personalityType, interests, 
                                                      latitude, longitude, maxDistance);
        logger.info("Found {} users matching the criteria", users.size());
        return ResponseEntity.ok(users);
    }
}
