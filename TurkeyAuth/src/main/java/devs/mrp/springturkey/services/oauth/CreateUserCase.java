package devs.mrp.springturkey.services.oauth;

import devs.mrp.springturkey.entities.User;
import reactor.core.publisher.Mono;

public interface CreateUserCase {

	public Mono<User> createUser(Mono<User> user);

}
