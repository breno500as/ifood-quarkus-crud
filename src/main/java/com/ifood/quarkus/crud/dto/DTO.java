package com.ifood.quarkus.crud.dto;

import javax.validation.ConstraintValidatorContext;

public interface DTO {

	default boolean isValid(ConstraintValidatorContext v) {
		return true;
	}
}
