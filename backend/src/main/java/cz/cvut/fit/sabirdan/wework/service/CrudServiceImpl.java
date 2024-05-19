package cz.cvut.fit.sabirdan.wework.service;

import cz.cvut.fit.sabirdan.wework.domain.EntityWithIdLong;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public abstract class CrudServiceImpl<T extends EntityWithIdLong> implements CrudService<T> {
    @Override
    public T save(T e) {
        return getRepository().save(e);
    }

    @Override
    public Optional<T> findById(Long id) {
        return getRepository().findById(id);
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public void update(Long id, T e) {
        getRepository().flush();
    }

    @Override
    public void deleteById(Long id){
        if (!getRepository().existsById(id))
            throw new NotFoundException("This " + getEntityName().toLowerCase() + " does not exist. Probably somebody already deleted the " + getEntityName().toLowerCase());

        getRepository().deleteById(id);
    }

    public abstract String getEntityName();
}
