package cz.cvut.fit.sabirdan.wework.service.user;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.http.request.EditBasicRequest;
import cz.cvut.fit.sabirdan.wework.http.request.EditUsernameRequest;
import cz.cvut.fit.sabirdan.wework.http.response.GetMeRespond;
import cz.cvut.fit.sabirdan.wework.repository.UserRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import cz.cvut.fit.sabirdan.wework.http.exception.ConflictException;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl extends CrudServiceImpl<User> implements UserService {

    private final UserRepository userRepository;

    @Override
    public JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public String getEntityName() {
        return "User";
    }

    @Override
    public User findByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("username", getEntityName() + " does not exist with username \"" + username + "\"")
        );
    }

    @Override
    public GetMeRespond getMe() {
        User user = findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return GetMeRespond.builder()
                .username(user.getUsername())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .sex(user.getSex())
                .build();
    }

    @Override
    public void editBasic(EditBasicRequest editBasicRequest) {
        User user = findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setFirstName(editBasicRequest.getFirstName());
        user.setLastName(editBasicRequest.getLastName());
        user.setSex(editBasicRequest.getSex());
    }

    @Override
    public void editUsername(EditUsernameRequest editUsernameRequest) {
        User user = findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.getUsername().equals(editUsernameRequest.getUsername()))
            return;

        if (userRepository.existsByUsername(user.getUsername()))
            throw new ConflictException("username", getEntityName() + " already exists with username \"" + editUsernameRequest.getUsername() + "\"");

        user.setUsername(editUsernameRequest.getUsername());
    }

    @Override
    public User save(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new ConflictException("username", getEntityName() + " already exists with username \"" + user.getUsername() + "\"");

        return super.save(user);
    }


}
