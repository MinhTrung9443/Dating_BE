package vn.iotstar.DatingApp.Entity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;
}
