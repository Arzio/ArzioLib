package com.arzio.arziolib.api.util.reflection.finder;

import java.util.Arrays;

import com.arzio.arziolib.api.exception.FinderException;
import com.arzio.arziolib.api.util.reflection.CDClasses;

public class NameClassFinder implements ContentFinder<Class<?>>{

	private final String[] classNames;
	
	public NameClassFinder(String... className) {
		this.classNames = className;
	}
	
	@Override
	public Class<?> find(Class<?> from) throws FinderException {
		for (String className : classNames) {
			try {
				return CDClasses.getClassBySourceFileName(className);
			} catch (ClassNotFoundException e) {
				;
			}
		}
		throw new FinderException("Failed to find a class with possible names: "+Arrays.toString(classNames));
	}
	
	public static ContentFinder<Class<?>> find(String... classNames) {
		return new NameClassFinder(classNames);
	}

}
