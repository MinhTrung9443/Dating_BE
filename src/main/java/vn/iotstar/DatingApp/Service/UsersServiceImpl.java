package vn.iotstar.DatingApp.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Entity.Users;
import vn.iotstar.DatingApp.Repository.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService{
	private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
	@Autowired
	private UsersRepository userRepository;

	@Override
	public Optional<Users> findByAccount(Account account) {
		return userRepository.findByAccount(account);
	}

	@Override
	public Integer calculateAge(Date dob) {
        if (dob == null) {
            logger.warn("Date of birth is null, cannot calculate age.");
            return null; // Hoặc trả về 0, hoặc ném Exception tùy logic
        }

        try {
            // Chuyển đổi java.util.Date sang java.time.LocalDate
            Instant instant = dob.toInstant();
            ZoneId zoneId = ZoneId.systemDefault(); // Lấy múi giờ hệ thống
            LocalDate birthDate = instant.atZone(zoneId).toLocalDate();
            LocalDate currentDate = LocalDate.now(zoneId); // Lấy ngày hiện tại theo múi giờ

            // Kiểm tra xem ngày sinh có ở tương lai không
            if (birthDate.isAfter(currentDate)) {
                 logger.warn("Date of birth {} is in the future.", birthDate);
                return null; // Hoặc 0, hoặc ném Exception
            }

            // Tính khoảng thời gian (Period)
            Period period = Period.between(birthDate, currentDate);

            // Lấy số năm
            return period.getYears();

        } catch (Exception e) {
            // Ghi log lỗi nếu có vấn đề khi chuyển đổi hoặc tính toán
            logger.error("Error calculating age for date of birth: {}", dob, e);
            return null; // Trả về null khi có lỗi
        }
    }

	@Override
	@Transactional // Đảm bảo là một giao dịch
	public Users updateUserLocation(Long id, Double latitude, Double longitude) {
		if (id == null || latitude == null || longitude == null) {
            logger.warn("Attempted to update location with null parameters. UserId: {}, Lat: {}, Lon: {}", id, latitude, longitude);
            throw new IllegalArgumentException("User ID, latitude, and longitude cannot be null.");
        }

        logger.info("Updating location for user ID: {}. New Lat: {}, New Lon: {}", id, latitude, longitude);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {} during location update.", id);
                    return new RuntimeException("Không tìm thấy người dùng với ID: " + id);
                });

        user.setLatitude(latitude);
        user.setLongitude(longitude);
        userRepository.save(user); // Lưu thay đổi vào DB
        logger.info("Location successfully updated for user ID: {}", id);
        
        return user;
    }

}
