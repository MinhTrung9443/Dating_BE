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
public class Profile {
    private String name;
    private String imageUrl;
    private Integer age;
    private String location;

}
