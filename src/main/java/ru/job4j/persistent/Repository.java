package ru.job4j.persistent;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<T, Id extends Serializable> {
    void persist(T entity);

    void update(T entity);

    Optional<T> findById(Id id);

    void delete(T entity);

    List<T> findAll();
}
