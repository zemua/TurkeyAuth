package devs.mrp.springturkey.services.oauth;

import reactor.core.publisher.Mono;

public interface TokenRequestor {
	public Mono<String> getToken();
}
