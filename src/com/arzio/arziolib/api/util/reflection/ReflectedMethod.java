package com.arzio.arziolib.api.util.reflection;

import java.lang.reflect.Method;

import com.arzio.arziolib.api.exception.CDAReflectionException;
import com.arzio.arziolib.api.exception.FinderException;
import com.arzio.arziolib.api.util.reflection.finder.ContentFinder;

public class ReflectedMethod {
	private Method method;
	
	public ReflectedMethod(ReflectedClass clazz, ContentFinder<Method> finder) {
		try {
			this.method = finder.find(clazz.getReferencedClass());
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public Method getReferencedMethod() {
		return this.method;
	}
	
	public Object invoke(Object instance, Object... args) {
		try {
			return method.invoke(instance, args);
		} catch (Exception e) {
			throw new CDAReflectionException("There was an error when trying to invoke a method. This is weird.", e);
		}
	}
}
