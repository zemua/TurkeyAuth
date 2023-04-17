package devs.mrp.springturkey.exceptions.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FieldErrorMessage {

	private String fieldName;
	private String errorMessage;

}
