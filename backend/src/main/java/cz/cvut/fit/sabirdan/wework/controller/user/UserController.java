package cz.cvut.fit.sabirdan.wework.controller.user;

import cz.cvut.fit.sabirdan.wework.http.request.EditBasicRequest;
import cz.cvut.fit.sabirdan.wework.http.request.EditUsernameRequest;
import cz.cvut.fit.sabirdan.wework.http.response.GetMeRespond;
import cz.cvut.fit.sabirdan.wework.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // information that only the user himself can read
    @GetMapping("/me")
    private ResponseEntity<GetMeRespond> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }

    @PutMapping("/me/basic")
    private void editBasic(@RequestBody @Validated EditBasicRequest editBasicRequest) {
        userService.editBasic(editBasicRequest);
    }

    @PutMapping("me/username")
    private void editUsername(@RequestBody @Validated EditUsernameRequest editUsernameRequest) {
        userService.editUsername(editUsernameRequest);
    }
}
