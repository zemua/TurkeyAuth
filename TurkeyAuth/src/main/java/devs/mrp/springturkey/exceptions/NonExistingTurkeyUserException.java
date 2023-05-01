package devs.mrp.springturkey.exceptions;

import java.io.IOException;

public class NonExistingTurkeyUserException extends IOException {

	private static final long serialVersionUID = 7503981856605252791L;

	public NonExistingTurkeyUserException() {
		super();
	}

	public NonExistingTurkeyUserException(String msg) {
		super(msg);
	}

	public NonExistingTurkeyUserException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NonExistingTurkeyUserException(Throwable cause) {
		super(cause);
	}

}
