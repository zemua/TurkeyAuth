package devs.mrp.springturkey.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import devs.mrp.springturkey.configuration.SecurityConfig;
import devs.mrp.springturkey.controllers.dtos.UserRequest;
import devs.mrp.springturkey.controllers.dtos.UserResponse;
import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.KeycloakClientUnauthorizedException;
import devs.mrp.springturkey.services.oauth.CreateFacade;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
@AutoConfigureWebTestClient
@Slf4j
class UserControllerTest {

	@Autowired
	private WebTestClient webClient;
	@MockBean
	private CreateFacade createFacade;

	@Test
	void testCreateUser() throws JsonProcessingException, Exception {
		UserResponse response = new UserResponse("test@email.com");
		UserRequest request = new UserRequest("test@email.com", "mysecret");

		when(createFacade.execute(ArgumentMatchers.any())).thenReturn(Mono.just(request.toUser()));

		webClient.post()
		.uri("/user/create")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request), UserResponse.class)
		.exchange()
		.expectStatus().isEqualTo(201)
		.expectBody(UserResponse.class)
		.isEqualTo(response);

		verify(createFacade, times(1)).execute(ArgumentMatchers.any());
	}

	@Test
	void errorCreatingUserFromClient() {
		UserRequest request = new UserRequest("test@email.com", "mysecret");

		Exception ex = new Exception("An error ocurred");
		when(createFacade.execute(ArgumentMatchers.any(Mono.class))).thenReturn(Mono.error(new ClientRequestException()));

		webClient.post()
		.uri("/user/create")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request), UserResponse.class)
		.exchange()
		.expectStatus().is4xxClientError();
	}

	@Test
	void errorCreatingUserFromServerCredentials() {
		UserRequest request = new UserRequest("test@email.com", "mysecret");

		Exception ex = new Exception("An error ocurred");
		when(createFacade.execute(ArgumentMatchers.any(Mono.class))).thenReturn(Mono.error(new KeycloakClientUnauthorizedException()));

		webClient.post()
		.uri("/user/create")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request), UserResponse.class)
		.exchange()
		.expectStatus().is5xxServerError();
	}

}
