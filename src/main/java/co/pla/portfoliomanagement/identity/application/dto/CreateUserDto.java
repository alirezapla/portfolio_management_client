package co.pla.portfoliomanagement.identity.application.dto;

import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthorityType;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthority;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreateUserDto {
    private String username;
    private String password;
    private String email;
    private Set<String> authorities;

    public CreateUserDto() {
    }

    public CreateUserDto(String username, String password, String email, Set<String> authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public CreateUserDto(SignupDto signupRequest, Set<String> authorities) {
        this(signupRequest.username(), signupRequest.password(), signupRequest.email(), authorities);
    }

    public User toEntity() {
        return new User(
                this.username,
                this.email,
                this.password,
                this.authorities
                        .stream()
                        .map(UserAuthorityType::getByUserAuthorityTitle)
                        .map(UserAuthority::new)
                        .collect(Collectors.toSet())
        );
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
