package cz.cvut.fit.sabirdan.wework.controller.role.member;

import cz.cvut.fit.sabirdan.wework.http.response.role.RoleDTO;
import cz.cvut.fit.sabirdan.wework.service.role.member.MemberRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/member-roles")
public class MemberRoleController {
    private final MemberRoleService memberRoleService;

    @Autowired
    public MemberRoleController(MemberRoleService memberRoleService) {
        this.memberRoleService = memberRoleService;
    }

    @GetMapping
    public ResponseEntity<Iterable<RoleDTO>> getMemberRoles() {
        return ResponseEntity.ok(memberRoleService.findAll().stream().map(RoleDTO::new).collect(Collectors.toList()));
    }
}
