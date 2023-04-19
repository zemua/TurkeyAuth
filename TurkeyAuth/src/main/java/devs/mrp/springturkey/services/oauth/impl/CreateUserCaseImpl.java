package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import devs.mrp.springturkey.entities.User;
import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.KeycloakClientUnauthorizedException;
import devs.mrp.springturkey.exceptions.TurkeyGenericException;
import devs.mrp.springturkey.services.oauth.AuthClient;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import devs.mrp.springturkey.services.oauth.dtos.CreateUserDto;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CreateUserCaseImpl implements CreateUserCase {

	@Autowired
	private AuthClient authClient;

	@Value("${turkey.realm}")
	private String realm;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<User> createUser(Mono<User> user) {
		return user.map(CreateUserDto::new)
				.flatMap(this::execute);
	}

	private Mono<User> execute(CreateUserDto user) {
		return authClient.getClient()
				.flatMap(client -> sendRequest(client, user));
	}

	private Mono<User> sendRequest(WebClient client, CreateUserDto user) {
		return client.post()
				.uri("/auth/admin/realms/" + realm + "/users")
				.body(BodyInserters.fromValue(user))
				.<User>exchangeToMono(response -> handleResponse(response, user.toUser()));
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
