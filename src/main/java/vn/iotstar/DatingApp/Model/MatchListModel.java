package vn.iotstar.DatingApp.Model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchListModel {
	private Long id;
    private String status;
    private Date createdAt;
    private Long user1;
    private Long user2;
}
