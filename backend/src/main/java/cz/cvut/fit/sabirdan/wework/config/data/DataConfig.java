package cz.cvut.fit.sabirdan.wework.config.data;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.DefaultSystemRole;
import cz.cvut.fit.sabirdan.wework.repository.UserRepository;
import cz.cvut.fit.sabirdan.wework.service.role.member.MemberRoleService;
import cz.cvut.fit.sabirdan.wework.service.role.system.SystemRoleService;
import cz.cvut.fit.sabirdan.wework.service.status.membership.MembershipStatusService;
import cz.cvut.fit.sabirdan.wework.service.status.project.ProjectStatusService;
import cz.cvut.fit.sabirdan.wework.service.status.task.TaskStatusService;
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
    CommandLineRunner commandLineRunner(
            MemberRoleService memberRoleService,
            SystemRoleService systemRoleService,
            UserRepository userRepository,
            TaskStatusService taskStatusService,
            ProjectStatusService projectStatusService,
            MembershipStatusService membershipStatusService
    )
    {
        return args -> {
            taskStatusService.initializeTaskStatuses();
            systemRoleService.initializeSystemRoles();
            memberRoleService.initializeMemberRoles();
            projectStatusService.initializeProjectStatuses();
            membershipStatusService.initializeMembershipStatuses();

            if (!userRepository.existsByUsername("superadmin"))
                userRepository.save(
                        new User(
                        "superadmin",
                        passwordEncoder.encode("safesecret"),
                        "Super",
                        "Admin",
                        systemRoleService.findDefaultByName(DefaultSystemRole.SUPER_ADMIN.name())
                        )
                );

            if (!userRepository.existsByUsername("jirinovak"))
                userRepository.save(
                        new User(
                                "jirinovak",
                                passwordEncoder.encode("password"),
                                "Jiri",
                                "Novak",
                                systemRoleService.findDefaultByName(DefaultSystemRole.USER.name())
                        )
                );

            if (!userRepository.existsByUsername("davidstar"))
                userRepository.save(
                        new User(
                                "davidstar",
                                passwordEncoder.encode("password"),
                                "David",
                                "Star",
                                systemRoleService.findDefaultByName(DefaultSystemRole.USER.name())
                        )
                );
        };
    }
}
