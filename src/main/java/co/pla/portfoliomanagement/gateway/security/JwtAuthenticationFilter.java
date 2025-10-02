package co.pla.portfoliomanagement.gateway.security;

import co.pla.portfoliomanagement.gateway.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authenticationHeaderIsNotExist(authorizationHeader)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String jwt = extractJWTFromAuthorizationHeader(authorizationHeader);
        String username = extractUsernameFromJwt(jwt);
        checkUserExistenceAndAuthenticate(httpServletRequest, username, jwt);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean authenticationHeaderIsNotExist(String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith("Bearer ");
    }

    private String extractJWTFromAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private String extractUsernameFromJwt(String jwt) {
        return jwtUtil.extractUsername(jwt);
    }

    private void checkUserExistenceAndAuthenticate(HttpServletRequest httpServletRequest, String username, String jwt) {
        if (isUsernameExistAndSecurityContextIsNull(username)) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                authenticateUser(httpServletRequest, userDetails);
            }
        }
    }

    private void authenticateUser(HttpServletRequest httpServletRequest, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken jwtAuthentication =
                new JwtAuthentication(userDetails.getUsername(), null, userDetails.getAuthorities(), userDetails);
        jwtAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
    }

    private boolean isUsernameExistAndSecurityContextIsNull(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }


}