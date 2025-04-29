package vn.iotstar.DatingApp.Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "search_criteria")
public class SearchCriteria {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dating_purpose")
    private String datingPurpose;
    
    @Column(name = "min_age")
    private int minAge;
    
    @Column(name = "max_age")
    private int maxAge;
    
    private Double distance;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String interests;
    
    @Column(name = "zodiac_sign")
    private String zodiacSign;
    
    @Column(name = "personality_type")
    private String personalityType;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private Users users;
}
