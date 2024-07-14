package cz.cvut.fit.sabirdan.wework.service.status;

import cz.cvut.fit.sabirdan.wework.domain.status.Status;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.repository.status.StatusRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract public class StatusServiceImpl<T extends Status, R extends StatusRepository<T>> extends CrudServiceImpl<T> implements StatusService<T> {
    protected final R statusRepository;

    @Override
    public T getById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The status does not exist"));
    }

    @Override
    public T getByValue(String value) {
        return statusRepository.findByValue(value)
                .orElseThrow(() -> new NotFoundException("The status does not exist"));
    }

    @Override
    public T getByValue(String value, String attributeNameForErrorHandling) {
        return statusRepository.findByValue(value)
                .orElseThrow(() -> new NotFoundException("The status does not exist", attributeNameForErrorHandling));
    }
}