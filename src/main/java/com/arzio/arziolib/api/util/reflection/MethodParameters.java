package com.arzio.arziolib.api.util.reflection;

import java.lang.reflect.Method;

public class MethodParameters {

    private final Method method;
    private final Class<?>[] parameterTypes;
    private final Object[] values;

    private MethodParameters(Method method){
        this.method = method;
        this.parameterTypes = method.getParameterTypes();
        this.values = new Object[parameterTypes.length];
    }

    public Method getMethod(){
        return this.method;
    }

    public boolean hasParameter(Class<?> clazz){
        for (Class<?> type : parameterTypes){
            if (type.equals(clazz)){
                return true;
            }
        }
        return false;
    }

    public <T> void setParameter(Class<T> clazz, T value){
        for (int i = 0; i < parameterTypes.length; i++){
            Class<?> type = parameterTypes[i];

            if (type.equals(clazz)){
                values[i] = value;
            }
        }
    }

    public Object[] getValues(){
        return values;
    }

    public static MethodParameters getFrom(Method method){
        return new MethodParameters(method);
    }
}