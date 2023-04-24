package devs.mrp.springturkey.services.oauth.factory;

import devs.mrp.springturkey.services.oauth.SendEmailCase;
import devs.mrp.springturkey.services.oauth.impl.EmailCommand;

public interface EmailSenderFactory {

	public SendEmailCase get(EmailCommand command);

}
