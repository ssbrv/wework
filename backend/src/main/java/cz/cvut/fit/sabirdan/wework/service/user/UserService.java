package cz.cvut.fit.sabirdan.wework.service.user;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

public interface UserService extends CrudService<User> {
    User findByUsername(String username);
}
