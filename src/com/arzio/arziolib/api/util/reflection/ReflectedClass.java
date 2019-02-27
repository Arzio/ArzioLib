package com.arzio.arziolib.api.util.reflection;

import com.arzio.arziolib.api.exception.FinderException;
import com.arzio.arziolib.api.util.reflection.finder.ContentFinder;

public class ReflectedClass {
	
	private Class<?> clazzFound;
	
	public ReflectedClass(ContentFinder<Class<?>> finder) {
		try {
			this.clazzFound = finder.find(null);
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public ReflectedClass(Class<?> clazz) {
		this.clazzFound = clazz;
	}
	
	public Class<?> getReferencedClass() {
		return clazzFound;
	}
	
	public boolean hasFound() {
		return this.getReferencedClass() != null;
	}

}
