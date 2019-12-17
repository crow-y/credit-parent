package tech.fullink.credit.tools.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.fullink.credit.common.exceptions.BaseErrorKeys;
import tech.fullink.credit.common.exceptions.ServerException;
import tech.fullink.credit.common.utils.DateUtil;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author Jamestang on 2017/11/7.
 */
@Data
@Slf4j
public class JwtService {

    /**
     * for Service  or tenant Token
     */
    public String createTenantToken(String secretKey,
                                    String envProfile, Date expireDate,
                                    String tokenType, String orgCode) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuedAt(DateUtil.getCurrentDate())
                    .withExpiresAt(expireDate)
                    .withIssuer(JwtConstant.JWT_TOKEN_ISSUER)
                    .withClaim(JwtClaim.orgCode, orgCode)
                    .withClaim(JwtClaim.type, tokenType)
                    .withClaim(JwtClaim.profile, envProfile)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException exception) {
            log.warn("UTF-8 encoding not supported ={}", exception);
            throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED);
        } catch (JWTCreationException exception) {
            log.warn("generator Jwt Token Creation error ={}", exception);
            throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED);
            //Invalid Signing configuration / Could not convert Claims.
        } catch (Exception ex) {
            log.warn("未知异常，异常信息：{}", ex);
            throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED);
        }
    }


    /**
     * for User Generator token
     */
    public String createUserToken(String secretKey,
                                  String envProfile, Date expireDate,
                                  String tokenType, String orgCode, String userID) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuedAt(DateUtil.getCurrentDate())
                    .withExpiresAt(expireDate)
                    .withIssuer(JwtConstant.JWT_TOKEN_ISSUER)
                    .withClaim(JwtClaim.userId, userID)
                    .withClaim(JwtClaim.type, tokenType)
                    .withClaim(JwtClaim.profile, envProfile)
                    .withClaim(JwtClaim.orgCode, orgCode)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException exception) {
            log.warn("UTF-8 encoding not supported ={}", exception);
            throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED);
        } catch (JWTCreationException exception) {
            log.warn("generator Jwt Token Creation error ={}", exception);
            throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED);
            //Invalid Signing configuration / Could not convert Claims.
        } catch (Exception ex) {
            log.warn("未知异常，异常信息：{}", ex);
            throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED);
        }
    }


    /**
     * @param jwtToken  请求 token
     * @param secretKey 秘钥
     */
    public DecodedJWT decryJwtToken(String jwtToken, String secretKey) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(JwtConstant.JWT_TOKEN_ISSUER)
                    .build(); //Reusable verifier instance
            return verifier.verify(jwtToken);
        } catch (UnsupportedEncodingException exception) {
            log.warn("token 字符集不支持:{},{},{}", exception, jwtToken, secretKey);
            throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED);
        } catch (JWTVerificationException exception) {
            log.warn("Token验证异常：{},{},{}", exception, jwtToken, secretKey);
            throw ServerException.fromKey(BaseErrorKeys.UNAUTHORIZED);
        }
    }


    public static void main(String[] args) {

    }

}
