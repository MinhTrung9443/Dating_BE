package vn.iotstar.DatingApp.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.iotstar.DatingApp.Dto.ProfileDto;
import vn.iotstar.DatingApp.Dto.SearchCriteriaDto;
import vn.iotstar.DatingApp.Entity.SearchCriteria;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.MatchListRepository;
import vn.iotstar.DatingApp.Repository.SearchCriteriaRepository;
import vn.iotstar.DatingApp.Repository.UsersRepository;

@Service
public class SearchCriteriaServiceImpl implements SearchCriteriaService {
	private static final Logger logger = LoggerFactory.getLogger(SearchCriteriaServiceImpl.class);
	@Autowired
	private SearchCriteriaRepository searchRepo;
	@Autowired
	private MatchListRepository matchRepo;
	@Autowired
	private UsersRepository usersRepo;

	@Override
	public Optional<SearchCriteria> findByUsers(Users users) {
		return searchRepo.findByUsers(users);
	}

	@Override
	@Transactional
	public SearchCriteria createDefaultCriteria(Users user) {
		logger.info("Creating default search criteria for user: {}", user.getAccount().getEmail());

		// Kiểm tra xem user đã có criteria chưa (phòng trường hợp gọi nhầm)
		Optional<SearchCriteria> existingCriteria = findByUsers(user);
		if (existingCriteria.isPresent()) {
			logger.warn("User {} already has search criteria. Returning existing one.", user.getAccount().getEmail());
			return existingCriteria.get();
		}

		SearchCriteria defaultCriteria = new SearchCriteria();
		defaultCriteria.setUsers(user); // Liên kết với user

		// --- Đặt các giá trị mặc định ---
		defaultCriteria.setMinAge(18); // Ví dụ: tuổi từ 18
		defaultCriteria.setMaxAge(55); // Ví dụ: đến 55
		defaultCriteria.setDistance(50.0); // Ví dụ: khoảng cách 50km
		defaultCriteria.setDatingPurpose(null); // Hoặc một giá trị mặc định "new_friends"
		defaultCriteria.setInterests(null); // Hoặc chuỗi rỗng ""
		defaultCriteria.setZodiacSign(null);
		defaultCriteria.setPersonalityType(null);

		// ** Xử lý interestedInGenders (QUAN TRỌNG) **
		// Bạn cần thêm trường này vào Entity SearchCriteria trước
		// Ví dụ Entity có: @ElementCollection private Set<String> interestedInGenders;
//        Set<String> defaultGenders = new HashSet<>();
		// Logic mặc định: Có thể dựa vào giới tính của user hoặc để trống
		// Ví dụ đơn giản: tìm cả nam và nữ (cần định nghĩa các giá trị chuẩn "male",
		// "female")
//         defaultGenders.add("male");
//         defaultGenders.add("female");
//        defaultCriteria.s(defaultGenders); // Bỏ comment khi có trường này

		// Lưu vào DB
		return searchRepo.save(defaultCriteria);
	}

	@Override
	@Transactional
	public SearchCriteria createOrUpdateCriteria(Users user, SearchCriteria dto) {
		logger.info("Creating or updating criteria for user: {}", user.getAccount().getEmail());
		// Tìm criteria hiện có hoặc tạo mới nếu chưa có
		SearchCriteria criteria = findByUsers(user).orElse(new SearchCriteria()); // Tạo mới nếu chưa tồn tại

		criteria.setUsers(user); // Đảm bảo liên kết đúng user

		logger.debug("Saving criteria for user {}: {}", user.getAccount().getEmail(), criteria);
		return searchRepo.save(criteria);
	}

	// ========================================================================
	// == LOGIC LẤY DISCOVERY CARDS (NÊN ĐẶT TRONG DISCOVERY SERVICE RIÊNG) ==
	// ========================================================================

