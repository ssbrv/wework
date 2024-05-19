package cz.cvut.fit.sabirdan.wework.service.user;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.http.request.UpdateUserRequest;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

public interface UserService extends CrudService<User> {
    User getByUsername(String username);

    User getById(Long id);

    void updateUserByUsername(String username, UpdateUserRequest updateUserRequest);

    void updateUserById(Long id, UpdateUserRequest updateUserRequest);

    void updateUser(User user, UpdateUserRequest updateUserRequest);
}
