package vn.iotstar.DatingApp.Service;

import java.util.List;
import java.util.Optional;

import vn.iotstar.DatingApp.Dto.ProfileDto;
import vn.iotstar.DatingApp.Dto.SearchCriteriaDto;
import vn.iotstar.DatingApp.Entity.SearchCriteria;
import vn.iotstar.DatingApp.Entity.Users;

public interface SearchCriteriaService {

	Optional<SearchCriteria> findByUsers(Users users);

	SearchCriteria createDefaultCriteria(Users user);

	SearchCriteria createOrUpdateCriteria(Users user, SearchCriteria dto);

	/**
	 * Lấy *toàn bộ* danh sách các thẻ hồ sơ người dùng phù hợp với tiêu chí của
	 * người dùng hiện tại. (Không phân trang)
	 *
	 * @param currentUser Người dùng đang thực hiện yêu cầu.
	 * @return Một List chứa tất cả ProfileCardDto phù hợp.
	 */
	List<ProfileDto> getDiscoveryCards(Users currentUser);

	void saveSettings(Long userId, SearchCriteriaDto settings);

	SearchCriteria getSettings(Long userId);

}
