package devs.mrp.springturkey.services.oauth.factory.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import devs.mrp.springturkey.services.oauth.SendEmailCase;
import devs.mrp.springturkey.services.oauth.factory.EmailSenderFactory;
import devs.mrp.springturkey.services.oauth.impl.EmailCommand;
import devs.mrp.springturkey.services.oauth.impl.SendEmailCaseImpl;

@Component
public class EmailSenderFactoryImpl implements EmailSenderFactory {

	@Value("${turkey.realm}")
	private String realm;



	@Override
	public SendEmailCase get(EmailCommand command) {
		return new SendEmailCaseImpl(realm, command);
	}

}
