package devs.mrp.springturkey.controllers;

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
import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.UnauthorizedException;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
@EnableAutoConfiguration
@Slf4j
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
	void errorCreatingUserFromClient() {
		UserDto user = new UserDto("test@email.com");

		Exception ex = new Exception("An error ocurred");
		when(createUserCase.createUser(ArgumentMatchers.any(Mono.class))).thenReturn(Mono.error(new ClientRequestException()));

		webClient.post()
		.uri("/user/create")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(user), UserDto.class)
		.exchange()
		.expectStatus().is4xxClientError();
	}

	@Test
	void errorCreatingUserFromServerCredentials() {
		UserDto user = new UserDto("test@email.com");

		Exception ex = new Exception("An error ocurred");
		when(createUserCase.createUser(ArgumentMatchers.any(Mono.class))).thenReturn(Mono.error(new UnauthorizedException()));

		webClient.post()
		.uri("/user/create")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(user), UserDto.class)
		.exchange()
		.expectStatus().is5xxServerError();
	}

}
