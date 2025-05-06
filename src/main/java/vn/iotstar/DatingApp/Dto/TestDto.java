package vn.iotstar.DatingApp.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TestDto {
	@JsonProperty("datingPurpose")
    private String datingPurpose;
	
}