package vn.iotstar.DatingApp.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageItem {
	private int senderId;
    private String name;
    private String content;
    private int count;
    private String picture;
}
