package com.onboarding.business.util;

import static com.onboarding.business.constant.UrlMapping.EXCEPTION_MESSAGE;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.onboarding.business.exception.CustomException;

@Component
public class Mapper {

	public <T> T convert(Object srcObj, Class<T> targetClass) {
		T response = null;

		try {
			response = new ModelMapper().map(srcObj, targetClass);
		}
		catch (Exception e) {
			throw new CustomException(EXCEPTION_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
}
