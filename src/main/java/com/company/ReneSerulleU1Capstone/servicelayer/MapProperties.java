package com.company.ReneSerulleU1Capstone.servicelayer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MapProperties<First, Second> {
    private First b;
    private Second e;

    public MapProperties(First b, Second e) {
        this.b = b;
        this.e = e;
    }

    public MapProperties(First b, Class<Second> extendedClass)
            throws IllegalAccessException, InstantiationException {
        this.b = b;
        this.e = extendedClass.newInstance();
    }

    public Second mapFirstToSecond(boolean ignoreErrors) throws IllegalAccessException {
        List<Field> bFields = getFields(b.getClass(), new ArrayList<>());
        List<Field> eFields = getFields(e.getClass(), new ArrayList<>());

        bFields = bFields.stream().filter(field ->
                eFields.stream().anyMatch(field1 -> field1.getName().equals(field.getName())))
            .collect(Collectors.toList());

        if (bFields.isEmpty()) return e;

        for (Field field : bFields) {
            try {
                Class<?>[] noParams = {};
                String name = field.getName().substring(0,1).toUpperCase() +
                        field.getName().substring(1);
                String setMethod = "set" + name;
                String getMethod = "get" + name;
                Method get = b.getClass().getMethod(getMethod, noParams);
                Method set = setMethod(b.getClass(), setMethod, get.getGenericReturnType());
                get.setAccessible(true);
                set.setAccessible(true);
                set.invoke(e, get.invoke(b));
            } catch (NoSuchMethodException e) {
                if (!ignoreErrors) {
                    throw new NoSuchElementException("Method not found. " + e.getMessage());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (!ignoreErrors) {
                    throw new IllegalAccessException("Attempting to invoke a method that's not accessible. " +
                            e.getMessage());
                }
            }
        }
        return e;
    }

    public List<Field> getFields(Class className, List<Field> fields) {
        if (className == Object.class) return fields;
        Field[] f = className.getDeclaredFields();
        fields.addAll(Arrays.asList(f));
        getFields(className.getSuperclass(), fields);
        return fields;
    }

    public Method setMethod(Class<?> className, String methodName, Type returnType) throws NoSuchMethodException {
        try {
            return className.getMethod(methodName, (Class<?>) returnType);
        } catch (NoSuchMethodException ex) {
            if (className != Object.class) {
                return setMethod(className.getSuperclass(), methodName, returnType);
            } else {
                throw ex;
            }
        }
    }



}
