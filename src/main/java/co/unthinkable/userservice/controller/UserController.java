package co.unthinkable.userservice.controller;
import co.unthinkable.userservice.dto.RoleDto;
import co.unthinkable.userservice.dto.RoleToUser;
import co.unthinkable.userservice.dto.UserDto;
import co.unthinkable.userservice.dto.UserInfoDto;
import co.unthinkable.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@ApiOperation(value = "getGreeting", notes="get greeting",nickname = "getGreeting")
@RequestMapping(value = "/api")
@SecurityRequirement(name = "javainuseapi")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserInfoDto>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/users/save")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) throws URISyntaxException, ParseException {
        URI uri = new URI(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(userDto));
    }

    @PostMapping("/role/save")
    public ResponseEntity<RoleDto> saveRole(@RequestBody RoleDto roleDto) throws URISyntaxException, ParseException {
        URI uri = new URI(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(roleDto));
    }

    @PostMapping("/role/addRoleToUser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUser form) throws IOException {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request  , HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String issuer = request.getRequestURL().toString();
        Map<String, String> tokens = userService.refreshToken(authorizationHeader, issuer);
        if(tokens.containsKey("error_message")){
            response.setHeader("error", tokens.get("error_message"));
            response.setStatus(FORBIDDEN.value());
        }
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
    }

    @ApiOperation("Login.")
    @PostMapping(value = "/login",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> fakeLogin(@RequestParam String username, @RequestParam String password) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @ApiOperation("Logout.")
    @PostMapping("/logout")
    public void fakeLogout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }
}
