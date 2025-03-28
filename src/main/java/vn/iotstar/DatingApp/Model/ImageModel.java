package vn.iotstar.DatingApp.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageModel {
	private Long id;

    private String image;

    private Long userId;
}
