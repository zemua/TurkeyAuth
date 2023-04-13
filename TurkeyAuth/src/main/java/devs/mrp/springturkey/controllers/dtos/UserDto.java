package devs.mrp.springturkey.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import devs.mrp.springturkey.entities.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserDto {

	@JsonProperty
	private String email;

	public UserDto(User user) {
		this.email = user.getEmail();
	}

	public User toUser() {
		return User.builder()
				.email(this.email)
				.username(this.email)
				.build();
	}

}
