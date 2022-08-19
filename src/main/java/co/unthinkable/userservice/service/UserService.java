package co.unthinkable.userservice.service;

import co.unthinkable.userservice.dto.RoleDto;
import co.unthinkable.userservice.dto.UserDto;
import co.unthinkable.userservice.dto.UserInfoDto;
import co.unthinkable.userservice.entity.Role;
import co.unthinkable.userservice.entity.User;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto saveUser(UserDto userDto) throws ParseException;
    RoleDto saveRole(RoleDto roleDto) throws ParseException;
    void addRoleToUser(String username, String roleName);
    UserDto getUser(String username);
    List<UserInfoDto> getUsers();
    Map<String, String> refreshToken(String authorizationHeader, String issuer);

}
