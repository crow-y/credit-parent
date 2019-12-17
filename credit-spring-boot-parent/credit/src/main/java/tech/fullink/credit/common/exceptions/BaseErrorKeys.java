package tech.fullink.credit.common.exceptions;

/**
 * @author crow
 */
public class BaseErrorKeys {
    private BaseErrorKeys() {}

    public static final String INITIALIZATION_ERROR = "initialization.error";
    public static final String BAD_REQUEST = "bad.request";
    public static final String NOT_FOUND = "not.found";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String METHOD_ARGUMENT_NOT_VALID = "method.argument.not.valid";
    public static final String MISSING_SERVLET_REQUEST_PARAMETER = "missing.servlet.request.parameter";
    public static final String MEDIA_TYPE_NOT_SUPPORTED = "media.type.not.supported";
    public static final String PARAM_UNRECOGNIZED = "param.unrecognized";
    public static final String PARAM_INVALID_FORMAT = "param.invalid.format";
    public static final String ILLEGAL_ARGUMENT = "illegal.argument";
    public static final String RATE_LIMIT_EXCEED = "rate.limit.exceed";
    public static final String SOCKET_EXCEPTION = "socket.exception";
    public static final String SOCKET_TIMEOUT = "socket.timeout";
    public static final String THIRD_PARTY_EXCEPTION = "third.party.exception";
    public static final String INTERNAL_SERVER_ERROR = "internal.server.error";
    public static final String IP_INVALID = "ip.invalid";
    public static final String SHUTTING = "shutting";
    public static final String BUSINESS_ERROR = "business.error";
}
