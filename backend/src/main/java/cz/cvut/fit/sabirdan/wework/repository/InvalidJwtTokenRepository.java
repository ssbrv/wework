package cz.cvut.fit.sabirdan.wework.repository;

import cz.cvut.fit.sabirdan.wework.domain.InvalidJwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface InvalidJwtTokenRepository extends JpaRepository<InvalidJwtToken, Long> {
    boolean existsByJwtToken(String jwtToken);

    @Transactional
    @Modifying
    @Query("DELETE FROM InvalidJwtToken jwt_token WHERE jwt_token.expiration < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();
}
