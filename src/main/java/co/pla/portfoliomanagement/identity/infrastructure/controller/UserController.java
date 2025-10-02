package co.pla.portfoliomanagement.identity.infrastructure.controller;

import co.pla.portfoliomanagement.core.http.response.SuccessfulRequestResponseEntity;
import co.pla.portfoliomanagement.identity.application.dto.ChangePasswordByAdminDto;
import co.pla.portfoliomanagement.identity.application.dto.ChangePasswordDto;
import co.pla.portfoliomanagement.identity.application.dto.EditUserDto;
import co.pla.portfoliomanagement.identity.application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Object> updateUser(@RequestBody EditUserDto editUserDto) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(userService.editUser(editUserDto)));
    }

    @PutMapping("/admin/change-password")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> changePasswordByAdmin(@RequestBody ChangePasswordByAdminDto changePasswordByAdminDto) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(userService.changePasswordByAdmin(changePasswordByAdminDto)));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(userService.changePassword(changePasswordDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok("deleted");
    }
}
