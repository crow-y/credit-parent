package tech.fullink.credit.common.enums;

import lombok.Getter;

@Getter
public enum BaseCodeEnum {
    SUCCESS("000000","成功"),


    /* CLIENT_CONTINUE("100","继续"),
     CLIENT_SWITCHING_PROTOCOLS("101","转换协议"),
     CLIENT_PROCESSING("102",""),
 */
    REQUEST_OK ("200","正常"),
    REQUEST_CREATED("201","已创建"),
    REQUEST_ACCEPTED("202","接受"),
    REQUEST_NON_AUTHORITATIVE_INFORMATION("203","非官方信息"),
    REQUEST_NO_CONTENT("204","无内容"),
    REQUEST_RESET_CONTENT("205","重置内容"),
    REQUEST_PARTIAL_CONTENT("206","局部内容"),
    REQUEST_MULTI_STATUS("207",""),

   /* REDIRECT_MULTIPLE_CHOICES("300","多重选择"),
    REDIRECT_MOVED_PERMANENTLY("301","永久移除"),
    REDIRECT_MOVED_TEMPORARILY("302","临时移除"),
    REDIRECT_SEE_OTHER("303","参见其他信息"),
    REDIRECT_NOT_MODIFIED("304","未修正"),
    REDIRECT_USE_PROXY("305","使用代理"),
    REDIRECT_TEMPORARY_REDIRECT("306",""),*/

    REQUEST_BAD_REQUEST("001","错误请求"),
    REQUEST_UNAUTHORIZED("021","未授权"),
    REQUEST_FORBIDDEN("061","禁止"),
    REQUEST_NOT_FOUND("081","未找到"),
    REQUEST_METHOD_NOT_ALLOWED("101","方法未允许"),
    REQUEST_NOT_ACCEPTABLE("121","无法访问"),
    REQUEST_PROXY_AUTHENTICATION_REQUIRED("141","代理服务器认证要求"),
    REQUEST_REQUEST_TIMEOUT("161","请求超时"),
    REQUEST_CONFLICT("181","冲突"),
    REQUEST_GONE("201","已经不存在"),
    REQUEST_LENGTH_REQUIRED("221","需要数据长度"),
    REQUEST_PRECONDITION_FAILED("241","先决条件错误"),
    REQUEST_REQUEST_TOO_LONG("261","请求实体过大"),
    REQUEST_REQUEST_URI_TOO_LONG("281","请求URI过长"),
    REQUEST_UNSUPPORTED_MEDIA_TYPE("301","不支持的媒体格式"),
    REQUEST_REQUESTED_RANGE_NOT_SATISFIABLE("321","请求范围无法满足"),
    REQUEST_EXPECTATION_FAILED("341","期望失败"),


    RESPONSE_INTERNAL_SERVER_ERROR("500","内部服务器错误"),
    RESPONSE_NOT_IMPLEMENTED("601","未实现"),
    RESPONSE_BAD_GATEWAY("621","错误的网关"),
    RESPONSE_SERVICE_UNAVAILABLE("641","服务无法获得"),
    RESPONSE_GATEWAY_TIMEOUT("661","网关超时"),
    RESPONSE_HTTP_VERSION_NOT_SUPPORTED("681","不支持的 HTTP 版本"),


    REQUEST_ACCOUNT_LOCK("062", "账号被锁定"),


    ERROR("999999","系统错误"),



    TIMEOUT("199161","外部系统调用超时"),
    FORBIDDEN("199061","禁止访问"),
    BUSY("999003","资源忙"),
    TOKEN_ERROR("999004","TOKEN错误"),
    GATEWAY_TIMEOUT("",""),
    AUTHENTICATION_FAIL("999005","权限验证失败"),
    VALIDATION_FAIL("999006","参数校验异常"),//参数校验异常
    ILLEGALITY("999009","非法的请求"),

    TRACE_ID_EMPTY("999007","日志跟踪号为空"),
    TRACE_ID_LIMIT("999008","日志跟踪号长度超过限制"),
    BELONG_SYS_EMPTY("999009","归属系统标识为空"),
    BELONG_SYS_LIMIT("999010","归属系统标识长度超过限制"),
    RESOURCE_EMPTY("999011","资源不存在"),
    RESOURCE_LIMIT("999012","资源不唯一"),
    REQUEST_NOT_SUPPORT("999013","请求不支持"),
    BUSINESS_FLAG_EMPTY("999014","业务标识为空"),
    IP_LIMIT("999015","IP限制不允许访问"),
    RESOURCE_EXISTED("999016","资源已存在"),
    ;

    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String msg;

    BaseCodeEnum(String code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getBizCode(SysEnum sysEnum) {
        String belongSystem = sysEnum.getNumber();
        String bizPreFix;
        if (this == REQUEST_OK) {
            bizPreFix = "0";
        } else if (this.name().startsWith("REQUEST")) {
            bizPreFix = "1";
        } else if (this.name().startsWith("RESPONSE")) {
            bizPreFix = "2";
        } else {
            bizPreFix = "9";
        }
        return bizPreFix+belongSystem+code;
    }

}
