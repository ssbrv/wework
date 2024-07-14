package cz.cvut.fit.sabirdan.wework.service.status;

import cz.cvut.fit.sabirdan.wework.domain.status.Status;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

public interface StatusService<T extends Status> extends CrudService<T> {
    T getById(Long id);
    T getByValue(String value, String attributeNameForErrorHandling);
    T getByValue(String value);
}
