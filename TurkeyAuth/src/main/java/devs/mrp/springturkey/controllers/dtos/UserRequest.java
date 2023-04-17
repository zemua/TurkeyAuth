package devs.mrp.springturkey.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import devs.mrp.springturkey.entities.User;
import jakarta.validation.constraints.Email;
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
public class UserRequest {

	@JsonProperty("email")
	@NotBlank
	@Email
	private String email;
	@JsonProperty("secret")
	@NotBlank
	private String secret;

	public UserRequest(User user) {
		this.email = user.getEmail();
		this.secret = user.getSecret();
	}

	public User toUser() {
		return User.builder()
				.email(this.email)
				.username(this.email)
				.secret(this.secret)
				.build();
	}

}
