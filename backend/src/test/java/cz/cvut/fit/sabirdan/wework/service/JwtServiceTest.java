package cz.cvut.fit.sabirdan.wework.service;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.repository.InvalidJwtTokenRepository;
import cz.cvut.fit.sabirdan.wework.service.jwt.JwtService;
import cz.cvut.fit.sabirdan.wework.utils.RandomHexKeyGenerator;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtServiceTest {
    @Mock
    private InvalidJwtTokenRepository invalidJwtTokenRepository;
    @Mock
    private JwtService jwtService;

    @Before
    public void setup() {
        jwtService = new JwtService(RandomHexKeyGenerator.generateRandomHexKey(256), 3600000L, invalidJwtTokenRepository);
    }

    @Test
    public void testValidJwtToken() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setLastFullLogoutDate(LocalDateTime.now());
        String jwtToken = jwtService.generateJwtToken(user);
        when(invalidJwtTokenRepository.existsByJwtToken(jwtToken)).thenReturn(false);

        // Act
        boolean result = !jwtService.isJwtTokenInvalid(jwtToken, user);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testLoggedOutJwtToken() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setLastFullLogoutDate(LocalDateTime.now());
        String jwtToken = jwtService.generateJwtToken(user);
        when(invalidJwtTokenRepository.existsByJwtToken(jwtToken)).thenReturn(true);

        // Act
        boolean result = jwtService.isJwtTokenInvalid(jwtToken, user);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testFullyLoggedOutJwtToken() {
        // Arrange
        User user = new User();
        user.setId(1L);
        String jwtToken = jwtService.generateJwtToken(user);
        user.setLastFullLogoutDate(LocalDateTime.now().plusSeconds(2));
        // when(invalidJwtTokenRepository.existsByJwtToken(jwtToken)).thenReturn(false); - the test ends before this check

        // Act
        boolean result = jwtService.isJwtTokenInvalid(jwtToken, user);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testExpiredJwtToken() throws InterruptedException {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setLastFullLogoutDate(LocalDateTime.now());
        String jwtToken = jwtService.generateJwtToken(user, 1L);
        // when(invalidJwtTokenRepository.existsByJwtToken(jwtToken)).thenReturn(false); - the test ends before this check
        Thread.sleep(2); // wait for 2 milliseconds for token to expire

        // Act and Assert
        Assert.assertThrows(ExpiredJwtException.class, () -> {
            jwtService.isJwtTokenInvalid(jwtToken, user);
        });
    }
}
