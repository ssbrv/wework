package cz.cvut.fit.sabirdan.wework.service.user;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.repository.UserRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import cz.cvut.fit.sabirdan.wework.http.exception.ConflictException;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
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
    public User save(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new ConflictException("username", getEntityName() + " already exists with username \"" + user.getUsername() + "\"");

        return super.save(user);
    }
}
