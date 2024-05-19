package cz.cvut.fit.sabirdan.wework.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
    T save(T e);
    Optional<T> findById(Long id);
    List<T> findAll();
    void update(Long id, T e);
    void deleteById(Long id);
    JpaRepository<T, Long> getRepository();
}

