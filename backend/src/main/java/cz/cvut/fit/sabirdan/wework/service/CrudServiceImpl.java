package cz.cvut.fit.sabirdan.wework.service;

import cz.cvut.fit.sabirdan.wework.domain.EntityWithIdLong;
import cz.cvut.fit.sabirdan.wework.utilites.exception.ConflictException;
import cz.cvut.fit.sabirdan.wework.utilites.exception.NotFoundException;

import java.util.Optional;

public abstract class CrudServiceImpl<T extends EntityWithIdLong> implements CrudService<T> {
    @Override
    public T create(T e) {
        if (e.getId() != null && getRepository().existsById(e.getId()))
            throw new ConflictException(getEntityName() + " with such id already exists");

        return getRepository().save(e);
    }

    @Override
    public Optional<T> readById(Long id) {
        return getRepository().findById(id);
    }

    @Override
    public Iterable<T> readAll() {
        return getRepository().findAll();
    }

    @Override
    public void update(Long id, T e) {
        getRepository().flush();
    }

    @Override
    public void deleteById(Long id){
        if (!getRepository().existsById(id))
            throw new NotFoundException(getEntityName() + " with such id does not exist");

        getRepository().deleteById(id);
    }

    public abstract String getEntityName();
}
