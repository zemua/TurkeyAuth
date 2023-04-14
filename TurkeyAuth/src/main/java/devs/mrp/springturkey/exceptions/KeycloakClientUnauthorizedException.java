package devs.mrp.springturkey.exceptions;

import java.net.ConnectException;

public class KeycloakClientUnauthorizedException extends ConnectException {

	private static final long serialVersionUID = 2760016356339629971L;

	public KeycloakClientUnauthorizedException() {
		super();
	}

	public KeycloakClientUnauthorizedException(String msg) {
		super(msg);
	}

}
