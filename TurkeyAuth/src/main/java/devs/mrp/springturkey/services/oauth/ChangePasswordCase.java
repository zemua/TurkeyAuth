package devs.mrp.springturkey.services.oauth;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public interface ChangePasswordCase {

	public Mono<String> sendChangePassword(Mono<String> userId, WebClient client);

}
