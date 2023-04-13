package devs.mrp.springturkey.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devs.mrp.springturkey.controllers.dtos.UserDto;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	private CreateUserCase createUserCase;

	@PostMapping("/create")
	public Mono<ResponseEntity<UserDto>> create(@RequestBody Mono<UserDto> data) {
		return createUserCase.createUser(data.map(userDto -> userDto.toUser()))
				.map(user -> ResponseEntity.ok(new UserDto(user)));
	}

}
