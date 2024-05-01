package cz.cvut.fit.sabirdan.wework.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrudService<T> {
    T create(T e);
    Optional<T> readById(Long id);
    Iterable<T> readAll();
    void update(Long id, T e);
    void deleteById(Long id);
    JpaRepository<T, Long> getRepository();
}

