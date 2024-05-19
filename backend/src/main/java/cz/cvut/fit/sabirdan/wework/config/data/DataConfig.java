package cz.cvut.fit.sabirdan.wework.config.data;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.role.SystemRole;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.repository.UserRepository;
import cz.cvut.fit.sabirdan.wework.service.role.member.MemberRoleService;
import cz.cvut.fit.sabirdan.wework.service.role.system.SystemRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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

            User superAdmin = new User("superadmin", passwordEncoder.encode("s4fe5ecr3t"), "Josh", "Peterson");
            superAdmin.setRole(systemRoleService.getSuperAdminSystemRole());

            User jiriNovak = new User("jirinovak", passwordEncoder.encode("password123"), "Jiri", "Novak");
            jiriNovak.setRole(systemRoleService.getUserSystemRole());

            User davidStar = new User("davidstar", passwordEncoder.encode("password123"), "David", "Hvezda");
            davidStar.setRole(systemRoleService.getUserSystemRole());

            userRepository.saveAll(List.of(superAdmin, jiriNovak, davidStar));
        };
    }
}
