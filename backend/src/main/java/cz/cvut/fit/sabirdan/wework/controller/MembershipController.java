package cz.cvut.fit.sabirdan.wework.controller;

import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeMemberRoleRequest;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeMembershipStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.request.InviteRequest;
import cz.cvut.fit.sabirdan.wework.http.request.LeaveRequest;
import cz.cvut.fit.sabirdan.wework.http.response.InviteResponse;
import cz.cvut.fit.sabirdan.wework.http.response.membership.MembershipDTO;
import cz.cvut.fit.sabirdan.wework.service.membership.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping
    private ResponseEntity<InviteResponse> invite(@RequestBody @Validated InviteRequest inviteRequest) {
        return ResponseEntity.ok(membershipService.invite(inviteRequest));
    }

    // supported operations: kick, accept, reject, uninvite
    @PutMapping("{membershipId}")
    private void changeMembershipStatus(@PathVariable Long membershipId, @RequestBody @Validated ChangeMembershipStatusRequest changeMembershipStatusRequest) {
        membershipService.changeMembershipStatus(membershipId, changeMembershipStatusRequest);
    }

    @PutMapping("{membershipId}/member-role")
    private void changeMemberRole(@PathVariable Long membershipId, @RequestBody @Validated ChangeMemberRoleRequest changeMemberRoleRequest) {
        membershipService.changeMemberRole(membershipId, changeMemberRoleRequest);
    }

    @GetMapping("invitations")
    private ResponseEntity<Iterable<MembershipDTO>> getInvitations() {
        return ResponseEntity.ok(membershipService.getInvitations().stream().map(MembershipDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("{membershipId}")
    private ResponseEntity<MembershipDTO> getMembership(@PathVariable Long membershipId) {
        return ResponseEntity.ok(new MembershipDTO(membershipService.getById(membershipId)));
    }
}
