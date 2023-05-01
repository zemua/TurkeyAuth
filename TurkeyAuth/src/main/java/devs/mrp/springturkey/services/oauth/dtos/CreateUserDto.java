package devs.mrp.springturkey.services.oauth.dtos;

import java.util.Arrays;
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
	private boolean emailVerified;

	public CreateUserDto(User user) {
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.enabled = true;
		this.emailVerified = false;
		this.credentials = List.of(Credentials.builder()
				.type("password")
				.value(user.getSecret().clone())
				.temporary(false)
				.build());
		Arrays.fill(user.getSecret(), '0');
		this.requiredActions = List.of("VERIFY_EMAIL");
	}

	public User toUser() {
		User user = User.builder()
				.email(this.email)
				.username(this.username)
				.secret(this.credentials.get(0).getValue().clone())
				.build();
		Arrays.fill(this.credentials.get(0).getValue(), '0');
		return user;
	}

	public User toUserWithoutSecret() {
		User user = User.builder()
				.email(this.email)
				.username(this.username)
				.build();
		Arrays.fill(this.credentials.get(0).getValue(), '0');
		return user;
	}

}
