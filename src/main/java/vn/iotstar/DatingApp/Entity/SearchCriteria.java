package vn.iotstar.DatingApp.Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SearchCriteria {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String datingPurpose;
    private int minAge;
    private int maxAge;
    private int distance;
    private String interests;
    private String zodiacSign;
    private String personalityType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private Users users;
}
