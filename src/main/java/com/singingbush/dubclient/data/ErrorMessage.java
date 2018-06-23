package com.singingbush.dubclient.data;

import java.util.Objects;

/**
 * @author Samael Bate (singingbush)
 * created on 20/06/18
 */
public class ErrorMessage {

    private String statusMessage;
    private String statusDebugMessage;

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getStatusDebugMessage() {
        return statusDebugMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ErrorMessage that = (ErrorMessage) o;
        return Objects.equals(statusMessage, that.statusMessage) &&
            Objects.equals(statusDebugMessage, that.statusDebugMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusMessage, statusDebugMessage);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorMessage{");
        sb.append("statusMessage='").append(statusMessage).append('\'');
        sb.append(", statusDebugMessage='").append(statusDebugMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
