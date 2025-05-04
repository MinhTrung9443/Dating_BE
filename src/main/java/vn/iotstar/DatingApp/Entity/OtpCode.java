// src/main/java/vn/iotstar/DatingApp/entity/OtpCode.java
package vn.iotstar.DatingApp.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "otp_codes") // Tên bảng trong SQL Server
public class OtpCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255) // Email hoặc SĐT
    private String identifier;

    @Column(name = "otp_code", nullable = false, length = 10)
    private String otpCode;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default // Gán giá trị mặc định khi dùng Builder
    private LocalDateTime createdAt = LocalDateTime.now();

    // Phương thức kiểm tra hết hạn (tiện ích)
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}