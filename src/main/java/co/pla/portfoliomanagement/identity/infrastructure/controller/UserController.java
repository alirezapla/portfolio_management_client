package co.pla.portfoliomanagement.identity.infrastructure.controller;

import co.pla.portfoliomanagement.core.http.response.SuccessfulRequestResponseEntity;
import co.pla.portfoliomanagement.core.logging.AppLogEvent;
import co.pla.portfoliomanagement.identity.application.dto.ChangePasswordByAdminDTO;
import co.pla.portfoliomanagement.identity.application.dto.ChangePasswordDTO;
import co.pla.portfoliomanagement.identity.application.dto.EditUserDTO;
import co.pla.portfoliomanagement.identity.application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(userService.get(id)));
    }

    @GetMapping()
    public ResponseEntity<Object> getUsers(@RequestParam int page, @RequestParam int perPage) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(userService.findAll(page, perPage)));
    }

    @PutMapping()
    public ResponseEntity<Object> updateUser(@RequestBody EditUserDTO editUserDTO) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(userService.editUser(editUserDTO)));
    }

    @PutMapping("/admin/change-password")
    public ResponseEntity<Object> changePasswordByAdmin(@RequestBody ChangePasswordByAdminDTO changePasswordByAdminDTO) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(userService.changePasswordByAdmin(changePasswordByAdminDTO)));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(userService.changePassword(changePasswordDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok("deleted");
    }
}
