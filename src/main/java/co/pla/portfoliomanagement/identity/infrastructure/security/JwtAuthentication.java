package co.pla.portfoliomanagement.identity.infrastructure.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthentication extends UsernamePasswordAuthenticationToken {

    private CustomUserDetails userDetails;

    public JwtAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public JwtAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, UserDetails userDetails) {
        super(principal, credentials, authorities);
        this.userDetails = (CustomUserDetails) userDetails;
    }

    public CustomUserDetails getUserDetails() {
        return userDetails;
    }
}