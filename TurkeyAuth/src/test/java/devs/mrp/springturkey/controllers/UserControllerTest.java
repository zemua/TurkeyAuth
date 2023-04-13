package devs.mrp.springturkey.controllers;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import devs.mrp.springturkey.configuration.SecurityConfig;
import devs.mrp.springturkey.controllers.dtos.UserDto;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
@EnableAutoConfiguration
class UserControllerTest {

	@Autowired
	private WebTestClient webClient;
	@MockBean
	private CreateUserCase createUserCase;

	@Test
	void testCreateUser() throws JsonProcessingException, Exception {
		UserDto user = new UserDto("test@email.com");

		when(createUserCase.createUser(ArgumentMatchers.any(Mono.class))).thenReturn(Mono.just(user.toUser()));

		webClient.post()
		.uri("/user/create")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(user), UserDto.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody(UserDto.class)
		.isEqualTo(user);
	}

	@Test
	void errorCreatingUser() {
		fail();
	}

}
