package devs.mrp.springturkey.exceptions;

import java.io.IOException;

public class SendVerificationMailException extends IOException {

	private static final long serialVersionUID = 1090147636073654423L;

	public SendVerificationMailException() {
		super();
	}

	public SendVerificationMailException(String msg) {
		super(msg);
	}

	public SendVerificationMailException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SendVerificationMailException(Throwable cause) {
		super(cause);
	}

}
