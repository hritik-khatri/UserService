package co.unthinkable.userservice.converter;

import co.unthinkable.userservice.dto.RoleDto;
import co.unthinkable.userservice.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

public class RoleDtoConverter {
    @Autowired
    private ModelMapper modelMapper;

    public RoleDto convertToDto(Role role) {
        RoleDto roleDto = modelMapper.map(role, RoleDto.class);
        return roleDto;
    }

    public Role convertToEntity(RoleDto roleDto) throws ParseException {
        Role role = modelMapper.map(roleDto, Role.class);
        return role;
    }
}