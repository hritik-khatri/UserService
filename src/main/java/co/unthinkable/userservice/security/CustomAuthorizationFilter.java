package co.unthinkable.userservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private static final List<String> AUTH_WHITELIST = Arrays.asList(
            "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/v2/api-docs",
                    "/webjars/**",
                    "/swagger-ui/**"
    );
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(request.getServletPath());
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/token/refresh/**")
                || request.getServletPath().startsWith("/swagger-resources/")|| request.getServletPath().startsWith("/swagger-ui.html")
                || request.getServletPath().equals("/v2/api-docs") ||  request.getServletPath().startsWith( "/webjars/")
                || request.getServletPath().startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
        } else{
          String authorizationHeader = request.getHeader(AUTHORIZATION);
          if (authorizationHeader != null && !authorizationHeader.isEmpty() && authorizationHeader.startsWith("Bearer ")){
              try {
                  String token = authorizationHeader.substring("Bearer ".length());
                  Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                  JWTVerifier verifier = JWT.require(algorithm).build();
                  DecodedJWT decodedJWT = verifier.verify(token);
                  String username = decodedJWT.getSubject();
                  String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                  Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                  stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                  UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                  SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                  filterChain.doFilter(request, response);
              } catch (Exception exception) {
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("Error", exception.getMessage());
//                    response.sendError(FORBIDDEN.value
                  response.setStatus(FORBIDDEN.value());
                  Map<String, String> error = new HashMap<>();
                  error.put("error_message", exception.getMessage());
                  response.setContentType(APPLICATION_JSON_VALUE);
                  new ObjectMapper().writeValue(response.getOutputStream(), error);
              }
          } else {
              filterChain.doFilter(request,response);
          }
        }
    }
}
