package com.singingbush.dubclient;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class DubRepositoryException extends Throwable {

    public DubRepositoryException(final String message) {
        super(message);
    }

    public DubRepositoryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
