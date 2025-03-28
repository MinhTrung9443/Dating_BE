package vn.iotstar.DatingApp.Model;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserModel {
    private Long id;
    private String name;
    private String phone;
    private String gender;
    private String sexualOrientation;
    private Date birthday;
    private String biography;
    private double height;
    private int weight;
    private String zodiacSign;
    private String personalityType;
    private String interests;
    private String address;
    private String job;
}
