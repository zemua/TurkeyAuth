package devs.mrp.springturkey.controllers.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;

import devs.mrp.springturkey.controllers.UserController;
import devs.mrp.springturkey.controllers.dtos.UserRequest;
import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.GetUserInfoException;
import devs.mrp.springturkey.exceptions.KeycloakClientUnauthorizedException;
import devs.mrp.springturkey.exceptions.SendVerificationMailException;
import devs.mrp.springturkey.exceptions.TokenRetrievalException;
import devs.mrp.springturkey.exceptions.TurkeyGenericException;
import devs.mrp.springturkey.exceptions.dto.FieldErrorMessage;
import reactor.core.publisher.Mono;

class ControllerExceptionHandlerTest {

	private ControllerExceptionHandler controllerExceptionHandler;

	@BeforeEach
	void setup() {
		controllerExceptionHandler = new ControllerExceptionHandler();
	}

	@Test
	void testTurkeyGenericException() {
		TurkeyGenericException ex = new TurkeyGenericException();
		ResponseEntity<?> response = controllerExceptionHandler.handleTurkeyGenericException(ex);
		assertEquals(520, response.getStatusCode().value());
		assertEquals("An error has ocurred processing the request", response.getBody());
	}

	@Test
	void testHandleKeycloakClientUnauthorizedException() {
		KeycloakClientUnauthorizedException ex = new KeycloakClientUnauthorizedException();
		ResponseEntity<?> response = controllerExceptionHandler.handleKeycloakClientUnauthorizedException(ex);
		assertEquals(500, response.getStatusCode().value());
	}

	@Test
	void testHandleClientRequestException() {
		ClientRequestException ex = new ClientRequestException();
		ResponseEntity<?> response = controllerExceptionHandler.handleClientRequestException(ex);
		assertEquals(400, response.getStatusCode().value());
	}

	@Test
	void testHandleClientRequestExceptionSpecificCode() {
		ClientRequestException ex = new ClientRequestException(HttpStatusCode.valueOf(409));
		ResponseEntity<?> response = controllerExceptionHandler.handleClientRequestException(ex);
		assertEquals(409, response.getStatusCode().value());
	}

	@Test
	void testHandleTokenRetrievalException() {
		TokenRetrievalException ex = new TokenRetrievalException();
		ResponseEntity<?> response = controllerExceptionHandler.handleTokenRetrievalException(ex);
		assertEquals(523, response.getStatusCode().value());
	}

	@Test
	void testHandleGetUserInfoException() {
		GetUserInfoException ex = new GetUserInfoException();
		String response = controllerExceptionHandler.handleGetUserInfoException(ex).block();
		assertEquals("Error getting user information", response);
	}

	@Test
	void testHandleSendVerificationMailException() {
		SendVerificationMailException ex = new SendVerificationMailException();
		String response = controllerExceptionHandler.handleSendVerificationMailException(ex).block();
		assertEquals("Error sending verification email", response);
	}

	@Test
	void testProcessValidationError() throws NoSuchMethodException, SecurityException {
		MethodParameter parameter = new MethodParameter(UserController.class.getMethod("create", Mono.class), 0);
		UserRequest request = new UserRequest("some@email.com", "");
		BindingResult result = new BeanPropertyBindingResult(request, "myRequest");

		WebExchangeBindException ex = new WebExchangeBindException(parameter, result);
		ex.addError(new FieldError("UserObject", "secret", "Shall not be empty"));

		List<FieldErrorMessage> errors = controllerExceptionHandler.processValidationError(ex).block();
		assertEquals("[FieldErrorMessage(fieldName=secret, errorMessage=Shall not be empty)]", errors.toString());
	}

}
