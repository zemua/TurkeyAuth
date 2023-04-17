package devs.mrp.springturkey.services.oauth;

import reactor.core.publisher.Mono;

public interface VerifyEmailCase {

	public Mono<String> sendVerifyEmail(Mono<String> userId);

}
