package devs.mrp.springturkey.services.oauth.facade;

import reactor.core.publisher.Mono;

public interface UpdatePassFacade {

	public Mono<String> execute(Mono<String> email);

}
