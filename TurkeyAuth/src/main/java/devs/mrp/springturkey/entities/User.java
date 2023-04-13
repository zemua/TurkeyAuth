package devs.mrp.springturkey.entities;

import org.springframework.lang.NonNull;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

	@NonNull
	private String username;
	@NonNull
	private String email;

}
