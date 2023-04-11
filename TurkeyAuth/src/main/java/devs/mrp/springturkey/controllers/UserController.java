package devs.mrp.springturkey.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devs.mrp.springturkey.controllers.dtos.UserDto;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@PostMapping("/create")
	public Mono<ResponseEntity<UserDto>> create(@RequestBody Mono<UserDto> data) {
		return data.map(user -> ResponseEntity.ok(user));
	}

}
