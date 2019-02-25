package com.arzio.arziolib.api.util.reflection.finder;

import com.arzio.arziolib.api.exception.FinderException;
import com.arzio.arziolib.api.util.reflection.CDClasses;

public class NameClassFinder implements ContentFinder<Class<?>>{

	private final String className;
	
	public NameClassFinder(String className) {
		this.className = className;
	}
	
	@Override
	public Class<?> find(Class<?> from) throws FinderException {
		try {
			return CDClasses.getClassBySourceFileName(className);
		} catch (ClassNotFoundException e) {
			throw new FinderException(e);
		}
	}
	
	public static ContentFinder<Class<?>> find(String className) {
		return new NameClassFinder(className);
	}

}
