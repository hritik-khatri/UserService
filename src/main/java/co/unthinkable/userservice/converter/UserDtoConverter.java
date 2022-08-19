package co.unthinkable.userservice.converter;

import co.unthinkable.userservice.dto.UserDto;
import co.unthinkable.userservice.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;


public class UserDtoConverter {

@Autowired
private ModelMapper modelMapper;

    public UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    public User convertToEntity(UserDto userDto) throws ParseException {
        User user  = modelMapper.map(userDto, User.class);
        return user;
    }
}
