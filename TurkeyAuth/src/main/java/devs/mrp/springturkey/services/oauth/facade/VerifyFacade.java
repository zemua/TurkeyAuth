package devs.mrp.springturkey.services.oauth.facade;

import reactor.core.publisher.Mono;

public interface VerifyFacade {

	public Mono<String> execute(Mono<String> user);

}
