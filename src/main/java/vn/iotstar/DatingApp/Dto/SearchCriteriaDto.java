package vn.iotstar.DatingApp.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Important for Jackson
@AllArgsConstructor
@Builder
public class SearchCriteriaDto {

    private String datingPurpose;

    private Integer minAge;

    private Integer maxAge;

    private Double distance;

    private String interests;

    private String zodiacSign;

    private String personalityType;

    // No explicit getters/setters needed here if using Lombok
}