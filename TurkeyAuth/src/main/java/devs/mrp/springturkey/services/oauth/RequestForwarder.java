package devs.mrp.springturkey.services.oauth;

import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;

public interface RequestForwarder<I, O> {

	public Mono<ResponseEntity<O>> forward(Mono<I> element);

}
