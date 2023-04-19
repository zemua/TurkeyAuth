package devs.mrp.springturkey.anotations;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TurkeySecretValidator implements ConstraintValidator<TurkeySecret, char[]> {

	@Override
	public boolean isValid(char[] value, ConstraintValidatorContext context) {
		if (Objects.isNull(value)) {
			return false;
		} else if (value.length < 8) {
			return false;
		} else if (missingNumbers(value)) {
			return false;
		} else if (missingLowerCase(value)) {
			return false;
		} else if (missingUpperCase(value)) {
			return false;
		} else if (missingSymbol(value)) {
			return false;
		}
		return true;
	}

	private boolean missingNumbers(char[] value) {
		for (char c : value) {
			if (Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	private boolean missingLowerCase(char[] arr) {
		for (char c : arr) {
			if (Character.isLowerCase(c)) {
				return false;
			}
		}
		return true;
	}

	private boolean missingUpperCase(char[] arr) {
		for (char c : arr) {
			if (Character.isUpperCase(c)) {
				return false;
			}
		}
		return true;
	}

	private boolean missingSymbol(char[] arr) {
		for (char c : arr) {
			if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}
		return true;
	}

}
