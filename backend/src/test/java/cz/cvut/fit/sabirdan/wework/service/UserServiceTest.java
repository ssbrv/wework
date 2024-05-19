package cz.cvut.fit.sabirdan.wework.service;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.repository.UserRepository;
import cz.cvut.fit.sabirdan.wework.service.user.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserServiceImpl userService;

    @Before
    public void setup() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void testFindByUsernameSuccess() {
        // Arrange
        User user = new User();
        user.setUsername("testpeterson");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act
        User result = userService.getByUsername(user.getUsername());

        // Assert
        assertEquals(user, result);
    }

    @Test
    public void testFindByUsernameThrowsException() {
        // Arrange
        String usernameOfNonExistingUser = "notfound";
        String expectedMessage = "User does not exist with username \"" + usernameOfNonExistingUser + "\"";
        String expectedAttribute = "username";
        when(userRepository.findByUsername(usernameOfNonExistingUser)).thenReturn(Optional.empty());

        // Act and Assert
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            userService.getByUsername(usernameOfNonExistingUser);
        });
        assertEquals(expectedMessage, notFoundException.getMessage());
        assertEquals(expectedAttribute, notFoundException.getAttribute());
    }
}
