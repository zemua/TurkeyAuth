package devs.mrp.springturkey.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import devs.mrp.springturkey.controllers.dtos.UserDto;

class UserTest {

	@Test
	void convertFromUser() {
		User user = User.builder().email("some@email.com").build();
		UserDto dto = new UserDto(user);
		assertEquals(user.getEmail(), dto.getEmail());
	}

	@Test
	void convertToUser() {
		UserDto dto = new UserDto("some@mail.com");
		User user = dto.toUser();
		assertEquals(dto.getEmail(), user.getEmail());
	}

}
