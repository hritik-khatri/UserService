package co.unthinkable.userservice.converter;

import co.unthinkable.userservice.dto.UserDto;
import co.unthinkable.userservice.dto.UserInfoDto;
import co.unthinkable.userservice.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

public class UserInfoDtoConverter {

    @Autowired
    private ModelMapper modelMapper;

    public UserInfoDto convertToDto(User user) {
        UserInfoDto userInfoDto = modelMapper.map(user, UserInfoDto.class);
        return userInfoDto;
    }

    public User convertToEntity(UserInfoDto userInfoDto) throws ParseException {
        User user  = modelMapper.map(userInfoDto, User.class);
        return user;
    }
}
