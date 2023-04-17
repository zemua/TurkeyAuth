package devs.mrp.springturkey.services.oauth.dtos;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserInfoDto {

	@NotBlank
	private String id;
	@NotBlank
	private String usernmae;
	private boolean enabled;
	private boolean totp;
	private boolean emailVerified;
	@NotBlank
	private String email;
	private List<String> requiredActions;

}
