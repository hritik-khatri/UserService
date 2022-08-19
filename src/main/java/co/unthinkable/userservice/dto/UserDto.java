package co.unthinkable.userservice.dto;

import co.unthinkable.userservice.entity.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String name;
    private String password;
    private Collection<RoleDto> roles = new ArrayList<>();

}

