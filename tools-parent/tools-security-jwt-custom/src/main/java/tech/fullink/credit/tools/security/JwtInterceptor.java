package tech.fullink.credit.tools.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tech.fullink.credit.common.constants.Correlation;
import tech.fullink.credit.common.exceptions.BaseErrorKeys;
import tech.fullink.credit.common.exceptions.ServerException;
import tech.fullink.credit.common.plugins.BaseRedisPlugin;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author changsheng on 2017/11/3.
 * @author crow on 2019/10/11
 */
@Slf4j
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Value("${token.key.base.secret.key:CLOUD}")
    private String secretKey;

    @Value("${user.token.valid.second:60}")
    private Integer userTokenValidSecond;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private BaseRedisPlugin baseRedisPlugin;


    private LoadingCache<String, DecodedJWT> tokenCache;

    @PostConstruct
    public void init() {
        tokenCache = CacheBuilder.newBuilder()
                .expireAfterWrite(userTokenValidSecond, TimeUnit.SECONDS)
                .maximumSize(1000)
                .build(new CacheLoader<String, DecodedJWT>() {
                    @Override
                    public DecodedJWT load(String token) throws Exception {
                        return jwtService.decryJwtToken(token, secretKey);
                    }
                });
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod && !HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            Method method = ((HandlerMethod) handler).getMethod();
            try {
                checkTokenScopes(method, request);
            } finally {
                response.setHeader("Access-Control-Allow-Origin", "*");
            }
        }
        return true;
    }


    public boolean isTokenBlocked(DecodedJWT dwt) {
        if (Strings.isNullOrEmpty(dwt.getClaim(JwtClaim.jti).asString())) {
            // block tokens without "jti"
            return true;
        }
        String value = dwt.getClaim(JwtClaim.orgCode).asString() + "|" + dwt.getClaim(JwtClaim.jti).asString();
        if (null != this.baseRedisPlugin) {
            Long score = this.baseRedisPlugin.zScore(redisKeyTokenBlacklist(), value);
            return score != null;
        }
        return false;

    }


    private String redisKeyTokenBlacklist() {
        return "tokens:blacklist";
    }


    private void checkTokenScopes(Method method, HttpServletRequest req) {
        log.debug("Authenticating user for request. path = {}", req.getPathInfo());
        TokenScope tokenScope = method.getAnnotation(TokenScope.class);
        if (tokenScope == null) {
            return;
        }

        //获取token
        String jwtToken = this.getToken(req);
        try {
            DecodedJWT dwt = tokenCache.get(jwtToken);

            String orgCode = dwt.getClaim(JwtClaim.orgCode).asString();
            if (StringUtils.isEmpty(orgCode)) {
                throw new RuntimeException("token 解签不含机构号");
            }

            String tokenType = dwt.getClaim(JwtClaim.type).asString();
            TokenType role = Arrays.stream(tokenScope.value())
                    .filter(authRole -> authRole.name().equalsIgnoreCase(tokenType))
                    .findAny().orElse(null);
            if (role != null) {
                if (TokenType.USER.name().equalsIgnoreCase(tokenType)) {
                    String userId = dwt.getClaim(JwtClaim.userId).asString();
                    if (StringUtils.isEmpty(orgCode)) {
                        throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED, "token 解签不含客户号");
                    }

                    if (isTokenBlocked(dwt)) {
                        log.warn("[USER TOKEN] 已经无效，token[{}]， jwt：[{}]", jwtToken);
                        throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED, "token验证不通过");
                    }

                    MDC.put(TokenMdcKeys.USER_ID, dwt.getClaim(JwtClaim.userId).asString());
                    MDC.put(TokenMdcKeys.ACCOUNT, dwt.getClaim(JwtClaim.loginNo).asString());
                    MDC.put(TokenMdcKeys.ORG_CODE, orgCode);

                    if (dwt.getClaim(JwtClaim.version) != null) {
                        MDC.put(TokenMdcKeys.VERSION, dwt.getClaim(JwtClaim.version).asString());
                    }
                    if (dwt.getClaim(JwtClaim.appChannel) != null) {
                        MDC.put(TokenMdcKeys.APP_CHANNEL, dwt.getClaim(JwtClaim.appChannel).asString());
                    }
                    if (dwt.getClaim(JwtClaim.appId) != null) {
                        MDC.put(TokenMdcKeys.APP_ID, dwt.getClaim(JwtClaim.appId).asString());
                    }
                    if (dwt.getClaim(JwtClaim.os) != null) {
                        MDC.put(TokenMdcKeys.OS, dwt.getClaim(JwtClaim.os).asString());
                    }
                    MDC.put(Correlation.MDC_CALL_FROM, "[" + tokenType + "-" + userId + "]");
                } else if (TokenType.TENANT.name().equalsIgnoreCase(tokenType)) {
                    String reqOrgCode = req.getParameter(TokenMdcKeys.ORG_CODE);
                    if (!StringUtils.isEmpty(reqOrgCode) && !reqOrgCode.equals(orgCode)) {
                        log.warn("token 解签机构号 和请求机构号不匹配 reqOrgCode is{}, tokenOrgCode is {}", reqOrgCode, orgCode);
                        throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED, "token 解签机构号 和请求机构号不匹配 reqOrgCode is{" + reqOrgCode + "}, tokenOrgCode is {" + orgCode + "}");
                    }
                    MDC.put(TokenMdcKeys.ORG_CODE, orgCode);
                    MDC.put(Correlation.MDC_CALL_FROM, "[" + orgCode + "]");
                } else if (TokenType.SERVICE.name().equalsIgnoreCase(tokenType)) {
                    MDC.put(Correlation.MDC_CALL_FROM, "[" + dwt.getClaim(JwtClaim.jti).asString() + "]");
                }
                return;
            }

            log.warn("token[{}]校验失败", jwtToken);
        } catch (Throwable e) {
            log.warn("调用cif校验token[{}]异常:{}", jwtToken, e);
        }
        throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED, "token验证不通过");
    }


    /**
     * 获取token令牌
     *
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(authHeader)) {
            log.warn("请求缺少头部:" + HttpHeaders.AUTHORIZATION);
            throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED, "请求缺少头部:" + HttpHeaders.AUTHORIZATION);
        }

        return getJwt(authHeader.trim());
    }


    /**
     * 截取有效token荷载, 去除Authorization前面的"Bearer "
     * @param token
     * @return
     */
    private String getJwt(String token) {
        if (token != null && !"".equals(token)) {
            token = token.trim();
            return token.indexOf(32) < 0 ? token : token.substring(token.lastIndexOf(' ') + 1, token.length());
        }
        log.info("token为空");
        return null;
    }

}
