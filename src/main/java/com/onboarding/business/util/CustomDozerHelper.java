package com.onboarding.business.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dozer.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;

public class CustomDozerHelper {
	
	
	private CustomDozerHelper() {
	}

	/**
	 * Copy list of source objects into list of destination class & return dest
	 * list.
	 */
	public static <F, T> List<T> map(final Mapper mapper, final List<F> source, final Class<T> destType) {

		final List<T> dest = new ArrayList<>();
		source.forEach(object -> {
			if (object != null) {
				T target = mapper.map(object, destType);
				dest.add(target);
			}
		});
		dest.removeIf(Objects::isNull);
		return dest;
	}
	
	private static final ModelMapper modelMapper;
	static {
		modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}
	
	public static <D, T> D mapEntity(final T entity, Class<D> outClass) {
		
		return modelMapper.map(entity, outClass);
		
	}

	public static <F, T> List<T> map(final Mapper mapper, final Page<F> source, final Class<T> destType) {

		final List<T> dest = new ArrayList<>();
		source.forEach(object -> {
			if (object != null) {
				T target = mapper.map(object, destType);
				dest.add(target);
			}
		});
		dest.removeIf(Objects::isNull);
		return dest;
	}


}