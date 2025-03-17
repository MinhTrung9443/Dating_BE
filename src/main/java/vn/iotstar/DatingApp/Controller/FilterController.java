package vn.iotstar.DatingApp.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.Request.AgeFilterRequest;
import vn.iotstar.DatingApp.Service.FilterService;

@RestController
public class FilterController {
	@Autowired
	private FilterService filterService;
	
	//Done but not elimanating current USER
	@PostMapping("/api/filter/age")
	@PreAuthorize("permitAll()") //for testing
	public ResponseEntity<List<Users>> ageFiltering(@Valid @RequestBody AgeFilterRequest ageFilter) {
		int minAge = ageFilter.getMinAge();
		int maxAge = ageFilter.getMaxAge();
		
		return ResponseEntity.ok(filterService.filterUsersByAge(minAge, maxAge));
	}
}
