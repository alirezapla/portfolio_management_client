package co.pla.portfoliomanagement.identity.application.dto;

import co.pla.portfoliomanagement.identity.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UsersDTO {

    private final List<UserDTO> users;
    private final Long total;

    public UsersDTO(List<UserDTO> users, Long total) {
        this.users = users;
        this.total = total;
    }

    public static UsersDTO fromEntity(List<User> users, Long totalElements) {
        return new UsersDTO(
                users
                        .stream()
                        .map(UserDTO::fromEntity)
                        .collect(Collectors.toList()),
                totalElements);
    }
}
