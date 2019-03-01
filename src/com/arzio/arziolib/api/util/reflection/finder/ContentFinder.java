package com.arzio.arziolib.api.util.reflection.finder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.arzio.arziolib.api.exception.FinderException;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper.FieldChecker;

public interface ContentFinder<F> {

	public F find(Class<?> from) throws FinderException;
	
	public static class MethodBuilder {
		
		private Class<?> returnType;
		private Class<?>[] types;
		private String regex;
		
		public MethodBuilder() { }
		
		public MethodBuilder withReturnType(Class<?> returnType) {
			this.returnType = returnType;
			return this;
		}
		
		public MethodBuilder withParameterTypes(Class<?>... types) {
			this.types = types;
			return this;
		}
		
		public MethodBuilder withRegexName(String regex){
			this.regex = regex;
			return this;
		}
		
		public ContentFinder<Method> build() {
			return new ContentFinder<Method>() {
				
				@Override
				public Method find(Class<?> from) throws FinderException{
					Method found = ReflectionHelper.findMethodWithTypes(from, regex, returnType, types);
					if (found == null) {
						throw new FinderException("The method cannot the be found!");
					}
					return found;
				}
				
			};
		}
	}
	
	public static class FieldBuilder<T> {
		
		private Class<?> type;
		private FieldChecker<T> checker;
		private Object instance;
		private String regex;
		
		public FieldBuilder() { }
		
		public FieldBuilder<T> withType(Class<?> returnType) {
			this.type = returnType;
			return this;
		}
		
		public FieldBuilder<T> withExactValue(Object instance, Class<?> type, final T value){
			return this.withValue(instance, type, new FieldChecker<T>() {

				@Override
				public boolean isCorrect(T found, Field field) {
					return found.equals(value);
				}
				
			});
		}
		
		public FieldBuilder<T> withValue(Object instance, Class<?> type, FieldChecker<T> checker) {
			this.withType(type);
			this.checker = checker;
			this.instance = instance;
			return this;
		}
		
		public FieldBuilder<T> withRegexName(String regex){
			this.regex = regex;
			return this;
		}
		
		public ContentFinder<Field> build() {
			return new ContentFinder<Field>() {

				@Override
				public Field find(Class<?> from) throws FinderException{
					Field found = ReflectionHelper.findValueWithTypeAndFilter(instance, from, type, checker, regex);
					if (found == null) {
						throw new FinderException("The field could not be found!");
					}
					return found;
				}
				
			};
		}
	}
}
