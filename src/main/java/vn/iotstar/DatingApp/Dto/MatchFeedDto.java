package vn.iotstar.DatingApp.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchFeedDto {
	private String id; // Sử dụng String để linh hoạt
    private String name;
    private String pictureUrl; // ** Đổi tên từ picture để rõ ràng là URL **
    private String location; // Có thể là khoảng cách hoặc địa điểm
    private String date; // Ngày match (dạng String để hiển thị)
    private boolean isNewMatch; // ** Thêm để hiển thị "Nouveau Match !" nếu cần ** (Tùy chọn)
}
