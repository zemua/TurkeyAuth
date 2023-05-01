package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.exceptions.SendVerificationMailException;
import devs.mrp.springturkey.services.oauth.VerifyEmailCase;
import reactor.core.publisher.Mono;

@Service
public class VerifyEmailCaseImpl implements VerifyEmailCase {

	@Value("${turkey.realm}")
	private String realm;

	@Override
	public Mono<String> sendVerifyEmail(Mono<String> userId, WebClient client) {
		return userId.flatMap(id -> sendRequest(client, id));
	}

	private Mono<String> sendRequest(WebClient client, String userId) {
		return client.put()
				.uri("/auth/admin/realms/" + realm + "/users/" + userId + "/send-verify-email")
				.<String>exchangeToMono(response -> handleResponse(response, userId));
	}

	private Mono<String> handleResponse(ClientResponse response, String userId) {
		if (response.statusCode().is2xxSuccessful()) {
			return Mono.just(userId);
		} else {
			return Mono.error(new SendVerificationMailException("Failed to send email for email verification"));
		}
	}

}
