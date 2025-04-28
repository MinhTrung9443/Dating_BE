package vn.iotstar.DatingApp.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.DatingApp.Entity.OtpCode;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    Optional<OtpCode> findTopByIdentifierAndExpiresAtAfterOrderByCreatedAtDesc(
            String identifier, LocalDateTime currentTimre);

    void deleteByIdentifier(String identifie);

}