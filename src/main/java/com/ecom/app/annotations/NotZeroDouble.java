/**
 * 
 */
package com.ecom.app.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = NotZeroDoubleValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
/**
 * @author akash
 *
 */
public @interface NotZeroDouble {
	String message() default "must be greater than 0";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
