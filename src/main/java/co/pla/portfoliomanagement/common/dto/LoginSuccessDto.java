package co.pla.portfoliomanagement.common.dto;

import co.pla.portfoliomanagement.identity.domain.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class LoginSuccessDto {
    public String jwt;
    private Set<String> userAccountAuthorities;

    public LoginSuccessDto() {
    }

    public LoginSuccessDto(String jwt, Set<String> userAccountAuthorities) {
        this.jwt = jwt;
        this.userAccountAuthorities = userAccountAuthorities;
    }

    public static LoginSuccessDto mapFromUserCredentials(User user, String generateToken) {
        return new LoginSuccessDto(
                generateToken,
                user.getUserAuthorities()
                        .stream()
                        .map(us -> us.getAuthorityType().getTitle())
                        .collect(Collectors.toSet())
        );
    }
}
