package devs.mrp.springturkey.controllers.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class TurkeyExceptionHandler {

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
		log.error("Handling exception: ", ex);
		return ResponseEntity.internalServerError().build();
	}

	@ExceptionHandler(ClientRequestException.class)
	public ResponseEntity<?> handleClientRequestException(ClientRequestException ex) {
		log.error("Handling exception:", ex);
		return ResponseEntity.badRequest().build();
	}

}
