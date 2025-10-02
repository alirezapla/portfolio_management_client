package co.pla.portfoliomanagement.identity.application.dto;

import co.pla.portfoliomanagement.identity.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UsersDto {

    private final List<UserDto> users;
    private final Long total;

    public UsersDto(List<UserDto> users, Long total) {
        this.users = users;
        this.total = total;
    }

    public static UsersDto fromEntity(List<User> users, Long totalElements) {
        return new UsersDto(
                users
                        .stream()
                        .map(UserDto::fromEntity)
                        .collect(Collectors.toList()),
                totalElements);
    }
}
