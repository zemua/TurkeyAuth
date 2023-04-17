package devs.mrp.springturkey.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

	@NotBlank
	private String username;
	@NotBlank
	@Email
	private String email;
	private String secret;

}
