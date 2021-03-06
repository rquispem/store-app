package com.store.app.common.exception;

public class FieldNotValidException extends RuntimeException {

	private static final long serialVersionUID = 3882388034439071290L;

	private final String fieldName;

	public FieldNotValidException(String fieldName, final String message) {
		super(message);
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	@Override
	public String toString() {
		return "FieldNotValidException [fieldName=" + fieldName + "]";
	}
}
