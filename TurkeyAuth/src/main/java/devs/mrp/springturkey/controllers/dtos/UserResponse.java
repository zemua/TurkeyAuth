package devs.mrp.springturkey.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import devs.mrp.springturkey.entities.User;
import jakarta.validation.constraints.NotBlank;
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
public class UserResponse {

	@JsonProperty("email")
	@NotBlank
	private String email;

	public UserResponse(User user) {
		this.email = user.getEmail();
	}

	public User toUser() {
		return User.builder()
				.email(this.email)
				.username(this.email)
				.build();
	}

}
