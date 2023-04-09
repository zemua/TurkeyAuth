package devs.mrp.springturkey.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import devs.mrp.springturkey.configuration.SecurityConfig;
import devs.mrp.springturkey.controllers.dtos.UserDto;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

	@Autowired
	private WebTestClient webClient;

	@Test
	void testCreateUser() throws JsonProcessingException, Exception {
		UserDto user = new UserDto("test@email.com");

		webClient.post()
		.uri("/user")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(user), UserDto.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody(UserDto.class)
		.isEqualTo(user);
	}

}
