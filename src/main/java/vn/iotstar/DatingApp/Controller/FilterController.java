package vn.iotstar.DatingApp.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.Request.AgeFilterRequest;
import vn.iotstar.DatingApp.Model.Request.HobbiesFilterRequest;
import vn.iotstar.DatingApp.Model.Response.ApiResponse;
import vn.iotstar.DatingApp.Service.FilterService;

@RestController
public class FilterController {
	@Autowired
	private FilterService filterService;
	
	// New endpoint for filtering by range of age (min-max)
	@PostMapping("/api/filter/age")
	@PreAuthorize("permitAll()") //for testing
	public ResponseEntity<List<Users>> ageFiltering(@Valid @RequestBody AgeFilterRequest ageFilter) {
		int minAge = ageFilter.getMinAge();
		int maxAge = ageFilter.getMaxAge();
		
		return ResponseEntity.ok(filterService.filterUsersByAge(minAge, maxAge));
	}
	
	// New endpoint for filtering by hobbies
	@PostMapping("/api/filter/hobbies")
    public ResponseEntity<ApiResponse> filterByHobbies(@RequestBody HobbiesFilterRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
        List<Users> filteredUsers = filterService.filterUsersByHobbies(request.getHobbies(), userEmail);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                "Users filtered by hobbies successfully",
                filteredUsers));
    }
}
