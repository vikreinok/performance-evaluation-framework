package com.takipi.samples.servprof.main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class DataFilter {


    public static final String CLASS_1 = "class org.hibernate.jpa.criteria.compile.CriteriaQueryTypeQueryAdapter";
    public static final int MAX_DEPT = 3;


    public void readData(Object data) {
        readData(data, 0);
    }

    protected int readData(Object data, int dept) {

        try {
            String tabulation = getTabulation(dept) + " ";

            if((data != null) ) { // && data.getClass().isAssignableFrom(arrayName)
                Object[] objects = new Object[1];
                try {
                    objects = (Object[]) data;
                } catch (Exception e) {
                    objects = new Object[] {(Object) data};
                }

                for (Object obj: objects){


                    if(!isWrapperType(obj.getClass())) {


                        for (Method method : obj.getClass().getDeclaredMethods()) {
                            method.setAccessible(true); // You might want to set modifier to public first.
                            System.out.println("M " + tabulation + method.getName() + "(" + getFormattedTypeParams(method) + ")");
                        }

                        for (Field field : obj.getClass().getDeclaredFields()) {
                            field.setAccessible(true); // You might want to set modifier to public first.
                            Object value = getValueWithExceptionHandling(obj, field);
                            if (value != null) {
                                System.out.println("F " + tabulation + field.getName() + "=" + value);
                            }
                            Class<? extends Field> clazz = field.getClass();
                            if (!isWrapperType(clazz)) {

                                if(dept < MAX_DEPT) {
                                    dept = readData(clazz, dept + 1);
                                }
                                if (clazz.getName().equals(CLASS_1)) {
                                    System.err.println("----------------------------------------------------------------------------------");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dept;
    }

    private Object getValueWithExceptionHandling(Object obj, Field field)  {
        Object value;
        try {
            value = field.get(obj);
        } catch (Exception e) {
            return null;
        }
        return value;
    }

    private String getFormattedTypeParams(Method method) {

        String str = "";
        Class<?>[] parameterTypes = method.getParameterTypes();

        for (int index = 0; index < parameterTypes.length; index++) {
            Class<?> type = parameterTypes[index];
            str += type.getSimpleName();
            if (index < parameterTypes.length-1) {
                str +=  ", ";
            }
        }

        return str;
    }


    private String getTabulation(int debt) {
        StringBuilder sb = new StringBuilder("");
        for (int length = 0; length < debt; length++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }
}
