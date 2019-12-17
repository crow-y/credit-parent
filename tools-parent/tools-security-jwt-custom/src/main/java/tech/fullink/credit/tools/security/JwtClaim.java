package tech.fullink.credit.tools.security;

/**
 * @author Jamestang 2017/11/4.
 */
public interface JwtClaim {
    /**
     * 代表这个JWT的接收对象；
     */
    String aud = "aud";
    String jti = "jti";
    /**
     * 代表这个JWT的签发主体；
     */
    String iss = "iss";
    /**
     * 创建时间
     */
    String iat = "iat";
    /**
     * 失效时间
     */
    String exp = "exp";
    /**
     * 生效时间
     */
    String nbf = "nbf";
    String scope = "scope";
    // custom fields

    // ============== 自定义内容 ===================

    String orgCode = "orgCode";
    String userId = "userId";
    String loginNo = "loginNo";
    String type = "type";
    String productCode = "productCode";
    String channel = "channel";
    /**
     * dev ,test ,prod
     */
    String profile = "dev";

    String version = "version";
    String appId = "appId";
    String appChannel = "appChannel";
    String os = "os";


    //sub(Subject)：代表这个JWT的主体，即它的所有人；

    //aud(Audience)：代表这个JWT的接收对象；

    // exp(Expiration time)：是一个时间戳，代表这个JWT的过期时间；

    // nbf(Not Before)：是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的；

    // iat(Issued at)：是一个时间戳，代表这个JWT的签发时间；

    // jti(JWT ID)：是JWT的唯一标识。

    /**
     * secret version
     */
    String secVer = "secVer";
}
