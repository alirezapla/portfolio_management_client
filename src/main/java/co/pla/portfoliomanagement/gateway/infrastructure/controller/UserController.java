package co.pla.portfoliomanagement.gateway.infrastructure.controller;

import co.pla.portfoliomanagement.gateway.infrastructure.util.response.SuccessfulResponseEntity;
import co.pla.portfoliomanagement.identity.application.dto.ChangePasswordByAdminDto;
import co.pla.portfoliomanagement.identity.application.dto.ChangePasswordDto;
import co.pla.portfoliomanagement.identity.application.dto.EditUserDto;
import co.pla.portfoliomanagement.identity.application.facade.UserFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "UserController", description = "User tag")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(userFacade.getUserByUid(id)));
    }

    @GetMapping()
    public ResponseEntity<Object> getUsers(@RequestParam int page, @RequestParam int perPage) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(userFacade.getUsers(page, perPage)));
    }

    @PutMapping()
    public ResponseEntity<Object> updateUser(@RequestBody EditUserDto editUserDto) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(userFacade.updateUser(editUserDto)));
    }

    @PutMapping("/admin/change-password")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> changePasswordByAdmin(@RequestBody ChangePasswordByAdminDto changePasswordByAdminDto) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(userFacade.changePasswordByAdmin(changePasswordByAdminDto)));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(userFacade.changePassword(changePasswordDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        userFacade.deleteUserByUid(id);
        return ResponseEntity.ok("deleted");
    }
}
