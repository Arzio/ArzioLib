package com.arzio.arziolib.api.util.reflection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import com.arzio.arziolib.ArzioLib;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraftforge.event.Event;

public class ReflectionHelper {
	
	private static BiMap<String, String> allCraftingDeadClassNames;
	
	public static interface FieldChecker<T> {
		public boolean isCorrect(T found, Field field) throws Exception;
	}
	
	public static Object invoke(Method method, Object instance, Object... args) throws Exception{
		method.setAccessible(true);
		return method.invoke(instance, args);
	}
	
	public static void setFinalStatic(Field field, Object newValue) throws Exception {
		setFinal(field, null, newValue);
	}

	public static void setFinal(Field field, Object instance, Object newValue) throws Exception {
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(instance, newValue);
	}
	
	public static Object getValue(Field field, Object instance) throws Exception{
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		return field.get(instance);
	}
	
	public static BiMap<String, String> getSimpleSourceFileNamesToOriginalClassNames(){
		if (allCraftingDeadClassNames == null) {
			
			ModContainer modContainer = getCraftingDeadModContainer();
			
			File file = modContainer.getSource();
			
			allCraftingDeadClassNames = ReflectionHelper.mapClassesInsideZipWithOriginalNames(file);
		}
		return allCraftingDeadClassNames;
	}
	
	public static ModContainer getCraftingDeadModContainer() {
		Loader loader = Loader.instance();
		
		loader.getModList();
		ModContainer modContainer = null;
		
		for (ModContainer mod : loader.getModList() ) {
			String modNameLowerCaseWithoutSpaces = mod.getName().replace(" ", "").toLowerCase();
			if (modNameLowerCaseWithoutSpaces.contains("craftingdead") || modNameLowerCaseWithoutSpaces.startsWith("cd")) {
				modContainer = mod;
				break;
			}
		}
		return modContainer;
	}
	
	public static <T, R> R getValueFromFieldName(Class<R> resultType, Class<T> clazz, T instance, String... possibleNames) {
		
		Class<?> currentLoop = clazz;
		
		while (currentLoop != Object.class) {
			for (Field f : currentLoop.getDeclaredFields()) {
				try {
					for (String name : possibleNames) {
						if (f.getName().equals(name)) {
							return (R) f.get(instance);
						}
					}
					
				} catch (Exception e) {
					ArzioLib.getInstance().getLogger().log(Level.SEVERE, "Field "+f+" could not be used by the value getter.", e);
				}
			}
			currentLoop = currentLoop.getSuperclass();
		}
		
		return null;
	}
	
	public static <T> T getValueFromEvent(Event event, Class<T> result, Class<?>... otherTypes) {
		
		Class<?> currentLoop = event.getClass();
		
		while (currentLoop != Object.class) {
			for (Field f : currentLoop.getDeclaredFields()) {
				try {
					if (f.getType().equals(result)) {
						return (T) f.get(event);
					}
					
					if (otherTypes != null) {
						for (Class<?> type : otherTypes) {
							f.setAccessible(true);
							if (f.getType().equals(type)) {
								return (T) f.get(event);
							}
						}
					}
				} catch (Exception e) {
					ArzioLib.getInstance().getLogger().log(Level.SEVERE, "Field "+f+" could not be used by the value getter.", e);
				}
			}
			currentLoop = currentLoop.getSuperclass();
		}
		
		return null;
	}
	
	public static <T> Field findValueWithTypeAndFilter(Object instance, Class<?> clazz, Class<?> type, FieldChecker<T> predicate, String regex) {
		
		for (Field f : clazz.getDeclaredFields()) {
			
			try {
				f.setAccessible(true);
				
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
				
				if (type != null) {
					if (!f.getType().equals(type)) {
						continue;
					}
				}
				
				if (regex != null) {
					if (!f.getName().matches(regex)) {
						continue;
					}
				}
				
				if (predicate != null) {
					
					if (instance == null ? (f.getModifiers() & Modifier.STATIC) != 0 : (f.getModifiers() & Modifier.STATIC) == 0) {
						if (!predicate.isCorrect((T) f.get(instance), f)) {
							continue;
						}
					}
				}
				
				return f;
			} catch (Exception e) {
				ArzioLib.getInstance().getLogger().log(Level.SEVERE, "Field with value not found in class "+clazz, e);
			}
			
		}
		
		return null;
	}
	
	public static Method findMethodWithTypes(Class<?> clazz, String regex, Class<?> returnType, Class<?>[] types) {
		
		for (Method m : clazz.getDeclaredMethods()) {
			
			try {
				m.setAccessible(true);
				if (returnType != null && !returnType.equals(m.getReturnType())) {
					continue;
				}
				if (regex != null) {
					if (!m.getName().matches(regex)) {
						continue;
					}
				}
				if (Arrays.equals(types, m.getParameterTypes())) {
					return m;
				}
			} catch (Exception e) {
				ArzioLib.getInstance().getLogger().log(Level.SEVERE, "Method with return type "+clazz+" and parameters "+Arrays.toString(types)+" not found in class "+clazz.getSimpleName(), e);
			}
			
		}
		
		return null;
	}
	
	public static BiMap<String, String> mapClassesInsideZipWithOriginalNames(File file) {
		
		BiMap<String, String> biMap = HashBiMap.create();
		
		try (ZipFile zipIn = new ZipFile(file)){
			Enumeration<? extends ZipEntry> entry = zipIn.entries();
			while (entry.hasMoreElements()) {
				ZipEntry next = entry.nextElement();
				if (next.isDirectory()) {
					continue;
				}

				byte[] data = IOUtils.toByteArray(zipIn.getInputStream(next));
				String name = next.getName();
				
				if (name.endsWith(".class")) {
					
					try {
						ClassReader reader = new ClassReader(data);
						ClassNode classNode = new ClassNode();
						reader.accept(classNode, ClassReader.SKIP_FRAMES);

						String sourceFileName = classNode.sourceFile;
						if (sourceFileName.endsWith(".java")) {
							sourceFileName = sourceFileName.substring(0, sourceFileName.lastIndexOf("."));
						}

						String reconstructedName = sourceFileName;
						String originalClassName = name.replace('/', '.').replaceAll(".class", "");
						
						biMap.put(reconstructedName, originalClassName);
					} catch (Exception e) {
					   e.printStackTrace();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return biMap;
	}
	
	public static <T> T safeCast(Object o, Class<T> clazz) {
		return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
	}
	
	public static <T> T unsafeCast(Object o, Class<T> clazz) {
		return clazz.cast(o);
	}
}
