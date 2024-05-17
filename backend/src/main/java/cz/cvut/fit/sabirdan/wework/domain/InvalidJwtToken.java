package cz.cvut.fit.sabirdan.wework.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "invalid_jwt_tokens")
public class InvalidJwtToken extends EntityWithIdLong {
    @Column(nullable = false)
    private String jwtToken;

    @Column(nullable = false)
    private LocalDateTime expiration;
}
