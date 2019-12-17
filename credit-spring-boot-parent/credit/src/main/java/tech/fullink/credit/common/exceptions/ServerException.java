package tech.fullink.credit.common.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author crow
 */
@Data
@NoArgsConstructor
public class ServerException extends RuntimeException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String key, String... args) {
        super("业务异常");
        this.errorKey = key;
        this.args = args;
    }

    private String errorKey;
    private String[] args;

    public static ServerException fromKey(String key, String... args) {
        return new ServerException(key, args);
    }

}
