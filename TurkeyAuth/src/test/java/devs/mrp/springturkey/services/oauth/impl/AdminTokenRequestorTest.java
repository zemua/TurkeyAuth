package devs.mrp.springturkey.services.oauth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import devs.mrp.springturkey.exceptions.TokenRetrievalException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { AdminTokenRequestor.class })
@TestPropertySource(locations = "classpath:application.yml")
class AdminTokenRequestorTest {

	private MockWebServer mockWebServer;
	@Autowired
	private AdminTokenRequestor adminTokenRequestor;

	@BeforeEach
	public void setup() throws Exception {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		ReflectionTestUtils.setField(adminTokenRequestor, "authPort", mockWebServer.getPort());
	}

	@AfterEach
	public void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	void testCall() throws InterruptedException {
		mockWebServer.enqueue(new MockResponse().setBody(sampleResponseBody()).setHeader("Content-Type", "application/json"));

		String result = adminTokenRequestor.getToken().block();

		assertEquals("eyJhbGciOiJSUzI1Ni", result);

		RecordedRequest request = mockWebServer.takeRequest();
		assertEquals("POST", request.getMethod());
		assertEquals("/auth/realms/Turkey/protocol/openid-connect/token", request.getPath());
		assertEquals("application/x-www-form-urlencoded;charset=UTF-8", request.getHeader("Content-Type"));
		assertEquals("client_id=turkeyTest&client_secret=gYrhtCrDekDl0nkclhI1yjWN5dUqTbND&grant_type=client_credentials", request.getBody().readUtf8());
	}

	private String sampleResponseBody() {
		return "{\n"
				+ "    \"access_token\": \"eyJhbGciOiJSUzI1Ni\",\n"
				+ "    \"expires_in\": 300,\n"
				+ "    \"refresh_expires_in\": 0,\n"
				+ "    \"token_type\": \"Bearer\",\n"
				+ "    \"not-before-policy\": 0,\n"
				+ "    \"scope\": \"profile email\"\n"
				+ "}";
	}

	@Test
	void testExceptionOnNullToken() {
		mockWebServer.enqueue(new MockResponse().setBody(noTokenResponseBody()).setHeader("Content-Type", "application/json"));

		Mono<String> monoResult = adminTokenRequestor.getToken();

		StepVerifier.create(monoResult)
		.expectError(TokenRetrievalException.class)
		.verify();
	}

	private String noTokenResponseBody() {
		return "{\n"
				+ "    \"expires_in\": 300,\n"
				+ "    \"refresh_expires_in\": 0,\n"
				+ "    \"token_type\": \"Bearer\",\n"
				+ "    \"not-before-policy\": 0,\n"
				+ "    \"scope\": \"profile email\"\n"
				+ "}";
	}

}
