package vn.iotstar.DatingApp.Entity;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonBackReference("matchlist-message")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private MatchList matchlist;
}
