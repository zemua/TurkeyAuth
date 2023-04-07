package devs.mrp.springturkey.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

	private String email;

}
