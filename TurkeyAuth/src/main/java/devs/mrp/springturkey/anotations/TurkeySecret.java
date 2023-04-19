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
	String message() default "Invalid password";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
