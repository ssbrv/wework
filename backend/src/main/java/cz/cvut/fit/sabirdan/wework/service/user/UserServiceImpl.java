package cz.cvut.fit.sabirdan.wework.service.user;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.http.exception.UnauthorizedException;
import cz.cvut.fit.sabirdan.wework.http.request.UpdateUserRequest;
import cz.cvut.fit.sabirdan.wework.repository.UserRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import cz.cvut.fit.sabirdan.wework.http.exception.ConflictException;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public User getByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("username", getEntityName() + " does not exist with username \"" + username + "\"")
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(getEntityName() + " does not exist")
        );
    }

    @Override
    public void updateUserByUsername(String username, UpdateUserRequest updateUserRequest) {
        updateUser(getByUsername(username), updateUserRequest);
    }

    @Override
    public void updateUserById(Long id, UpdateUserRequest updateUserRequest) {
        updateUser(getById(id), updateUserRequest);
    }

    @Override
    public void updateUser(User user, UpdateUserRequest updateUserRequest) {
        User editor = getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!editor.getUsername().equals(user.getUsername()) && (!editor.isAuthorized(Authorization.SYSTEM_EDIT_USERS) || !editor.hasAuthorityOver(user)))
            throw new UnauthorizedException("You do not have authority to edit user \"" + updateUserRequest.getUsername() + "\"");

        if (!user.getUsername().equals(updateUserRequest.getUsername()) && userRepository.existsByUsername(updateUserRequest.getUsername()))
            throw new ConflictException("username", getEntityName() + " already exists with username \"" + updateUserRequest.getUsername() + "\"");

        user.setUsername(updateUserRequest.getUsername());
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setSex(updateUserRequest.getSex());
    }

    @Override
    public User save(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new ConflictException("username", getEntityName() + " already exists with username \"" + user.getUsername() + "\"");

        return super.save(user);
    }
}
