package devs.mrp.springturkey.services.oauth.dtos;

import java.util.List;

import devs.mrp.springturkey.entities.User;
import lombok.Getter;

@Getter
public class CreateUserDto {


	private String email;
	private String username;
	private List<Credentials> credentials;
	private List<String> requiredActions;
	private boolean enabled;

	public CreateUserDto(User user) {
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.enabled = true;
		this.credentials = List.of(Credentials.builder()
				.type("password")
				.value(user.getSecret())
				.temporary(false)
				.build());
		this.requiredActions = List.of("VERIFY_EMAIL");
	}

	public User toUser() {
		User user = User.builder()
				.email(this.email)
				.username(this.username)
				.secret(this.credentials.get(0).getValue())
				.build();
		return user;
	}

}
