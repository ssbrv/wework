package cz.cvut.fit.sabirdan.wework.service.user;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.http.request.EditBasicRequest;
import cz.cvut.fit.sabirdan.wework.http.request.EditUsernameRequest;
import cz.cvut.fit.sabirdan.wework.http.response.GetMeRespond;
import cz.cvut.fit.sabirdan.wework.service.CrudService;
import org.springframework.security.core.Authentication;

public interface UserService extends CrudService<User> {
    User findByUsername(String username);

    GetMeRespond getMe();

    void editBasic(EditBasicRequest editBasicRequest);

    void editUsername(EditUsernameRequest editUsernameRequest);
}
