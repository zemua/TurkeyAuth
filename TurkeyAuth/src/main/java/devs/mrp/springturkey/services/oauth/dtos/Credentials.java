package devs.mrp.springturkey.services.oauth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import devs.mrp.springturkey.anotations.TurkeySecret;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Credentials {

	@JsonProperty
	String type;
	@JsonProperty
	@TurkeySecret
	char[] value;
	@JsonProperty
	boolean temporary;

}
