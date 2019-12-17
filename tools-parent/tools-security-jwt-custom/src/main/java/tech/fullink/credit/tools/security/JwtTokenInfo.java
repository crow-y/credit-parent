package tech.fullink.credit.tools.security;


import java.util.Map;

/**
 * @author Jamestang on 2017/11/4.
 */
public class JwtTokenInfo extends BaseTokenInfo {
    /**
     * token荷载内容
     */
    private Map<String, Object> claims;

    public JwtTokenInfo(Map<String, Object> claims) {
        this.claims = claims;
    }

    public String getTokenType() {
        return (String) claims.get(JwtClaim.type);
    }

    public String getUserId() {
        return (String) claims.get(JwtClaim.userId);
    }

    public String getOrgCode() {
        return (String) claims.get(JwtClaim.orgCode);
    }

    public String getAudience() {
        return (String) claims.get(JwtClaim.aud);
    }

    public String getJwtID() {
        return (String) claims.get(JwtClaim.jti);
    }

    public String getProductCode() {
        return (String) claims.get(JwtClaim.productCode);
    }

    public String getChannel() {
        return (String) claims.get(JwtClaim.channel);
    }

    public String getLoginNo() {
        return (String) claims.get(JwtClaim.loginNo);
    }

    public Integer getExpirationTime() {
        return (Integer) claims.get(JwtClaim.exp);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() / 1000 > getExpirationTime();
    }

    @Override
    public boolean isUser() {
        return getTokenType() != null && getTokenType().equalsIgnoreCase(TokenType.USER.name());
    }

    @Override
    public boolean isService() {
        return getTokenType() != null && getTokenType().equalsIgnoreCase(TokenType.SERVICE.name());
    }

    public boolean isTenant() {
        if (null != getTokenType()) {
            if (getTokenType().equalsIgnoreCase(TokenType.TENANT.name())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isClient() {
        return getTokenType() != null && getTokenType().equalsIgnoreCase(TokenType.CLIENT.name());
    }

    public Map<String, Object> getClaims() {
        return claims;
    }
}
