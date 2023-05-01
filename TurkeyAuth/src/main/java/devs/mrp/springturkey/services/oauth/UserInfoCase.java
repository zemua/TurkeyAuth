package devs.mrp.springturkey.services.oauth;

import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.services.oauth.dtos.UserInfoDto;
import reactor.core.publisher.Mono;

public interface UserInfoCase {

	public Mono<UserInfoDto> getUserInfo(Mono<String> email, WebClient webClient);

}
