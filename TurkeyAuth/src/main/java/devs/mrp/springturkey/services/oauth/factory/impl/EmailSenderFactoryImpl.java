package devs.mrp.springturkey.services.oauth.factory.impl;

import org.springframework.stereotype.Component;

import devs.mrp.springturkey.services.oauth.factory.EmailSenderFactory;
import devs.mrp.springturkey.services.oauth.factory.SendEmailCase;
import reactor.core.publisher.Mono;

@Component
public class EmailSenderFactoryImpl implements EmailSenderFactory {

	@Override
	public SendEmailCase get(Mono<String> userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
