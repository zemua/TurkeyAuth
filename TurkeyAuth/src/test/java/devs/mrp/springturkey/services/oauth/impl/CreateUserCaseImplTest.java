package devs.mrp.springturkey.services.oauth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import devs.mrp.springturkey.controllers.dtos.UserDto;
import devs.mrp.springturkey.entities.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { CreateUserCaseImpl.class, AuthClientImpl.class })
class CreateUserCaseImplTest {

	private static MockWebServer mockWebServer;

	@MockBean
	private AuthClientImpl authClient;

	@Autowired
	private CreateUserCaseImpl userService;

	@BeforeAll
	static void setup() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@BeforeEach
	void initialize() {
		String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
		when(authClient.getClient()).thenReturn(Mono.just(WebClient.builder().baseUrl(baseUrl).build()));
	}

	@Test
	void testCreateUser() throws InterruptedException, JsonProcessingException {
		User user = User.builder().email("some@test.mail").username("some@test.mail").build();

		ObjectMapper objectMapper = new ObjectMapper();
		mockWebServer.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(new UserDto(user)))
				.addHeader("Content-Type", "application/json"));

		Mono<User> userMono = userService.createUser(Mono.just(user));

		StepVerifier.create(userMono)
		.expectNextMatches(resultUser -> resultUser.getEmail().equals("some@test.mail"))
		.verifyComplete();

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertEquals("POST", recordedRequest.getMethod());
		assertEquals("/auth/admin/realms/Turkey/users", recordedRequest.getPath());
		assertEquals("{\"email\":\"some@test.mail\",\"username\":\"some@test.mail\",\"enabled\":true}", recordedRequest.getBody().readUtf8());
	}

}
