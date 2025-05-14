package vn.iotstar.DatingApp.Controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Dto.ProfileDto;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Service.IUserService;

@RestController
@RequestMapping("/api/searchCard")
public class SearchCardController {
	@Autowired
	IUserService userService;
	@PostMapping("/find/SexualOrientation")
	public ResponseEntity<?> findBySexualOrientation(String SexualOrientation)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }
		List<Users> listUser = new ArrayList<>();
		listUser = userService.findBySexualOrientation(SexualOrientation);
		return ResponseEntity.ok(listUser);

	}

	@PostMapping("/find/interests")
	public ResponseEntity<?> findByInterests(String interests)
	{
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
	            throw new RuntimeException("Người dùng chưa được xác thực");
	        }
			List<Users> listUser = new ArrayList<>();
			listUser = userService.findByInterests(interests);
			List<ProfileDto> listProfile = listUser.stream()
					.map(user -> mapUserToProfileCardDto(user))
					.collect(Collectors.toList());
			return ResponseEntity.ok(listProfile);
		} catch (Exception e) {
			return ResponseEntity.ok(List.of());
		}
	}
	
	private ProfileDto mapUserToProfileCardDto(Users targetUser) {

		String location = targetUser.getAddress();
		if (location == null || location.trim().isEmpty()) {
			location = "Không xác định";
		}

		String displayName = (targetUser.getName() != null ? targetUser.getName() : "");

		return ProfileDto.builder()
				.id(targetUser.getId())
				.name(displayName)
				.imageUrl(targetUser.getImages().get(0).getImage())
				.location(location)
				.age(calAge(targetUser.getBirthday()))
				.build();
	}
	
	private int calAge(LocalDate bd)
	{
		if (bd == null) {
	        return 18; // Hoặc trả về giá trị mặc định, tùy yêu cầu
	    }

	    LocalDate birthDate = bd;
	    LocalDate currentDate = LocalDate.now();

	    // Tính khoảng cách giữa hai ngày
	    Period period = Period.between(birthDate, currentDate);

	    return period.getYears();
	}

	@PostMapping("/find/zodiacSign")
	public ResponseEntity<?> findByZodiacSign(String zodiacSign)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }
		List<Users> listUser = new ArrayList<>();
		listUser = userService.findByZodiacSign(zodiacSign);
		return ResponseEntity.ok(listUser);

	}

	// search xong con tinh toan lai sap xep theo vi tri
}
