package devs.mrp.springturkey.services.oauth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import devs.mrp.springturkey.services.oauth.dtos.UserInfoDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { UserInfoCaseImpl.class })
class UserInfoCaseImplTest {

	private static MockWebServer mockWebServer;

	private WebClient webClient;

	@Autowired
	private UserInfoCaseImpl userInfoService;

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
	void getUserInfoSuccess() throws JsonProcessingException, InterruptedException {
		UserInfoDto userInfo = UserInfoDto.builder()
				.id("asd-qwe-vsdf")
				.username(null)
				.enabled(true)
				.totp(false)
				.emailVerified(false)
				.email("some@email.com")
				.requiredActions(List.of("VERIFY_EMAIL"))
				.build();

		ObjectMapper objectMapper = new ObjectMapper();
		mockWebServer.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(userInfo))
				.addHeader("Content-Type", "application/json"));

		Mono<UserInfoDto> userInfoMono = userInfoService.getUserInfo(Mono.just("some@email.com"), webClient);

		StepVerifier.create(userInfoMono)
		.expectNextMatches(result -> result.equals(userInfo))
		.verifyComplete();

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertEquals("GET", recordedRequest.getMethod());
		assertEquals("/auth/admin/realms/Turkey/users?email=some@email.com", recordedRequest.getPath());
	}

}
