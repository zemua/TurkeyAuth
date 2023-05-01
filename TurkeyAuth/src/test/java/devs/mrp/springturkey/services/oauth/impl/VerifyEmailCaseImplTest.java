package devs.mrp.springturkey.services.oauth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { VerifyEmailCaseImpl.class })
class VerifyEmailCaseImplTest {

	private static MockWebServer mockWebServer;

	private WebClient webClient;

	@Autowired
	private VerifyEmailCaseImpl verifyEmailCase;

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
	void test() throws InterruptedException {
		String userId = "someId";

		mockWebServer.enqueue(new MockResponse()
				.setResponseCode(204));

		Mono<String> monoResult = verifyEmailCase.sendVerifyEmail(Mono.just(userId), webClient);

		StepVerifier.create(monoResult)
		.expectNextMatches(r -> r.equals(userId))
		.verifyComplete();

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertEquals("PUT", recordedRequest.getMethod());
		assertEquals("/auth/admin/realms/Turkey/users/" + userId + "/send-verify-email", recordedRequest.getPath());
	}

}
