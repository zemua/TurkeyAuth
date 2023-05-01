package devs.mrp.springturkey.exceptions;

import java.io.IOException;

public class TokenRetrievalException extends IOException {

	private static final long serialVersionUID = 1732080730902288616L;

	public TokenRetrievalException() {
		super();
	}

	public TokenRetrievalException(String msg) {
		super(msg);
	}

	public TokenRetrievalException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public TokenRetrievalException(Throwable cause) {
		super(cause);
	}

}
