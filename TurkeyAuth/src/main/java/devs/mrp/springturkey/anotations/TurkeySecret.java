package devs.mrp.springturkey.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = TurkeySecretValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TurkeySecret {
	String message() default "Invalid password, it must be at least 8 characters long and contain at least a lower-case letter, an upper-case letter, a number and a symbol";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
