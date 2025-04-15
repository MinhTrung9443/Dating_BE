package vn.iotstar.DatingApp.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "users")
public class Users {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String gender;
    private String sexualOrientation;
    
    @Temporal(TemporalType.DATE)
    private Date birthday;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String biography;
    
    private double height;
    private int weight;
    
    @Column(name = "zodiac_sign")
    private String zodiacSign;
    
    @Column(name = "personality_type")
    private String personalityType;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String interests;
    
    private String address;
    private String job;
    // FOR LOCATION
    private Double latitude;
    private Double longitude;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    
    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();
    
    @JsonIgnore
    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SearchCriteria searchCriteria;
    
    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MatchList> matchLists = new ArrayList<>();
}
