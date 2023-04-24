package devs.mrp.springturkey.services.oauth.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import devs.mrp.springturkey.controllers.dtos.TurkeyCredentialsDto;
import devs.mrp.springturkey.controllers.dtos.UserTokenDto;
import devs.mrp.springturkey.services.oauth.RequestForwarder;
import reactor.core.publisher.Mono;

@Service
@Qualifier("token")
public class TokenRequestForwarder implements RequestForwarder<TurkeyCredentialsDto, UserTokenDto> {

	@Override
	public Mono<ResponseEntity<UserTokenDto>> forward(Mono<TurkeyCredentialsDto> element) {
		// TODO Auto-generated method stub
		return null;
	}

}
