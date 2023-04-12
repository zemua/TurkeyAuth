package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import devs.mrp.springturkey.entities.User;
import devs.mrp.springturkey.services.oauth.AuthClient;
import devs.mrp.springturkey.services.oauth.CreateUserCase;
import reactor.core.publisher.Mono;

@Service
public class CreateUserCaseImpl implements CreateUserCase {

	@Autowired
	private AuthClient authClient;

	@Override
	public Mono<User> createUser(Mono<User> user) {
		// TODO Auto-generated method stub
		return null;
	}

}
