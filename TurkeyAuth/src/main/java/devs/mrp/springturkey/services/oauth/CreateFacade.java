package devs.mrp.springturkey.services.oauth;

import devs.mrp.springturkey.entities.User;
import reactor.core.publisher.Mono;

public interface CreateFacade {

	public Mono<User> execute(Mono<User> user);

}
