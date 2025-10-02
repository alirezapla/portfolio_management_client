package co.pla.portfoliomanagement.identity.domain.entity;

import co.pla.portfoliomanagement.core.exceptions.ExceptionMessages;
import lombok.Getter;

@Getter
public enum UserAuthorityType {
    AUTHORITY_ADMIN(0, "AUTHORITY_ADMIN"),
    AUTHORITY_USER(1, "AUTHORITY_USER");
    private final int index;
    private final String title;

    UserAuthorityType(int index, String title) {
        this.index = index;
        this.title = title;
    }

    public static UserAuthorityType getByUserAuthorityTitle(String userAuthorityTypeTitle) {
        for (UserAuthorityType userAuthorityType : UserAuthorityType.values()) {
            if (userAuthorityType.getTitle().equals(userAuthorityTypeTitle))
                return userAuthorityType;
        }
        throw new RuntimeException(ExceptionMessages.EXCEPTION.getTitle());
    }
}