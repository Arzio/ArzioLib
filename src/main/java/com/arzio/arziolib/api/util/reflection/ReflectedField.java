package com.arzio.arziolib.api.util.reflection;

import java.lang.reflect.Field;

import com.arzio.arziolib.api.exception.CDAReflectionException;
import com.arzio.arziolib.api.exception.FinderException;
import com.arzio.arziolib.api.util.reflection.finder.ContentFinder;

public class ReflectedField<T> {

	private Field field;
	
	public ReflectedField(ReflectedClass clazz, ContentFinder<Field> finder) {
		this(clazz.getReferencedClass(), finder);
	}
	
	public ReflectedField(Class<?> clazz, ContentFinder<Field> finder) {
		try {
			this.field = finder.find(clazz);
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public T getValue(Object instance) {
		try {
			return (T) field.get(instance);
		} catch (Exception e) {
			throw new CDAReflectionException("There was an error when trying to get a value from a field", e);
		}
	}
	
	public void setValue(Object instance, T value) {
		try {
			field.set(instance, value);
		} catch (Exception e) {
			throw new CDAReflectionException("There was an error when trying to set a value in a field", e);
		}
	}
}