package devs.mrp.springturkey.entities;

import devs.mrp.springturkey.anotations.TurkeySecret;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class User {

	@NotBlank
	private String username;
	@NotBlank
	@Email
	private String email;
	@ToString.Exclude
	@TurkeySecret
	private char[] secret;

}
