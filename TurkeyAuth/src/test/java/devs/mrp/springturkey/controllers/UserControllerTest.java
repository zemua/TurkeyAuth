package devs.mrp.springturkey.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.fasterxml.jackson.core.JsonProcessingException;

import devs.mrp.springturkey.controllers.dtos.EmailEntity;
import devs.mrp.springturkey.controllers.dtos.UserRequest;
import devs.mrp.springturkey.controllers.dtos.UserResponse;
import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.KeycloakClientUnauthorizedException;
import devs.mrp.springturkey.services.oauth.facade.CreateFacade;
import devs.mrp.springturkey.services.oauth.facade.UpdatePassFacade;
import devs.mrp.springturkey.services.oauth.facade.VerifyFacade;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerTest {

	@Autowired
	private WebTestClient webClient;
	@MockBean
	private CreateFacade createFacade;
	@MockBean
	private VerifyFacade verifyFacade;
	@MockBean
	private UpdatePassFacade updatePassFacade;

	@Test
	@WithMockUser(authorities = "SCOPE_create_user")
	void testCreateUser() throws JsonProcessingException, Exception {
		UserResponse response = new UserResponse("test@email.com");
		char[] secret = {'m','y','s','e','c','r','e','t'};
		UserRequest request = new UserRequest("test@email.com", secret);

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
	@WithMockUser(authorities = "SCOPE_create_user")
	void errorCreatingUserFromClient() {
		char[] secret = {'m','y','s','e','c','r','e','t'};
		UserRequest request = new UserRequest("test@email.com", secret);

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
	@WithMockUser(authorities = "SCOPE_create_user")
	void errorCreatingUserFromServerCredentials() {
		char[] secret = {'m','y','s','e','c','r','e','t'};
		UserRequest request = new UserRequest("test@email.com", secret);

		Exception ex = new Exception("An error ocurred");
		when(createFacade.execute(ArgumentMatchers.any(Mono.class))).thenReturn(Mono.error(new KeycloakClientUnauthorizedException()));

		webClient.post()
		.uri("/user/create")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request), UserResponse.class)
		.exchange()
		.expectStatus().is5xxServerError();
	}

	@Test
	@WithMockUser(authorities = "SCOPE_send_verify")
	void sendVerifyEmail() {
		EmailEntity email = new EmailEntity("test@email.com");

		when(verifyFacade.execute(ArgumentMatchers.any())).thenReturn(Mono.just("someid"));

		webClient.put()
		.uri("/user/verify")
		.contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(email))
		.exchange()
		.expectStatus().isEqualTo(201)
		.expectBody(String.class)
		.isEqualTo("someid");

		verify(verifyFacade, times(1)).execute(ArgumentMatchers.any());
	}

	@Test
	@WithMockUser(authorities = "SCOPE_send_update_password")
	void sendUpdatePassword() {
		EmailEntity email = new EmailEntity("test@email.com");

		when(updatePassFacade.execute(ArgumentMatchers.any())).thenReturn(Mono.just("someid"));

		webClient.put()
		.uri("/user/password")
		.contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(email))
		.exchange()
		.expectStatus().isEqualTo(200)
		.expectBody(String.class)
		.isEqualTo("someid");

		verify(updatePassFacade, times(1)).execute(ArgumentMatchers.any());
	}

	@Test
	@WithMockUser
	void testNoAuthorityCreateFails() throws JsonProcessingException, Exception {
		UserResponse response = new UserResponse("test@email.com");
		char[] secret = {'m','y','s','e','c','r','e','t'};
		UserRequest request = new UserRequest("test@email.com", secret);

		webClient.post()
		.uri("/user/create")
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request), UserResponse.class)
		.exchange()
		.expectStatus().isEqualTo(401);
	}

	@Test
	@WithMockUser
	void testNoAuthorityVerifyEmailFails() {
		EmailEntity email = new EmailEntity("test@email.com");

		webClient.put()
		.uri("/user/verify")
		.contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(email))
		.exchange()
		.expectStatus().isEqualTo(401);
	}

	@Test
	@WithMockUser
	void testNoAuthoritySendUpdatePassword() {
		EmailEntity email = new EmailEntity("test@email.com");

		webClient.put()
		.uri("/user/password")
		.contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(email))
		.exchange()
		.expectStatus().isEqualTo(401);
	}

}
