package devs.mrp.springturkey.services.oauth.dtos;

import devs.mrp.springturkey.entities.User;
import lombok.Getter;

@Getter
public class CreateUserDto {


	private String email;
	private String username;
	private Credentials credentials;
	private boolean enabled;

	public CreateUserDto(User user) {
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.enabled = true;
		this.credentials = Credentials.builder()
				.type("password")
				.value(user.getSecret())
				.temporary(false)
				.build();
	}

	public User toUser() {
		User user = User.builder()
				.email(this.email)
				.username(this.username)
				.secret(this.credentials.getValue())
				.build();
		return user;
	}

}
