package vn.iotstar.DatingApp.Entity;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class MatchList {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private Date createdAt;
    
    @OneToMany(mappedBy = "matchlist")
    private List<Message> messages;
    
    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private Users user1; 

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private Users user2; 
}
