package devs.mrp.springturkey.services.oauth.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.services.oauth.AuthClient;
import devs.mrp.springturkey.services.oauth.UserInfoCase;
import devs.mrp.springturkey.services.oauth.VerifyEmailCase;
import devs.mrp.springturkey.services.oauth.facade.VerifyFacade;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class VerifyFacadeImpl implements VerifyFacade {

	@Autowired
	private UserInfoCase userInfoCase;
	@Autowired
	private VerifyEmailCase verifyEmailCase;
	@Autowired
	private AuthClient authClient;

	@Override
	public Mono<String> execute(Mono<String> email) {
		return authClient.getClient()
				.flatMap(client -> sendVerification(client, email));
	}

	private Mono<String> sendVerification(WebClient client, Mono<String> email) {
		return userInfoCase.getUserInfo(email, client)
				.doOnNext(userInfo -> log.info("To send verification email for: {}", userInfo))
				.flatMap(userInfo -> verifyEmailCase.sendVerifyEmail(Mono.just(userInfo.getId()), client));
	}

}
