package vn.iotstar.DatingApp.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetMessageRequest {
	private Long user1;
	private Long user2;
}
