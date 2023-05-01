package devs.mrp.springturkey.services.oauth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EmailCommandTest {

	@Test
	void testUpdatePassword() {
		EmailCommand command =EmailCommand.UPDATE_PASSWORD;
		String result = command.getCommand();

		assertEquals("UPDATE_PASSWORD", result);
	}

}
