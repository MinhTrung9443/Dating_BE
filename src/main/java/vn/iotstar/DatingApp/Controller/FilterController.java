package vn.iotstar.DatingApp.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import vn.iotstar.DatingApp.Dto.ProfileDto;
import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.SearchCriteria;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Model.Response.ApiResponse;
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

	// lỗi lấy luôn thông tin current user
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

	/***
	 	Endpoint: GET /api/v1/users/me/discovery-settings
		Mục đích: Lấy cài đặt đã lưu trong bản ghi SearchCriteria của người dùng.
		Success Response (200 OK): JSON trả về sẽ ánh xạ trực tiếp các trường từ Entity SearchCriteria của người dùng đó.
	 */
	@GetMapping("/me/discovery-settings")
	public ResponseEntity<ApiResponse> getDiscovery() {
		Users user = getCurrentUser();
		logger.info("Fetching discovery settings for user: {}", user.getAccount().getEmail());

		ResponseEntity<ApiResponse> response = searchService.findByUsers(user)
		        .map(criteria -> ResponseEntity.ok(ApiResponse.builder()
		                .message("Existing search criteria")
		                .status(200)
		                .data(criteria)
		                .build()))
		        .orElseGet(() -> {
		            logger.info("No existing criteria found for user {}, returning default.", user.getAccount().getEmail());
		            return ResponseEntity.ok(ApiResponse.builder()
		                    .message("Default search criteria")
		                    .status(200)
		                    .data(searchService.createDefaultCriteria(user))
		                    .build());
		        });

		return response;
	}


	/***
		Endpoint: PUT /api/v1/users/me/discovery-settings
		Mục đích: Cập nhật bản ghi SearchCriteria của người dùng với các giá trị mới.
		Request Body (JSON): Client gửi lên các giá trị muốn cập nhật, tương ứng với các trường trong SearchCriteria.
	 */
	@PutMapping("/me/discovery-settings")
    public ResponseEntity<ApiResponse> updateMyDiscoverySettings(@Valid @RequestBody SearchCriteria settingsDto) {
        Users user = getCurrentUser();
        logger.info("Updating discovery settings for user: {}", user.getAccount().getEmail());

        if (settingsDto.getMinAge() != 0 && settingsDto.getMaxAge() != 0 && settingsDto.getMinAge() > settingsDto.getMaxAge()) {
             throw new RuntimeException("Tuổi tối thiểu không được lớn hơn tuổi tối đa.");
        }

        SearchCriteria updatedCriteria = searchService.createOrUpdateCriteria(user, settingsDto);
        logger.info("Discovery settings updated successfully for user: {}", user.getAccount().getEmail());


        return ResponseEntity.ok(ApiResponse.builder()
        		.status(200)
        		.message("update search")
        		.data(updatedCriteria)
        		.build());
    }

	/***
	 	Endpoint: GET /api/v1/discovery/cards
		Mục đích: Lấy danh sách người dùng khác phù hợp với bộ lọc của người dùng hiện tại.
	 */
	@GetMapping("/me/cards")
	public ResponseEntity<ApiResponse> getMyCards(){
		Users currentUser = getCurrentUser();
//		Account acc = accountService.findAccountByEmail("buitien747@gmail.com").get();
//		Users currentUser = userService.findByAccount(acc).get();
        logger.info("Fetching ALL discovery cards for user: {}", currentUser.getAccount().getEmail());

        List<ProfileDto> cards = searchService.getDiscoveryCards(currentUser);

        logger.info("Returning {} profile cards.", cards.size());
        return ResponseEntity.ok(ApiResponse.builder()
        		.status(200)
        		.message("update search")
        		.data(cards)
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
