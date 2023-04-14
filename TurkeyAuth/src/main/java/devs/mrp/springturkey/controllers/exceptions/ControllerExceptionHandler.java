package devs.mrp.springturkey.controllers.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.KeycloakClientUnauthorizedException;
import devs.mrp.springturkey.exceptions.TokenRetrievalException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

	@ExceptionHandler(KeycloakClientUnauthorizedException.class)
	public ResponseEntity<?> handleKeycloakClientUnauthorizedException(KeycloakClientUnauthorizedException ex) {
		log.error("Handling exception: ", ex);
		return ResponseEntity.internalServerError().build();
	}

	@ExceptionHandler(ClientRequestException.class)
	public ResponseEntity<?> handleClientRequestException(ClientRequestException ex) {
		log.error("Handling exception:", ex);
		return ResponseEntity.status(ex.getResponse()).build();
	}

	@ExceptionHandler(TokenRetrievalException.class)
	public ResponseEntity<?> handleTokenRetrievalException(TokenRetrievalException ex) {
		log.error("Handling expceiont: ", ex);
		return ResponseEntity.status(HttpStatusCode.valueOf(523)).build();
	}

}
