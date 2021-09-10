/**
 * 
 */
package com.ecom.app.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author akash
 *
 */
public class NotZeroDoubleValidator implements ConstraintValidator<NotZeroDouble, Double>{

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
		return value>0 || value>0.0;
	}

}
