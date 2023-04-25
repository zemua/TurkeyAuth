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
public class RefreshCredentialsDto {

	@JsonProperty("client_id")
	private String clientId;
	@JsonProperty("client_secret")
	private char[] clientSecret;
	@JsonProperty("grant_type")
	private String grantType;
	@JsonProperty("refresh_token")
	private char[] refreshToken;

}
