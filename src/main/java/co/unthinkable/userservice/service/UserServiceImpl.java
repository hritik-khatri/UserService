package co.unthinkable.userservice.service;

import co.unthinkable.userservice.converter.RoleDtoConverter;
import co.unthinkable.userservice.converter.UserDtoConverter;
import co.unthinkable.userservice.converter.UserInfoDtoConverter;
import co.unthinkable.userservice.dto.RoleDto;
import co.unthinkable.userservice.dto.UserDto;
import co.unthinkable.userservice.dto.UserInfoDto;
import co.unthinkable.userservice.entity.Role;
import co.unthinkable.userservice.entity.User;
import co.unthinkable.userservice.repository.RoleRepository;
import co.unthinkable.userservice.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoConverter userDtoConverter;
    private final RoleDtoConverter roleDtoConverter;
    private final UserInfoDtoConverter userInfoDtoConverter;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user == null){
            log.info("User not found: " + username);
            throw new UsernameNotFoundException("User not found");
        }
        else{
            log.info("User found: " + user.getUsername());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().stream().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public UserDto saveUser(UserDto userDto) throws ParseException {
        log.info("Saving new user");
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userDtoConverter.convertToDto(userRepository.save(userDtoConverter.convertToEntity(userDto)));
    }

    @Override
    public RoleDto saveRole(RoleDto roleDto) throws ParseException {
        log.info("Saving new role - " + roleDto.getName());
        return roleDtoConverter.convertToDto(roleRepository.save(roleDtoConverter.convertToEntity(roleDto)));
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding new role to user {}", username);
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public UserDto getUser(String username) {
        log.info("Retrieving user with username {}", username);
        return userDtoConverter.convertToDto(userRepository.findByUsername(username));
    }

    @Override
    public List<UserInfoDto> getUsers() {
        log.info("Finding all users");
        return userRepository.findAll().stream().map(userInfoDtoConverter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Map<String,String> refreshToken(String authorizationHeader, String issuer){
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userDtoConverter.convertToEntity(getUser(username));
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+ 10*60*1000))
                        .withIssuer(issuer)
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                return tokens;
                }
            catch (Exception e){
                log.error("Error logging in :{} ", e.getMessage());
                Map<String, String> error  = new HashMap<>();
                error.put("error_message", e.getMessage());
                return error;
            }
        }
        else{
            throw new RuntimeException("Refresh token is invalid/missing");
        }
    }
}
