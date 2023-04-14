package devs.mrp.springturkey.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

public class ClientRequestException extends IOException {

	private static final long serialVersionUID = 5334012169867550826L;

	@Getter
	private HttpStatusCode response;

	public ClientRequestException() {
		this.response = HttpStatusCode.valueOf(400);
	}

	public ClientRequestException(HttpStatusCode response) {
		this.response = response;
	}

	public ClientRequestException(HttpStatusCode response, String msg) {
		super(msg);
		this.response = response;
	}

	public ClientRequestException(HttpStatusCode response, String msg, Throwable cause) {
		super(msg, cause);
		this.response = response;
	}

	public ClientRequestException(HttpStatusCode response, Throwable cause) {
		super(cause);
		this.response = response;
	}

	public ClientRequestException(String msg) {
		super(msg);
	}

	public ClientRequestException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ClientRequestException(Throwable cause) {
		super(cause);
	}

}
