package vn.iotstar.DatingApp.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.Request.AgeFilterRequest;
import vn.iotstar.DatingApp.Model.Request.DistanceFilterRequest;
import vn.iotstar.DatingApp.Model.Request.HobbiesFilterRequest;
import vn.iotstar.DatingApp.Model.Response.ApiResponse;
import vn.iotstar.DatingApp.Service.FilterService;
import vn.iotstar.DatingApp.Service.FilterServiceImpl;

@RestController
public class FilterController {
	private static final Logger logger = LoggerFactory.getLogger(FilterServiceImpl.class);
	@Autowired
	private FilterService filterService;
	
	// New endpoint for filtering by range of age (min-max)
	@PostMapping("/api/filter/age")
	public ResponseEntity<List<Users>> ageFiltering(@Valid @RequestBody AgeFilterRequest ageFilter) {
		int minAge = ageFilter.getMinAge();
		int maxAge = ageFilter.getMaxAge();
		
		return ResponseEntity.ok(filterService.filterUsersByAge(minAge, maxAge));
	}
	
	// New endpoint for filtering by hobbies
	@PostMapping("/api/filter/hobbies")
    public ResponseEntity<ApiResponse> filterByHobbies(@RequestBody HobbiesFilterRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        
		logger.info("Original authentication name: {}", currentEmail);
		
        List<Users> filteredUsers = filterService.filterUsersByHobbies(request.getHobbies(), currentEmail);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Users filtered by hobbies successfully",
                filteredUsers));
    }
	
	// New endpoint for filtering by hobbies
	@PostMapping("/api/filter/distance")
//    @PreAuthorize("permitAll()") // For testing, update to proper security when ready
    public ResponseEntity<List<Users>> distanceFiltering(@Valid @RequestBody DistanceFilterRequest distanceFilter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        logger.info("Original authentication name: {}", currentEmail);
        // testing case
//        if (authentication == null || currentEmail.equals("anonymousUser")) {
//        	currentEmail = "cochecheee";
//        }
        
        double maxDistance = distanceFilter.getMaxDistance();
        
        return ResponseEntity.ok(filterService.filterUsersByDistance(maxDistance, currentEmail));
    }
}
