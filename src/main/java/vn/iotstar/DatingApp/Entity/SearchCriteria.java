package vn.iotstar.DatingApp.Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    private Double distance;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String interests;

    @Column(name = "zodiac_sign")
    private String zodiacSign;

    @Column(name = "personality_type")
    private String personalityType;
    
    public SearchCriteria(Integer min, Integer max, Double distance) {
    	this.minAge = min;
    	this.maxAge = max;
    	this.distance = distance;
    }

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private Users users;
}
