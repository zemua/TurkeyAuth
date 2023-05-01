package devs.mrp.springturkey.exceptions;

import java.io.IOException;

public class GetUserInfoException extends IOException {

	private static final long serialVersionUID = -1059137822188032372L;

	public GetUserInfoException() {
		super();
	}

	public GetUserInfoException(String msg) {
		super(msg);
	}

	public GetUserInfoException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public GetUserInfoException(Throwable cause) {
		super(cause);
	}

}
