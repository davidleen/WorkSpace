package com.giants3.utils;

/**
 * 字符串的功能类。
 */

public class StringUtils {



    public static boolean  isEmpty(String s)
    {
        return s==null||s.trim().length()==0;
    }

    public static final String row_separator="\n";
    public static final String spec_separator="*";

    public static final String spec_separator_pattern="\\*";

    public static final String STRING_SPLIT_SEMICOLON =";";

    public static final String STRING_SPLIT_DOT =".";
    public static final String PRODUCT_NAME_SPILT ="-";
    public static final String STRING_SPLIT_COMMA =",";
    public static final String PRODUCT_NAME_COMMA =STRING_SPLIT_COMMA;
















    public static int index(String[] array,String s)
    {

        int length = array.length;
        for (int i = 0; i < length; i++) {

            if(array[i].equals(s))
                return i;

        }
        return -1;
    }



    public static final String[] split(String string)
    {

        if(StringUtils.isEmpty(string)) return new String[]{};
        return string.split(STRING_SPLIT_SEMICOLON);

    }

    public static final String[] split(String string,String  separator)
    {

        if(StringUtils.isEmpty(string)) return new String[]{};
        return string.split(separator);

    }
    public static final String combine(String[] strings )
    {
        return combine(strings,STRING_SPLIT_SEMICOLON);
    }
    public static final String combine(String[] strings,String separator )
    {


        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0, stringsLength = strings.length; i < stringsLength; i++) {
            stringBuilder.append(strings[i]) ;
            if(i<stringsLength-1)
                stringBuilder.append(separator);
        }


        return stringBuilder.toString();
    } public static final String combine(int[] intArrays )
    {


        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0, intArraysLength = intArrays.length; i < intArraysLength; i++) {
            int s = intArrays[i];
            stringBuilder.append(s);
            if(i<intArraysLength-1)
                stringBuilder.append(STRING_SPLIT_SEMICOLON);
        }


        return stringBuilder.toString();
    }
    public   static final <T> String combine(T[] strings )
    {


        StringBuilder stringBuilder=new StringBuilder();
        for(T s:strings)
            stringBuilder.append(s).append(STRING_SPLIT_SEMICOLON);
        if(stringBuilder.length()>0)
            stringBuilder.setLength(stringBuilder.length()-1);

        return stringBuilder.toString();
    }

    public static final <T> String combine(Iterable<T> strings )
    {




        StringBuilder stringBuilder=new StringBuilder();
        for(T s:strings)
            stringBuilder.append(s).append(STRING_SPLIT_SEMICOLON);
        if(stringBuilder.length()>0)
            stringBuilder.setLength(stringBuilder.length()-1);

        return stringBuilder.toString();
    }

    /**
     * 比较字符串是否相同
     * @param memo
     * @param rem
     * @return
     */
    public static boolean compare(String memo, String rem) {

        if(memo==null&&rem==null)
            return true;
        if(memo==null) return false;
        return
                memo.equalsIgnoreCase(rem);

    }











    public static final String  SQL_LIKE="%";
    /**
     * 数据库查询  模糊处理
     * @param s
     * @return
     */
    public static String sqlLike(String s) {


        return SQL_LIKE+s.trim()+SQL_LIKE;

    }

    /**
     * 数据库查询  模糊处理
     * @param s
     * @return
     */
    public static String sqlLeftLike(String s) {


        return SQL_LIKE+s.trim();

    }  /**
     * 数据库查询  模糊处理
     * @param s
     * @return
     */
    public static String sqlRightLike(String s) {


        return s.trim()+SQL_LIKE;

    }


    /**
     * 对sql 日期数据字符串截断， 取年月日数据
     * @param value
     * @return
     */
    public static String clipSqlDateData(String value) {



        if(StringUtils.isEmpty(value)) return "";
        if(value.length()<10) return value;
        return value.substring(0,10);
    }

    public static boolean isChar(String  value, int index) {



        if(index>=0&&index<value.length()) {


            return isAlpha(value.charAt(index));



        }

        return false;


    }

    /**
     * 判断
     * @param aChar
     * @return
     */
    public static  boolean isAlpha(char aChar)
    {
        if((aChar>='a'&&aChar<='z' ) ||  (aChar>='A'&&aChar<='Z'))
        {

            return true;
        }
        return false;
    }

    /**
     * 是否选中的开头。
     * @param source
     * @param candidate
     * @return
     */
    public static boolean startsWithAny(String source, String... candidate) {

        if(source==null)return false;
        for(String temp:candidate)
        {
            if(source.startsWith(temp)) return true;
        }
        return false;
    }
}
