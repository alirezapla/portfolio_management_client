package co.pla.portfoliomanagement.identity.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class UserAuthority {

    @Enumerated(EnumType.STRING)
    @Column(name = "user_authority", nullable = false)
    private UserAuthorityType authorityType;

    public UserAuthority() {
    }

    public UserAuthorityType getAuthorityType() {
        return authorityType;
    }

    public UserAuthority(UserAuthorityType authorityType) {
        validate(authorityType);
        this.authorityType = authorityType;
    }

    private void validate(UserAuthorityType authorityType) {
        if (authorityType == null)
            throw new IllegalArgumentException("Check UserAuthority constructor arguments.");
    }

    @Override
    public String toString() {
        return "UserAuthority{" +
                "authorityType='" + authorityType + '\'' +
                '}';
    }
}