package com.giants3.hd.utils;

/**
 * 数组方法的功能类
 */

import java.util.*;


/**
 * 数组相关
 * 算法类
 *
 * @author david
 */
public class ArrayUtils {
    private static final String TAG = ArrayUtils.class.getName();

    /**
     * 封装 对列表数据进行排序
     *
     * @param <T>
     * @param <T>
     * @param datas
     * @param comparator
     */

    @SuppressWarnings("unchecked")
    public static <T> void SortList(List<T> datas, Comparator<T> comparator) {

        if (datas == null || comparator == null)
            return;

        int size = datas.size();
        T[] array = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = datas.get(i);
        }

        Arrays.sort(array, comparator);

        datas.clear();
        for (int i = 0; i < size; i++) {
            datas.add(array[i]);
        }




    }

    /**
     * 封装 由数组转换成 列表
     *
     * @param <T>
     * @param array
     */

    public static <T> List<T> changeArrayToList(T[] array) {

        List<T> list = new ArrayList<T>();

        if (array != null && array.length > 0) {
            for (T data : array) {
                list.add(data);
            }
        }

        return list;

    }

    /**
     * 封装 由列表转换成数组
     *
     * @param <T>
     * @param list
     * @deprecated  泛类型转换失败。
     */

    public static <T> T[] changeArrayToList(List<T> list) {

        int size = list.size();
        T[] result =   (T[])new    Object[size];


        for (int i = 0; i < size; i++) {
            result[i] = list.get(i);
        }


        return result;

    }

    public static boolean isNotEmpty(List array) {
        return array!=null&&array.size()>0;
    }

    public static boolean isEmpty(List  list
    ) {
        return list==null||list.size()==0;
    }


    /**
     * 将字符数组转换成带分隔符的字符串
     * @param fields
     * @param divider
     * @return
     */
    public static String toDividerString(String[] fields, String divider) {


        int length=fields==null?0:fields.length;

        if(length==0) return "";
        temp.setLength(0);

        for(int i=0;i<length-1;i++)
        {
            temp.append(fields[i]).append(divider);


        }
        temp.append(fields[length-1]);

        return temp.toString();


    }


    /**
     * 字符串拼接容器 避免大量+ 处理
     */
    private static final StringBuilder temp=new StringBuilder(1000);
    /**
     * 将字符数组转换成带分隔符的字符串
     * @param lists
     * @param divider
     * @return
     */
    public static String toDividerString(List<String> lists, String divider) {


        int length=lists==null?0:lists.size();

        if(length==0) return "";
        temp.setLength(0);


        for(int i=0;i<length-1;i++)
        {
            temp.append(lists.get(i)).append(divider);


        }
        temp.append(lists.get(length-1));

        return temp.toString();


    }

    /**
     * 返回 给定值在数组中的位置
     * @param array
     * @param value
     * @param <T>
     * @return
     */
    public static <T> int indexOnArray(T[] array,T value)
    {

        int length = array.length;

        for (int i = 0; i < length; i++) {
            if(value.equals(array[i]))
                return i;
        }
        return -1;
    }

    public  static  <T>  Vector<T> changeListToVector(List<T> list) {

         int size=list.size();
        Vector<T> vector=new Vector<>(size);
        vector.addAll(list);
        return vector;
    }

    public static <T> void sortList(List<T> processes, Comparator  comparator) {



        final int size = processes.size();
        Object[] processArray=new Object[size];
        for (int i = 0; i < size; i++) {
            processArray[i]=processes.get(i);
        }
        Arrays.sort(processArray,comparator);
        processes.clear();
        for (int i = 0; i < size; i++) {

            processes.add((T)processArray[i]);
        }


    }
}

