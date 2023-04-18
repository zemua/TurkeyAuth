package devs.mrp.springturkey.services.oauth.dtos;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class UserInfoDto {

	@NotBlank
	private String id;
	@NotBlank
	private String username;
	private boolean enabled;
	private boolean totp;
	private boolean emailVerified;
	@NotBlank
	@Email
	private String email;
	private List<String> requiredActions;

}
