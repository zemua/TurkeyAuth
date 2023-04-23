package devs.mrp.springturkey.services.oauth.factory;

import reactor.core.publisher.Mono;

public interface EmailSenderFactory {

	public SendEmailCase get(Mono<String> userId); // TODO modify ChangePasswordCase to SendEmailCase and set the method "send" to accept an ENUM with the action

}
