package devs.mrp.springturkey.services.oauth.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.exceptions.SendVerificationMailException;
import devs.mrp.springturkey.services.oauth.ChangePasswordCase;
import reactor.core.publisher.Mono;

@Service
public class ChangePasswordCaseImpl implements ChangePasswordCase {

	private static final String UPDATE_PASSWORD_COMMAND = "UPDATE_PASSWORD";

	@Value("${turkey.realm}")
	private String realm;

	@Override
	public Mono<String> sendChangePassword(Mono<String> userId, WebClient client) {
		return userId.flatMap(id -> sendRequest(client, id));
	}

	private Mono<String> sendRequest(WebClient client, String userId) {
		return client.put()
				.uri("/auth/admin/realms/" + realm + "/users/" + userId + "/execute-actions-email")
				.body(BodyInserters.fromValue(List.of(UPDATE_PASSWORD_COMMAND))) // TODO call from generic executeActionsEmail class, make EmailActionsFactory as component
				.<String>exchangeToMono(response -> handleResponse(response, userId));
	}

	private Mono<String> handleResponse(ClientResponse response, String userId) {
		if (response.statusCode().is2xxSuccessful()) {
			return Mono.just(userId);
		} else {
			return Mono.error(new SendVerificationMailException("Failed to send mail fo resetting password"));
		}
	}

}
