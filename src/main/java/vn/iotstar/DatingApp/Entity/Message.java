package vn.iotstar.DatingApp.Entity;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Message {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromUser;
    private Long toUser;
    private String messageContent;
    private Date sentAt;
    private boolean liked;
    
    @ManyToOne()
    @JsonBackReference("matchlist-message")
    @JsonIgnoreProperties(ignoreUnknown = true) // thêm dòng này
    private MatchList matchlist;
}
