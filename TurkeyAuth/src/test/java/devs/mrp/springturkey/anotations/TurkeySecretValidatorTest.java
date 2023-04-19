package devs.mrp.springturkey.anotations;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(SpringExtension.class)
class TurkeySecretValidatorTest {

	private Validator validator;

	@BeforeEach
	public void setUpClass() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testCorrectPassword() {
		char[] secret = {'1','2','a','s','A','S','$','&'};
		Validable validable = new Validable(secret);

		var violations = validator.validate(validable);

		assertTrue(violations.isEmpty());
	}

	@Test
	void testNull() {
		char[] secret = null;
		Validable validable = new Validable(secret);

		var violations = validator.validate(validable);

		assertTrue(!violations.isEmpty());
	}

	@Test
	void testMissingNumber() {
		char[] secret = {'a','s','d','f','A','S','D','$','&'};
		Validable validable = new Validable(secret);

		var violations = validator.validate(validable);

		assertTrue(!violations.isEmpty());
	}

	@Test
	void testMissingLowercase() {
		char[] secret = {'1','2','3','4','A','S','D','$','&'};
		Validable validable = new Validable(secret);

		var violations = validator.validate(validable);

		assertTrue(!violations.isEmpty());
	}

	@Test
	void testMissingUppercase() {
		char[] secret = {'1','2','3','4','a','s','d','$','&'};
		Validable validable = new Validable(secret);

		var violations = validator.validate(validable);

		assertTrue(!violations.isEmpty());
	}

	@Test
	void testMissingSymbols() {
		char[] secret = {'1','2','3','4','a','s','d','A','S','D'};
		Validable validable = new Validable(secret);

		var violations = validator.validate(validable);

		assertTrue(!violations.isEmpty());
	}

	@Test
	void testTooShort() {
		char[] secret = {'1','2','a','s','A','S','$'};
		Validable validable = new Validable(secret);

		var violations = validator.validate(validable);

		assertTrue(!violations.isEmpty());
	}

	private class Validable {
		@TurkeySecret
		char[] secret;
		Validable(char[] s) {
			this.secret = s;
		}
	}

}
