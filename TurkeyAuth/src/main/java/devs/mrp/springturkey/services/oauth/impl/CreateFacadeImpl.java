package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import devs.mrp.springturkey.entities.User;
import devs.mrp.springturkey.services.oauth.CreateFacade;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import devs.mrp.springturkey.services.oauth.UserInfoCase;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CreateFacadeImpl implements CreateFacade {

	@Autowired
	private CreateUserCase createUserCase;
	@Autowired
	private UserInfoCase userInfoCase;

	@Override
	public Mono<User> execute(Mono<User> user) {
		return createUserCase.createUser(user)
				.doOnNext(u -> log.info("Created user: {}", u))
				.doOnNext(this::postCreateProcess);
	}

	private void postCreateProcess(User user) {
		userInfoCase.getUserInfo(Mono.just(user.getEmail()))
		.doOnNext(userInfoDto -> log.info("To send verification email for: {}", userInfoDto)) // TODO send verification email
		.subscribe();
	}

}
