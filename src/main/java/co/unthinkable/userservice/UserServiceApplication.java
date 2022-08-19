package co.unthinkable.userservice;

import co.unthinkable.userservice.converter.RoleDtoConverter;
import co.unthinkable.userservice.converter.UserDtoConverter;
import co.unthinkable.userservice.converter.UserInfoDtoConverter;
import co.unthinkable.userservice.dto.RoleDto;
import co.unthinkable.userservice.dto.UserDto;
import co.unthinkable.userservice.service.UserService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.util.ArrayList;



@SpringBootApplication
@SecurityScheme(name = "javainuseapi", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	 ModelMapper modelMapper() {return new ModelMapper();}

	@Bean
	RoleDtoConverter roleDtoConverter() {return new RoleDtoConverter();}

	@Bean
	UserDtoConverter userDtoConverter() {return new UserDtoConverter();}

	@Bean
	UserInfoDtoConverter userInfoDtoConverter() {return new UserInfoDtoConverter();}


	@Bean
	CommandLineRunner run(UserService userService) {
		return args ->{
			userService.saveRole(new RoleDto(null, "ROLE_USER"));
			userService.saveRole(new RoleDto(null, "ROLE_ADMIN"));
			userService.saveRole(new RoleDto(null, "ROLE_MANAGER"));
			userService.saveRole(new RoleDto(null, "ROLE_SUPER_ADMIN"));

			userService.saveUser(new UserDto(null, "hritik.khatri", "Hritik khatri","1234", new ArrayList<>()));
			userService.saveUser(new UserDto(null, "vatsal.gupta", "Vatsal gupta","1234", new ArrayList<>()));
			userService.saveUser(new UserDto(null, "bob.baggins", "Bob baggins","1234", new ArrayList<>()));

			userService.addRoleToUser("hritik.khatri","ROLE_SUPER_ADMIN");
			userService.addRoleToUser("hritik.khatri","ROLE_ADMIN");
			userService.addRoleToUser("hritik.khatri","ROLE_USER");
			userService.addRoleToUser("vatsal.gupta","ROLE_MANAGER");
			userService.addRoleToUser("bob.baggins", "ROLE_USER");

		};

	}

}
