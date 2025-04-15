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
@Table(name = "match_list")
public class MatchList {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Trạng thái của match:
     * - LIKE: User1 đã like User2
     * - DISLIKE: User1 đã dislike User2
     * - MATCHED: Cả hai đã like nhau
     * - REWIND: User1 đã rewind (quay lại) sau khi dislike User2 
     * - PENDING: User1 đã like User2 nhưng User2 chưa phản hồi
     */
    private String status;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @JsonIgnore
    @OneToMany(mappedBy = "matchlist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private Users user1; 

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private Users user2;
    
    /**
     * Đã xem profile hay chưa
     */
    @Column(name = "profile_viewed")
    private boolean profileViewed = false;
}
