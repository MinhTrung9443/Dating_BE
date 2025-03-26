package vn.iotstar.DatingApp.Model.Request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HobbiesFilterRequest {
	@NotEmpty(message = "At least one hobby must be provided")
    private List<String> hobbies;
}
