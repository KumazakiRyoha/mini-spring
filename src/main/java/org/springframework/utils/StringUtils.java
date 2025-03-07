package org.springframework.utils;

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     * 
     * @param str 待检查的字符串
     * @return 如果字符串为null或空字符串，则返回true；否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否为空或仅包含空白字符
     * 
     * @param str 待检查的字符串
     * @return 如果字符串为null、空字符串或仅包含空白字符，则返回true；否则返回false
     */
    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     * 
     * @param str 待检查的字符串
     * @return 如果字符串不为null且不为空字符串，则返回true；否则返回false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否不为空且不仅包含空白字符
     * 
     * @param str 待检查的字符串
     * @return 如果字符串不为null、不为空字符串且不仅包含空白字符，则返回true；否则返回false
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 将字符串的第一个字母转为小写
     * 
     * @param str 待处理的字符串
     * @return 首字母小写的字符串，如果输入为null或空字符串，则原样返回
     */
    public static String lowerFirst(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char[] chars = str.toCharArray();
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            chars[0] += 32; // 大写字母转小写字母(ASCII码相差32)
        }
        return String.valueOf(chars);
    }

    // 或者使用Java内置方法实现的替代版本
    public static String lowerFirstAlt(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}
