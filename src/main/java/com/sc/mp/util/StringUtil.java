package com.sc.mp.util;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	/**
     * 空字符串
     */
    private static final String NULLSTR = "";
    

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null || "".equals(object);
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }
	public static boolean isChinese(char c){
		return (c >= '一') && (c <= 40869);
	}
	/**
	 * 判断字符串是否是中文
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str){
	    if (str == null) {
	      return false;
	    }
	    char[] arrayOfChar;
	    int j = (arrayOfChar = str.toCharArray()).length;
	    for (int i = 0; i < j; i++){
	    	char c = arrayOfChar[i];
    		if (isChinese(c)) {
		        return true;
    		}
    	}
	    return false;
	}	
	
	/**
	 *中文字截取
	 * @param text
	 * @param length
	 * @param encode
	 * @return
	 */
	public static String substring(String text, int length, String encode){
		StringBuilder sb = new StringBuilder();
		if (text == null) {
		   return "";
		}
		try {
			if(text.getBytes(encode).length<=length){
				return text;
			}
			int currentLength = 0;
			for (char c : text.toCharArray()) {
				currentLength += String.valueOf(c).getBytes(encode).length;
				if (currentLength <= length) {
					sb.append(c);
				} else {
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			 sb = new StringBuilder();
		}
		return sb.toString();
	}

	/**
	 * 解析String 为 Boolean
	 * @param str
	 * @return  true : "true"
	 * 			<br/> null : null or "all"
	 * 			<br/> false : 其他
	 */
	public static Boolean parseBoolean(String str){
		if(str!=null){
			if(!"all".equalsIgnoreCase(str)){
				return new Boolean(str);
			}
		}
		return null;
	}
	/**
	 * List转化为String
	 * 
	 * */
	public static String listToString(List<String> list){
		 
		   if(list==null){
		      return null;
		   }
		 
		   StringBuilder result = new StringBuilder();
		   boolean first = true;
		 
		   //第一个前面不拼接","
		   for(String string :list) {
		      if(first) {
		         first=false;
		      }else{
		         result.append(",");
		      }
		      result.append(string);
		   }
		   return result.toString();
		}

	/**
	 * 前面补0
	 */
	public static String addZeroForStr(String str, int strLength) {
	    int strLen = str.length();
	    if (strLen < strLength) {
	        while (strLen < strLength) {
	            StringBuffer sb = new StringBuffer();
	            sb.append("0").append(str);// 左补0
	            // sb.append(str).append("0");//右补0
	            str = sb.toString();
	            strLen = str.length();
	        }
	    }
	    return str;
	}
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
