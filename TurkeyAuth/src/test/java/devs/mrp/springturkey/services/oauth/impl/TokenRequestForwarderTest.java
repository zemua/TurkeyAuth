package devs.mrp.springturkey.services.oauth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import devs.mrp.springturkey.controllers.dtos.ClientCredentialsDto;
import devs.mrp.springturkey.controllers.dtos.ClientTokenDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;

@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { TokenRequestForwarder.class })
class TokenRequestForwarderTest {

	private static MockWebServer mockWebServer;

	private WebClient webClient;

	@Autowired
	private ModelMapper modelMapper;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private TokenRequestForwarder requestForwarder;

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
		webClient = WebClient.builder().baseUrl(baseUrl).build();
	}

	@Test
	void testForward() throws InterruptedException, JsonProcessingException {
		char [] secret = {'s','o','m','e','s','e','c','r','e','t'};
		ClientCredentialsDto credentials = ClientCredentialsDto.builder()
				.clientId("someid")
				.clientSecret(secret)
				.grantType("some_grant_type")
				.build();

		char[] token = {'s','o','m','e','t','o','k','e','n'};
		ClientTokenDto response = ClientTokenDto.builder()
				.accessToken(token)
				.expiresIn(123)
				.refreshTokenExpiresIn(321)
				.tokenType("some_type")
				.notBeforePolicy(3)
				.scope("some scope")
				.build();

		mockWebServer.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(response))
				.setResponseCode(200));

		var result = requestForwarder.forward(Mono.just(credentials), Object.class).block();

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertEquals("POST", recordedRequest.getMethod());
		assertEquals("/auth/realms/Turkey/protocol/openid-connect/token", recordedRequest.getPath());
		assertEquals("[\"UPDATE_PASSWORD\"]", recordedRequest.getBody().readUtf8());

		ClientTokenDto tokenDto = modelMapper.map(result, ClientTokenDto.class);
		assertEquals(token, tokenDto.getAccessToken());
		assertEquals(123, tokenDto.getExpiresIn());
		assertEquals(321, tokenDto.getRefreshTokenExpiresIn());
		assertEquals("some_type", tokenDto.getTokenType());
		assertEquals(3, tokenDto.getNotBeforePolicy());
		assertEquals("some scope", tokenDto.getScope());
	}

}
