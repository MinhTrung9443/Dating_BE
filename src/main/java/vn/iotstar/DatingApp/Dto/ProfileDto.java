package vn.iotstar.DatingApp.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

	private Long id;

    private String name;

    private String imageUrl;

    private Integer age;

    private String location;
}
