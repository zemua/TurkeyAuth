package devs.mrp.springturkey.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devs.mrp.springturkey.controllers.dtos.UserRequest;
import devs.mrp.springturkey.controllers.dtos.UserResponse;
import devs.mrp.springturkey.services.oauth.CreateFacade;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/user")
@Validated
@Slf4j
public class UserController {

	@Autowired
	private CreateFacade createFacade;

	@PostMapping("/create")
	public Mono<ResponseEntity<UserResponse>> create(@Valid @RequestBody Mono<UserRequest> data) {
		return createFacade.execute(data.map(userDto -> userDto.toUser()))
				.map(user -> ResponseEntity.status(201).body(new UserResponse(user)));
	}

}
