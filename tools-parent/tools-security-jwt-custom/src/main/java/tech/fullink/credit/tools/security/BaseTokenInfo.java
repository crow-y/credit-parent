package tech.fullink.credit.tools.security;

/**
 *
 * @author Jamestang 2017/11/4.
 */
public abstract class BaseTokenInfo {
    public BaseTokenInfo() {
    }

    public boolean isUser() {
        return false;
    }

    public boolean isService() {
        return false;
    }

    public boolean isClient() {
        return false;
    }
}