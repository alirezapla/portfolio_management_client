package co.pla.portfoliomanagement.gateway.infrastructure.security;

import co.pla.portfoliomanagement.gateway.infrastructure.util.JwtUtil;
import co.pla.portfoliomanagement.identity.infrastructure.security.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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
        try {
            final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (isPublicEndpoint(httpServletRequest)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            if (authenticationHeaderIsNotExist(authorizationHeader)) {
                handleException(httpServletResponse, "Missing or invalid JWT token", HttpStatus.UNAUTHORIZED);
                return;
            }
            String jwt = extractJWTFromAuthorizationHeader(authorizationHeader);
            String username = extractUsernameFromJwt(jwt);
            checkUserExistenceAndAuthenticate(httpServletRequest, username, jwt);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (ExpiredJwtException e) {
            handleException(httpServletResponse, "JWT token has expired", HttpStatus.UNAUTHORIZED);
            return;
        } catch (MalformedJwtException e) {
            handleException(httpServletResponse, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            return;
        }
    }

    private void handleException(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorDetails);
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

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth") ||
                path.equals("/v3/api-docs") ||
                path.startsWith("/swagger-ui/");
    }
}