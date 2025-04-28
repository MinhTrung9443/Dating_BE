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
public class Match {
	private Long id;
    private String name;
    private String picture;
    private String location;
    private String date;
}
