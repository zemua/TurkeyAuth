package devs.mrp.springturkey.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ClientTokenDto {

	@JsonProperty("access_token")
	private char[] accessToken;
	@JsonProperty("expires_in")
	private int expiresIn;
	@JsonProperty("refresh_expires_in")
	private int refreshTokenExpiresIn;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("not-before-policy")
	private int notBeforePolicy;
	@JsonProperty("scope")
	private String scope;

}
