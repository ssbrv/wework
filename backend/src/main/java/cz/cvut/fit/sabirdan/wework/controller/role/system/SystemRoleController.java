package cz.cvut.fit.sabirdan.wework.controller.role.system;

import cz.cvut.fit.sabirdan.wework.service.role.system.SystemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/system-role")
public class SystemRoleController {

    private final SystemRoleService systemRoleService;

    @Autowired
    public SystemRoleController(SystemRoleService systemRoleService) {
        this.systemRoleService = systemRoleService;
    }
}
