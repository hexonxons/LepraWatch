package org.koroed.lepra;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 26.05.2014
 * Time: 12:04
 */
public class LepraException extends Exception {
    public LepraException(String message) {
        super(message);
    }

    public LepraException(Throwable cause) {
        super(cause);
    }

    public LepraException(String message, Throwable cause) {
        super(message, cause);
    }

    public LepraException() {
    }
}
