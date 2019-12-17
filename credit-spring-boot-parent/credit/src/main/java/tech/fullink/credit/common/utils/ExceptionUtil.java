package tech.fullink.credit.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author crow
 */
@Slf4j
public class ExceptionUtil {

    private ExceptionUtil() {}

    /**
     * ServerException堆栈摘要
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T extends Exception>  String serverExceptionTrack(T t) {
        return ExceptionUtil.shortTrack(t, 1, 1);
    }


    /**
     * FeignException堆栈摘要
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T extends Exception>  String feignExceptionTrack(T t) {
        return ExceptionUtil.shortTrack(t, 6, 1);
    }


    /**
     * 堆栈摘要
     *
     * @param t
     * @param <T>
     * @param startIndex
     * @param lines
     * @return
     */
    public static <T extends Exception>  String shortTrack(T t, int startIndex, int lines) {
        StackTraceElement[] traceElements = t.getStackTrace();
        if (null == traceElements || traceElements.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < startIndex + lines; i++) {
            StackTraceElement traceElement = traceElements[i];
            builder.append("\t").append("at ").append(traceElement.getClassName())
                    .append(".").append(traceElement.getMethodName())
                    .append("(").append(traceElement.getFileName())
                    .append(":").append(traceElement.getLineNumber())
                    .append(")").append("\r\n");
        }
        return builder.toString();
    }




}
