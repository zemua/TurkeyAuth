package devs.mrp.springturkey.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import devs.mrp.springturkey.controllers.dtos.UserResponse;

class UserTest {

	@Test
	void convertFromUser() {
		User user = User.builder().email("some@email.com").username("another@e.mail").build();
		UserResponse dto = new UserResponse(user);
		assertEquals(user.getEmail(), dto.getEmail());
	}

	@Test
	void convertToUser() {
		UserResponse dto = new UserResponse("some@mail.com");
		User user = dto.toUser();
		assertEquals(dto.getEmail(), user.getEmail());
		assertEquals(dto.getEmail(), user.getUsername());
	}

}
