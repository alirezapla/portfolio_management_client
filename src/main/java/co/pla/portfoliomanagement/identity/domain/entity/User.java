package co.pla.portfoliomanagement.identity.domain.entity;

import co.pla.portfoliomanagement.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_credential_id"))
    private Set<UserAuthority> userAuthorities = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String password, String role, Set<UserAuthority> userAuthorities) {
        this.username = username;
        this.email = email;
        this.passwordHash = password;
        this.userAuthorities = userAuthorities;
    }

    public void updateUser(String username, Set<UserAuthority> userAuthorities) {
        this.username = username;
        this.userAuthorities = userAuthorities;
    }

}