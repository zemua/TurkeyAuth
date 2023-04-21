package devs.mrp.springturkey.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devs.mrp.springturkey.controllers.dtos.EmailEntity;
import devs.mrp.springturkey.controllers.dtos.UserRequest;
import devs.mrp.springturkey.controllers.dtos.UserResponse;
import devs.mrp.springturkey.services.oauth.facade.CreateFacade;
import devs.mrp.springturkey.services.oauth.facade.VerifyFacade;
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
	@Autowired
	private VerifyFacade verifyFacade;

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('create_user')")
	public Mono<ResponseEntity<UserResponse>> create(@Valid @RequestBody Mono<UserRequest> data) {
		return createFacade.execute(data.map(userDto -> userDto.toUser()))
				.map(user -> ResponseEntity.status(201).body(new UserResponse(user)));
	}

	@PutMapping("/verify")
	@PreAuthorize("hasAuthority('send_verify')")
	public Mono<ResponseEntity<String>> verify(@Valid @RequestBody EmailEntity email) {
		return verifyFacade.execute(Mono.just(email.getEmail()))
				.map(mail -> ResponseEntity.status(201).body(mail));
	}

}
