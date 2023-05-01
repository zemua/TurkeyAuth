package devs.mrp.springturkey.services.oauth.impl;

import java.util.List;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.exceptions.SendVerificationMailException;
import devs.mrp.springturkey.services.oauth.SendEmailCase;
import reactor.core.publisher.Mono;

public class SendEmailCaseImpl implements SendEmailCase {

	private EmailCommand command;
	private String realm;

	public SendEmailCaseImpl(String realm, EmailCommand command) {
		this.command = command;
		this.realm = realm;
	}

	@Override
	public Mono<String> execute(Mono<String> userId, WebClient client) {
		return userId.flatMap(id -> sendRequest(client, id));
	}

	private Mono<String> sendRequest(WebClient client, String userId) {
		return client.put()
				.uri("/auth/admin/realms/" + realm + "/users/" + userId + "/execute-actions-email")
				.body(BodyInserters.fromValue(List.of(command)))
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
