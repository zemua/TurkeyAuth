package devs.mrp.springturkey.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devs.mrp.springturkey.entities.User;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@PostMapping
	public Mono<ResponseEntity<User>> create(@RequestBody Mono<User> data) {
		return data.map(user -> ResponseEntity.ok(user));
	}

}
