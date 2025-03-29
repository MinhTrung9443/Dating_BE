package vn.iotstar.DatingApp.Model.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceFilterRequest {
    @NotNull(message = "Khoảng cách tối đa là bắt buộc")
    @Min(value = 1, message = "Khoảng cách phải ít nhất 1 km")
    private Double maxDistance;
}