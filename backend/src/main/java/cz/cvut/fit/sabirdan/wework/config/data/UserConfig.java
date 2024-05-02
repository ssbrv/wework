package cz.cvut.fit.sabirdan.wework.config.data;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository)
    {
        User user1 = new User("joshpeterson", "password123", "Josh", "Peterson");
        User user2 = new User("jirinovak", "password123", "Jiri", "Novak");
        User user3 = new User("mark123", "password123", "Mark", "Nelson");
        User user4 = new User("davidstar", "password123", "David", "Hvezda");

        return args -> {
            userRepository.saveAll(List.of(user1, user2, user3, user4));
        };
    }
}
