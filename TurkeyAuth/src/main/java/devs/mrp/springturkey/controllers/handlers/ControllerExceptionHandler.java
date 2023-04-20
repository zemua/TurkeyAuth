package devs.mrp.springturkey.controllers.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.GetUserInfoException;
import devs.mrp.springturkey.exceptions.KeycloakClientUnauthorizedException;
import devs.mrp.springturkey.exceptions.NonExistingTurkeyUserException;
import devs.mrp.springturkey.exceptions.SendVerificationMailException;
import devs.mrp.springturkey.exceptions.TokenRetrievalException;
import devs.mrp.springturkey.exceptions.TurkeyGenericException;
import devs.mrp.springturkey.exceptions.dto.FieldErrorMessage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

	@ExceptionHandler(TurkeyGenericException.class)
	public ResponseEntity<?> handleTurkeyGenericException(TurkeyGenericException ex) {
		log.error("Handling exception: ", ex);
		return ResponseEntity.status(HttpStatusCode.valueOf(520))
				.body("An error has ocurred processing the request");
	}

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
		log.error("Handling expception: ", ex);
		return ResponseEntity.status(HttpStatusCode.valueOf(523)).build();
	}

	@ExceptionHandler(GetUserInfoException.class)
	@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
	@ResponseBody
	public Mono<String> handleGetUserInfoException(GetUserInfoException ex) {
		log.error("Handling exception: ", ex);
		return Mono.just("Error getting user information");
	}

	@ExceptionHandler(SendVerificationMailException.class)
	@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
	@ResponseBody
	public Mono<String> handleSendVerificationMailException(SendVerificationMailException ex) {
		log.error("Handling exception: ", ex);
		return Mono.just("Error sending verification email");
	}

	@ExceptionHandler(WebExchangeBindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Mono<List<FieldErrorMessage>> processValidationError(WebExchangeBindException ex) {
		List<FieldErrorMessage> errors = new ArrayList<>();
		ex.getAllErrors().forEach(e -> addErrorToList(e, errors));
		return Mono.just(errors);
	}

	private void addErrorToList(ObjectError error, List<FieldErrorMessage> errors) {
		FieldErrorMessage result;
		if (error instanceof FieldError) {
			FieldError fieldError = (FieldError) error;
			result = FieldErrorMessage.builder().fieldName(fieldError.getField()).errorMessage(fieldError.getDefaultMessage()).build();
		} else {
			result = FieldErrorMessage.builder().errorMessage(error.getDefaultMessage()).build();
		}
		errors.add(result);
	}

	@ExceptionHandler(NonExistingTurkeyUserException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Mono<String> handleNonExistingUserException(NonExistingTurkeyUserException ex) {
		log.error("Handling exception: ", ex);
		return Mono.just(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Mono<String> handleGenericException(Exception ex) {
		log.error("Handling uncatch exception: ", ex);
		return Mono.just("Internal server error");
	}

}
