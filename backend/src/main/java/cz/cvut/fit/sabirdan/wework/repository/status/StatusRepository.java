package cz.cvut.fit.sabirdan.wework.repository.status;

import cz.cvut.fit.sabirdan.wework.domain.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface StatusRepository<T extends Status> extends JpaRepository<T, Long> {
    boolean existsByValue(String value);
    Optional<T> findByValue(String value);
}
