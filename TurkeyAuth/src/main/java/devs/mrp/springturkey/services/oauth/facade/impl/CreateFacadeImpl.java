package devs.mrp.springturkey.services.oauth.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import devs.mrp.springturkey.entities.User;
import devs.mrp.springturkey.services.oauth.AuthClient;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import devs.mrp.springturkey.services.oauth.UserInfoCase;
import devs.mrp.springturkey.services.oauth.VerifyEmailCase;
import devs.mrp.springturkey.services.oauth.facade.CreateFacade;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CreateFacadeImpl implements CreateFacade {

	@Autowired
	private CreateUserCase createUserCase;
	@Autowired
	private UserInfoCase userInfoCase;
	@Autowired
	private VerifyEmailCase verifyEmailCase;
	@Autowired
	private AuthClient authClient;

	@Override
	public Mono<User> execute(Mono<User> user) {
		return authClient.getClient()
				.flatMap(client -> createUser(client, user));

	}

	private Mono<User> createUser(WebClient client, Mono<User> user) {
		return createUserCase.createUser(user, client)
				.doOnNext(u -> log.info("Created user: {}", u))
				.doOnNext(u -> postCreateProcess(u, client));
	}

	private void postCreateProcess(User user, WebClient client) {
		userInfoCase.getUserInfo(Mono.just(user.getEmail()), client)
		.doOnNext(userInfoDto -> log.info("To send verification email for: {}", userInfoDto))
		.flatMap(userInfo -> verifyEmailCase.sendVerifyEmail(Mono.just(userInfo.getId()), client))
		.subscribe();
	}

}
