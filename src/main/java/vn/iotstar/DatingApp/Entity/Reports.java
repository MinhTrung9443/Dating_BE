package vn.iotstar.DatingApp.Entity;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Reports {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reporter;
    private Long reported;
    private String reason;
    private Date reportAt;
}
