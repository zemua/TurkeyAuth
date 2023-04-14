package devs.mrp.springturkey.controllers.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import devs.mrp.springturkey.exceptions.ClientRequestException;
import devs.mrp.springturkey.exceptions.KeycloakClientUnauthorizedException;
import devs.mrp.springturkey.exceptions.TokenRetrievalException;

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

}
