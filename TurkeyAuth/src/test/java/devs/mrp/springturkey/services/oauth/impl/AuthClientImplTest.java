package devs.mrp.springturkey.services.oauth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.services.oauth.TokenRequestor;
import devs.mrp.springturkey.services.oauth.factory.BaseUrlProvider;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.core.publisher.Mono;

@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { AuthClientImpl.class })
@TestPropertySource(locations = "classpath:application.yml")
class AuthClientImplTest {

	private MockWebServer mockWebServer;
	@Autowired
	private AuthClientImpl authClient;
	@MockBean
	private BaseUrlProvider baseUrlProvider;
	@MockBean
	private TokenRequestor adminTokenRequestor;

	@BeforeEach
	public void setup() throws Exception {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		ReflectionTestUtils.setField(authClient, "authPort", mockWebServer.getPort());
	}

	@AfterEach
	public void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	void testCall() throws InterruptedException {
		mockWebServer.enqueue(new MockResponse().setBody("Hello world!"));
		when(adminTokenRequestor.getToken()).thenReturn(Mono.just("someToken"));
		when(baseUrlProvider.resolveBaseUrl()).thenReturn("http://localhost:" + mockWebServer.getPort());

		WebClient client = authClient.getClient().block();
		client.get()
		.uri("/path/to/endpoint")
		.retrieve()
		.bodyToMono(String.class)
		.block();

		RecordedRequest request = mockWebServer.takeRequest();
		assertEquals("GET", request.getMethod());
		assertEquals("/path/to/endpoint", request.getPath());
		assertEquals("application/json", request.getHeader("Content-Type"));
		assertEquals("Bearer someToken", request.getHeader("Authorization"));
	}

}
