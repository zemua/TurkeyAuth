package devs.mrp.springturkey.services.oauth;

import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.entities.User;
import reactor.core.publisher.Mono;

public interface CreateUserCase {

	public Mono<User> createUser(Mono<User> user, WebClient webClient);

}
