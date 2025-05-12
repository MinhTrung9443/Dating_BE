package vn.iotstar.DatingApp.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.iotstar.DatingApp.Dto.SearchCriteriaDto;
import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.SearchCriteria;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.Response.ApiResponse;
import vn.iotstar.DatingApp.Service.AccountService;
import vn.iotstar.DatingApp.Service.SearchCriteriaService;
import vn.iotstar.DatingApp.Service.UsersService;

@RestController
@RequestMapping("/api/users/me/settings")
public class SettingsController {
	private static final Logger log = LoggerFactory.getLogger(SettingsController.class);
	@Autowired
	private SearchCriteriaService service;
	@Autowired
	private UsersService userService;
	@Autowired
	private AccountService accountService;

	@GetMapping("")
	public ResponseEntity<ApiResponse> getSettings() {
		Long userId = getCurrentUser().getId();
		SearchCriteria settings = service.getSettings(userId);
		return ResponseEntity.ok(ApiResponse.builder().message("Success").status(200).data(settings).build());
	}

	@PutMapping("")
	public ResponseEntity<ApiResponse> saveSettings(@Valid @RequestBody SearchCriteriaDto incomingSettings) { // Use
																												// @RequestBody
		Long userId = getCurrentUser().getId();
		// --- CRITICAL DEBUG LOG ---
		// Log the object *immediately* after Spring deserializes it.

		log.info("Controller received @RequestBody SearchCriteria: {}", incomingSettings); // Log the whole object
		// Log individual fields as received by controller
		log.info(
				"Controller -> Input fields check: datingPurpose={}, minAge={}, maxAge={}, distance={}, interests={}, zodiacSign={}, personalityType={}",
				incomingSettings.getDatingPurpose(), incomingSettings.getMinAge(), incomingSettings.getMaxAge(),
				incomingSettings.getDistance(), incomingSettings.getInterests(), incomingSettings.getZodiacSign(),
				incomingSettings.getPersonalityType());
		// --- END CRITICAL DEBUG LOG ---

		// Call the service - NOTE: Service method should return the updated entity
		// Assume SearchCriteriaService.saveSettings now returns SearchCriteria
		service.saveSettings(userId, incomingSettings);

		log.info("Settings saved successfully via service for userId: {}. Returning updated data.", userId);

		return ResponseEntity.ok(ApiResponse.builder().message("Success").status(200).data(null) // Return the updated
																									// data from the
																									// service
				.build());
	}

	// --- API Cập nhật Vị trí Người dùng ---
	/**
	 * Endpoint: PUT /api/v1/users/me/location?lat=xx.xxx&long=yy.yyy Mục đích: Cập
	 * nhật vị trí hiện tại của người dùng đang đăng nhập. Request Parameters: lat
	 * (vĩ độ), long (kinh độ). Success Response (200 OK): ApiResponse với thông báo
	 * thành công. Error Responses: 400 (Thiếu tham số), 401 (Chưa xác thực), 404
	 * (Không tìm thấy user), 500 (Lỗi server).
	 */
	@PutMapping("/location") // Đường dẫn tương đối với @RequestMapping của class
	public ResponseEntity<ApiResponse> updateUserLocation(@RequestParam("lat") Double latitude, // ** Lấy từ Request
																								// Parameter **
			@RequestParam("long") Double longitude) { // ** Lấy từ Request Parameter **

		Users currentUser = getCurrentUser(); // Lấy người dùng đã xác thực

		// Kiểm tra tham số đầu vào
		if (latitude == null || longitude == null) {
			log.warn("Update location request missing latitude or longitude.");
			return ResponseEntity.badRequest().body(ApiResponse.builder().status(HttpStatus.BAD_REQUEST.value())
					.message("Vĩ độ (lat) và kinh độ (long) là bắt buộc.").build());
		}

		// Service cần một phương thức như: updateLocation(Long userId, Double latitude,
		// Double longitude)
		userService.updateUserLocation(currentUser.getId(), latitude, longitude);
		log.info("Location updated successfully for user {}: Lat={}, Long={}", currentUser.getAccount().getEmail(),
				latitude, longitude);

		return ResponseEntity.ok(ApiResponse.builder().status(HttpStatus.OK.value())
				.message("Cập nhật vị trí thành công.").data(currentUser).build());
	}

	// Helper method để lấy user hiện tại
	private Users getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal())) {
			throw new RuntimeException("Người dùng chưa được xác thực");
		}
		String userEmail = authentication.getName();
		Account currentAcc = accountService.findAccountByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản cho email: " + userEmail));
		return userService.findByAccount(currentAcc).orElseThrow(
				() -> new RuntimeException("Không tìm thấy thông tin người dùng cho tài khoản: " + userEmail));
	}
}
