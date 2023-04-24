package devs.mrp.springturkey.services.oauth;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public interface SendEmailCase {

	public Mono<String> execute(Mono<String> userId, WebClient client);

}
