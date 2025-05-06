package vn.iotstar.DatingApp.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProfileDto {

	private Long id;

    private String name;

    private String imageUrl;

    private Integer age;

    private String location;
}