	/**
	 * Lấy *toàn bộ* danh sách các thẻ hồ sơ người dùng phù hợp với tiêu chí của
	 * người dùng hiện tại. (Không phân trang)
	 *
	 * @param currentUser Người dùng đang thực hiện yêu cầu.
	 * @return Một List chứa tất cả ProfileCardDto phù hợp.
	 */
	@Override
	@Transactional // Sử dụng org.springframework.transaction.annotation.Transactional
	public List<ProfileDto> getDiscoveryCards(Users currentUser) { // Đổi tên hàm để khớp với interface nếu cần
		logger.info("Fetching ALL discovery cards (no paging) for user: {}", currentUser.getAccount().getEmail());

		// 1. Lấy Search Criteria (hoặc default)
		SearchCriteria criteria = findByUsers(currentUser).orElseGet(() -> createDefaultCriteria(currentUser)); // Dùng
																												// lại
																												// hàm
																												// đã có
		logger.debug("Using criteria: {}", criteria);

		// --- Kiểm tra điều kiện tiên quyết ---
		if (currentUser.getLatitude() == null || currentUser.getLongitude() == null) {
			logger.warn("Current user {} location not set. Returning empty list.", currentUser.getAccount().getEmail());
			return Collections.emptyList();
		}
		if (criteria.getDistance() == null || criteria.getDistance() <= 0) {
			logger.warn("User {} criteria distance invalid ({}). Returning empty list.",
					currentUser.getAccount().getEmail(), criteria.getDistance());
			return Collections.emptyList();
		}
		// ** Kiểm tra interestedInGenders nếu có **
		// Set<String> interestedGenders = criteria.getInterestedInGenders();
		// if (interestedGenders == null || interestedGenders.isEmpty()) { ... return
		// Collections.emptyList(); }

		// 2. Lấy danh sách ID người dùng đã tương tác
		List<Users> interactedUsers = matchRepo.findUsersILiked(currentUser.getId());
		interactedUsers.add(currentUser);
		logger.debug("Excluding user IDs: {}", interactedUsers);
		// Trích xuất danh sách ID
		List<Long> interactedUserIds = interactedUsers.stream().map(Users::getId).collect(Collectors.toList());

		// 3. Gọi phương thức Repository để lọc
		logger.debug("Executing findDiscoverableUsersNoPaging query...");
		List<Users> potentialMatches = usersRepo.filterUsers(criteria.getMinAge(),criteria.getMaxAge(),criteria.getZodiacSign()
				,criteria.getPersonalityType(),criteria.getInterests(),currentUser.getLatitude(),currentUser.getLongitude(),(double) 50);
		logger.info("Found {} total potential matches in DB.", potentialMatches.size());

		// 4. Map List<Users> sang List<ProfileCardDto>
		List<ProfileDto> profileDtos = potentialMatches.stream()
				.map(targetUser -> mapUserToProfileCardDto(targetUser, currentUser)) // Gọi hàm helper bên dưới
				.collect(Collectors.toList());

		logger.info("Mapped {} users to ProfileCardDto.", profileDtos.size());
		return profileDtos;
	}

	// --- Helper method để map từ Users Entity sang ProfileCardDto ---
	private ProfileDto mapUserToProfileCardDto(Users targetUser, Users currentUser) {
		// Tính tuổi (Cần Users có getDob() trả về LocalDate)
//		Integer age = null;
//		if (targetUser.getBirthday() != null) {
//			try { // Thêm try-catch phòng trường hợp dob không hợp lệ
//				age = usersService.calculateAge(targetUser.getBirthday());
//			} catch (Exception e) {
//				logger.error("Error calculating age for user {}: {}", targetUser.getId(), e.getMessage());
//			}
//		}
//
//		// Lấy ảnh đại diện chính (Cần UserService có hàm này)
//		String primaryImageUrl = usersService.getPrimaryProfileImageUrl(targetUser);

		// Lấy thông tin vị trí hiển thị (Ví dụ: City)
		String location = targetUser.getAddress();
		if (location == null || location.trim().isEmpty()) {
			location = "Không xác định";
		}

		// Tính khoảng cách
//		Double distanceKm = null;
//		if (currentUser.getLatitude() != null && currentUser.getLongitude() != null && targetUser.getLatitude() != null
//				&& targetUser.getLongitude() != null) {
//			distanceKm = calculateDistanceHaversine(currentUser.getLatitude(), currentUser.getLongitude(),
//					targetUser.getLatitude(), targetUser.getLongitude());
//		}

		// Lấy tên hiển thị
		String displayName = (targetUser.getName() != null ? targetUser.getName() : "");

		// Tạo DTO bằng Builder
		return ProfileDto.builder()
				.id(targetUser.getId())
				.name(displayName)
				.imageUrl(targetUser.getImages().get(0).getImage())
				.location(location)
				.age(18)
				.build();
	}

	@Override
	public SearchCriteria getSettings(Long userId) {
		SearchCriteria res = null;
		Users currentUser = usersRepo.findById(userId).get();

		res = searchRepo.findByUsers(currentUser).get();

		return res;
	}

