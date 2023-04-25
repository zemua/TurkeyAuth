package devs.mrp.springturkey.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devs.mrp.springturkey.controllers.dtos.RefreshCredentialsDto;
import devs.mrp.springturkey.controllers.dtos.UserCredentialsDto;
import devs.mrp.springturkey.controllers.dtos.UserTokenDto;
import devs.mrp.springturkey.services.oauth.RequestForwarder;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/user")
@Validated
public class UserController {

	@Autowired
	@Qualifier("token")
	private RequestForwarder<UserCredentialsDto, UserTokenDto> tokenRequestForwarder;

	@Autowired
	@Qualifier("token")
	private RequestForwarder<RefreshCredentialsDto, UserTokenDto> refreshRequestForwarder;

	@PostMapping("/token")
	public Mono<ResponseEntity<UserTokenDto>> forwardToToken(@Valid @RequestBody Mono<UserCredentialsDto> credentials) {
		return tokenRequestForwarder.forward(credentials, UserTokenDto.class);
	}

	@PostMapping("/refresh")
	public Mono<ResponseEntity<UserTokenDto>> forwardToRefresh(@Valid @RequestBody Mono<RefreshCredentialsDto> credentials) {
		return refreshRequestForwarder.forward(credentials, UserTokenDto.class);
	}

}
