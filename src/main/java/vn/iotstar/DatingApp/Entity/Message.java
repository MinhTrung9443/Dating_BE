package vn.iotstar.DatingApp.Entity;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "message")
public class Message {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private Users fromUser;
    
    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private Users toUser;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String messageContent;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;
    
    private boolean liked;
    
    @Column(name = "is_read") 
    private boolean isRead;
    
    @ManyToOne
    @JoinColumn(name = "match_id")
    private MatchList matchlist;
}
