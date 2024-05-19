package cz.cvut.fit.sabirdan.wework.controller.user;

import cz.cvut.fit.sabirdan.wework.http.request.UpdateUserRequest;
import cz.cvut.fit.sabirdan.wework.http.response.user.SafeUserDTO;
import cz.cvut.fit.sabirdan.wework.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    private ResponseEntity<SafeUserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(new SafeUserDTO(userService.getById(id)));
    }

    @PutMapping("{id}")
    private void updateUser(@PathVariable Long id, @RequestBody @Validated UpdateUserRequest updateUserRequest) {
        userService.updateUserById(id, updateUserRequest);
    }
}
