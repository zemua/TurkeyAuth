package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.entities.User;
import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.KeycloakClientUnauthorizedException;
import devs.mrp.springturkey.exceptions.TurkeyGenericException;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import devs.mrp.springturkey.services.oauth.dtos.CreateUserDto;
import reactor.core.publisher.Mono;

@Service
public class CreateUserCaseImpl implements CreateUserCase {

	@Value("${turkey.realm}")
	private String realm;

	@Override
	public Mono<User> createUser(Mono<User> user, WebClient webClient) {
		return user.map(CreateUserDto::new)
				.flatMap(u -> sendRequest(webClient, u));
	}

	private Mono<User> sendRequest(WebClient client, CreateUserDto user) {
		return client.post()
				.uri("/auth/admin/realms/" + realm + "/users")
				.body(BodyInserters.fromValue(user))
				.<User>exchangeToMono(response -> handleResponse(response, user.toUserWithoutSecret()));
	}

	private Mono<User> handleResponse(ClientResponse response, User user) {
		if (response.statusCode().is2xxSuccessful()) {
			return Mono.just(user);
		} else if (response.statusCode().value() == 401) {
			return Mono.error(new KeycloakClientUnauthorizedException());
		} else if (response.statusCode().is4xxClientError()) {
			return Mono.error(new ClientRequestException(response.statusCode()));
		} else {
			return response.createException()
					.flatMap(ex -> Mono.error(new TurkeyGenericException(ex)));
		}
	}

}
