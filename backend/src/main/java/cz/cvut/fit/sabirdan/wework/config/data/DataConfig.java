package cz.cvut.fit.sabirdan.wework.config.data;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.DefaultSystemRole;
import cz.cvut.fit.sabirdan.wework.repository.UserRepository;
import cz.cvut.fit.sabirdan.wework.service.role.member.MemberRoleService;
import cz.cvut.fit.sabirdan.wework.service.role.system.SystemRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner commandLineRunner(MemberRoleService memberRoleService, SystemRoleService systemRoleService, UserRepository userRepository)
    {
        return args -> {
            systemRoleService.initializeSystemRoles();
            memberRoleService.initializeMemberRoles();

            if (!userRepository.existsByUsername("superadmin"))
                userRepository.save(
                        new User(
                        "superadmin",
                        passwordEncoder.encode("s4fe5ecr3t"),
                        "Super",
                        "Admin",
                        systemRoleService.findDefaultByName(DefaultSystemRole.SUPER_ADMIN.name())
                        )
                );
        };
    }
}
