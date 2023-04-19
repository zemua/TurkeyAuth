package devs.mrp.springturkey.services.oauth;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public interface VerifyEmailCase {

	public Mono<String> sendVerifyEmail(Mono<String> userId, WebClient client);

}
