package vn.iotstar.DatingApp.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return ResponseEntity.ok(ApiResponse.builder()
        		.message("Success")
        		.status(200)
        		.data(settings)
        		.build());
    }

	@PutMapping("")
    public ResponseEntity<ApiResponse> saveSettings(@Valid @RequestBody SearchCriteriaDto incomingSettings) { // Use @RequestBody
		Long userId = getCurrentUser().getId();
        // --- CRITICAL DEBUG LOG ---
        // Log the object *immediately* after Spring deserializes it.

        log.info("Controller received @RequestBody SearchCriteria: {}", incomingSettings); // Log the whole object
        // Log individual fields as received by controller
        log.info("Controller -> Input fields check: datingPurpose={}, minAge={}, maxAge={}, distance={}, interests={}, zodiacSign={}, personalityType={}",
                incomingSettings.getDatingPurpose(),
                incomingSettings.getMinAge(),
                incomingSettings.getMaxAge(),
                incomingSettings.getDistance(),
                incomingSettings.getInterests(),
                incomingSettings.getZodiacSign(),
                incomingSettings.getPersonalityType());
        // --- END CRITICAL DEBUG LOG ---

        // Call the service - NOTE: Service method should return the updated entity
        // Assume SearchCriteriaService.saveSettings now returns SearchCriteria
        service.saveSettings(userId, incomingSettings);

        log.info("Settings saved successfully via service for userId: {}. Returning updated data.", userId);

        return ResponseEntity.ok(ApiResponse.builder()
                .message("Success")
                .status(200)
                .data(null) // Return the updated data from the service
                .build());
    }

 // Helper method để lấy user hiện tại
    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa được xác thực");
        }
        String userEmail = authentication.getName();
        Account currentAcc = accountService.findAccountByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản cho email: " + userEmail));
        return userService.findByAccount(currentAcc)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng cho tài khoản: " + userEmail));
    }
}
