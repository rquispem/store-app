package com.store.app.common.utils;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.store.app.common.exception.FieldNotValidException;

public class ValidationUtils {

	public static <T> void validateEntityFields(final Validator validator, final T entity) {
		final Set<ConstraintViolation<T>> errors = validator.validate(entity);
		final Iterator<ConstraintViolation<T>> itErrors = errors.iterator();
		if (itErrors.hasNext()) {
			final ConstraintViolation<T> violation = itErrors.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}
	}
}
