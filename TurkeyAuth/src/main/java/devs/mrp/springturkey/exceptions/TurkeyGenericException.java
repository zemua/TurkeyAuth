package devs.mrp.springturkey.exceptions;

public class TurkeyGenericException extends Exception {

	private static final long serialVersionUID = 782369982516804L;

	public TurkeyGenericException() {
		super();
	}

	public TurkeyGenericException(String msg) {
		super(msg);
	}

	public TurkeyGenericException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public TurkeyGenericException(Throwable cause) {
		super(cause);
	}

}
