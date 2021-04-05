package com.giants3;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassHelper {


    public static  final <C> Class<C> findParameterizedClass(Class classWithPara,Class paraClass)
    {


        return findParameterizedClass(classWithPara,null,paraClass);



    }



    public static  final <C> Class<C> findParameterizedClass(Class classWithPara,Class absClassWithPara,Class paraClass)
    {

        if(classWithPara==null) return null;
        if(classWithPara.equals(absClassWithPara) ) return null;

        Type genericSuperclass = classWithPara.getGenericSuperclass();
        try {
            if (genericSuperclass instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                for(Type item:actualTypeArguments)
                {
                    if (item instanceof Class) {

                        boolean isChildClass=isChildClass((Class) item,paraClass);

                        if(isChildClass) {
                            Class<C> clas = (Class<C>) item;
                            return clas;
                        }

                    }
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return findParameterizedClass(classWithPara.getSuperclass(),absClassWithPara,paraClass);




    }


    public static boolean isChildClass(Class a, Class parent)
    {

        if(a==null) return false;
        if(a.equals(parent)) return true;
        Class[] interfaces = a.getInterfaces();
        if(interfaces!=null)
        {
            for (Class temp : interfaces) {
                if (temp.equals(parent)) {
                    return true;
                }
            }

        }
        return isChildClass(a.getSuperclass(),parent);

    }
}
