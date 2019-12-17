package tech.fullink.credit.common.utils;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author crow
 */
public class CommonUtil {

    private CommonUtil() {}

    private static final String DIGITS_PATTERN = "^\\d*$|^-\\d*$";

    /**
     * 生成uuid
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * 判断字符串是否是数字
     *
     * @param digits
     * @return
     */
    public static boolean isDigits(String digits) {
        if (null == digits || "".equals(digits)) {
            return false;
        }
        return Pattern.matches(DIGITS_PATTERN, digits);
    }

}
