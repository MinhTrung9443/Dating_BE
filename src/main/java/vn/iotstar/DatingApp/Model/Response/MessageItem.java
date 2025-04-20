package vn.iotstar.DatingApp.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageItem {
	private int senderId;
    private String name;
    private String content;
    private int count;
    private String picture;
}
