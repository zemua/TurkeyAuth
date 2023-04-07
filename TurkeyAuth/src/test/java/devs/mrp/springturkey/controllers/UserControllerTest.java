package devs.mrp.springturkey.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import devs.mrp.springturkey.entities.User;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = UserController.class)
class UserControllerTest {

	@Autowired
	private WebTestClient webClient;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void testCreateUser() throws JsonProcessingException, Exception {
		User user = new User("test@email.com");

		webClient.post()
		.uri("/user")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(user), User.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody(User.class)
		.isEqualTo(user);
	}

}
