package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

import com.fasterxml.jackson.databind.ObjectMapper;

import devs.mrp.springturkey.entities.User;
import devs.mrp.springturkey.services.oauth.AuthClient;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import reactor.core.publisher.Mono;

@Service
public class CreateUserCaseImpl implements CreateUserCase {

	@Autowired
	private AuthClient authClient;

	@Value("${turkey.realm}")
	private String realm;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<User> createUser(Mono<User> user) {
		return user.flatMap(userValue -> sendRequest(userValue));
	}

	private Mono<User> sendRequest(User user) {
		return authClient.getClient()
				.post()
				.uri("/auth/admin/realms/" + realm + "/users")
				.body(BodyInserters.fromValue(user))
				.<User>exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return Mono.just(user);
					} else {
						return response.createException()
								.flatMap(Mono::error);
					}
				});
	}



}
