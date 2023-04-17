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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;

import devs.mrp.springturkey.controllers.UserController;
import devs.mrp.springturkey.controllers.dtos.UserRequest;
import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.KeycloakClientUnauthorizedException;
import devs.mrp.springturkey.exceptions.TokenRetrievalException;
import reactor.core.publisher.Mono;

class ControllerExceptionHandlerTest {

	private ControllerExceptionHandler controllerExceptionHandler;

	@BeforeEach
	void setup() {
		controllerExceptionHandler = new ControllerExceptionHandler();
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
	void testProcessValidationError() throws NoSuchMethodException, SecurityException {
		MethodParameter parameter = new MethodParameter(UserController.class.getMethod("create", Mono.class), 0);
		UserRequest request = new UserRequest("some@email.com", "");
		BindingResult result = new BeanPropertyBindingResult(request, "myRequest");

		WebExchangeBindException ex = new WebExchangeBindException(parameter, result);
		ex.addError(new ObjectError("secret", "Shall not be empty"));

		List<ObjectError> errors = controllerExceptionHandler.processValidationError(ex).block();
		assertEquals("[Error in object 'secret': codes []; arguments []; default message [Shall not be empty]]", errors.toString());
	}

}