	@Transactional
	@Override
	public void saveSettings(Long userId, SearchCriteriaDto criteriaDto) {
		logger.info("--- SERVICE saveSettings START (using DTO) ---");
        logger.info("Service received userId: {}", userId);
        logger.info("Service received DTO: {}", criteriaDto);

        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for saveSettings: " + userId));
        logger.info("Found user ID: {}", user.getId());

        SearchCriteria criteriaToUpdate = user.getSearchCriteria();
        if (criteriaToUpdate == null) {
            logger.info("No existing SearchCriteria found, creating new one.");
            criteriaToUpdate = new SearchCriteria();
            criteriaToUpdate.setUsers(user);
            user.setSearchCriteria(criteriaToUpdate);
        } else {
            logger.info("Found existing SearchCriteria with ID: {}", criteriaToUpdate.getId());
        }

        logger.info("Current entity values before update: datingPurpose={}, minAge={}, maxAge={}, distance={}, interests={}, zodiacSign={}, personalityType={}",
                criteriaToUpdate.getDatingPurpose(), criteriaToUpdate.getMinAge(), criteriaToUpdate.getMaxAge(),
                criteriaToUpdate.getDistance(), criteriaToUpdate.getInterests(), criteriaToUpdate.getZodiacSign(),
                criteriaToUpdate.getPersonalityType());

        // Apply updates from DTO to Entity, only if DTO fields are non-null
        if (criteriaDto.getDatingPurpose() != null) {
            criteriaToUpdate.setDatingPurpose(criteriaDto.getDatingPurpose());
        }
        if (criteriaDto.getMinAge() != null) {
            criteriaToUpdate.setMinAge(criteriaDto.getMinAge()); // No .intValue() needed if entity uses Integer
        }
        if (criteriaDto.getMaxAge() != null) {
            criteriaToUpdate.setMaxAge(criteriaDto.getMaxAge()); // No .intValue() needed if entity uses Integer
        }
        if (criteriaDto.getDistance() != null) {
            criteriaToUpdate.setDistance(criteriaDto.getDistance());
        }
        if (criteriaDto.getInterests() != null) {
            criteriaToUpdate.setInterests(criteriaDto.getInterests());
        }
        if (criteriaDto.getZodiacSign() != null) {
            criteriaToUpdate.setZodiacSign(criteriaDto.getZodiacSign());
        }
        if (criteriaDto.getPersonalityType() != null) {
            criteriaToUpdate.setPersonalityType(criteriaDto.getPersonalityType());
        }

        logger.info("Entity values after applying updates from DTO: datingPurpose={}, minAge={}, maxAge={}, distance={}, interests={}, zodiacSign={}, personalityType={}",
                criteriaToUpdate.getDatingPurpose(), criteriaToUpdate.getMinAge(), criteriaToUpdate.getMaxAge(),
                criteriaToUpdate.getDistance(), criteriaToUpdate.getInterests(), criteriaToUpdate.getZodiacSign(),
                criteriaToUpdate.getPersonalityType());

        logger.info("Saving User entity (will cascade to SearchCriteria)...");
        Users savedUser = usersRepo.save(user);
        logger.info("User entity saved.");

        SearchCriteria resultCriteria = savedUser.getSearchCriteria();
        logger.info("--- SERVICE saveSettings END --- Returning Entity: {}", resultCriteria);
	}

	// --- Hàm tính khoảng cách Haversine ---
	// (Nên đặt trong một lớp Utils riêng)
//	private static final int EARTH_RADIUS_KM = 6371;
//
//	private Double calculateDistanceHaversine(double lat1, double lon1, double lat2, double lon2) {
//		// Kiểm tra giá trị đầu vào cơ bản
//		if (Double.isNaN(lat1) || Double.isNaN(lon1) || Double.isNaN(lat2) || Double.isNaN(lon2)) {
//			return null; // Trả về null nếu tọa độ không hợp lệ
//		}
//		// Tránh tính toán nếu trùng tọa độ
//		if (Math.abs(lat1 - lat2) < 1e-9 && Math.abs(lon1 - lon2) < 1e-9) {
//			return 0.0;
//		}
//
//		double latDistance = Math.toRadians(lat2 - lat1);
//		double lonDistance = Math.toRadians(lon2 - lon1);
//		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
//				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//		// Đảm bảo giá trị bên trong acos nằm trong khoảng [-1, 1] để tránh NaN
//		double valueForAcos = Math.sqrt(a) / Math.sqrt(1 - a);
//		if (Double.isNaN(valueForAcos) || Double.isInfinite(valueForAcos)) {
//			// Xử lý trường hợp đặc biệt khi a gần bằng 1 (điểm đối cực)
//			if (a > 1.0)
//				a = 1.0; // Giới hạn a = 1
//			if (a == 1.0)
//				return Math.round(EARTH_RADIUS_KM * Math.PI * 10.0) / 10.0; // Nửa chu vi trái đất
//			valueForAcos = Math.sqrt(a) / Math.sqrt(1 - a); // Tính lại
//		}
//		if (Double.isNaN(valueForAcos))
//			return null; // Vẫn NaN thì trả null
//
//		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//		double distance = EARTH_RADIUS_KM * c;
//
//		return Math.round(distance * 10.0) / 10.0; // Làm tròn
//	}

}
