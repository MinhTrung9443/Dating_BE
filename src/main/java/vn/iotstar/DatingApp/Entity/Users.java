package vn.iotstar.DatingApp.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Users {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    
    @OneToMany(mappedBy = "user")
    private List<Image> images;
    
    @OneToOne(mappedBy = "users")
    private SearchCriteria searchCriteria;
    
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchList> matchLists;
}
