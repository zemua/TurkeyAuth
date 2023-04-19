package devs.mrp.springturkey.services.oauth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Credentials {

	@JsonProperty
	String type;
	@JsonProperty
	char[] value;
	@JsonProperty
	boolean temporary;

}
