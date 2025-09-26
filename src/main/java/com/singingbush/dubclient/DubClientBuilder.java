package com.singingbush.dubclient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.SSLContext;
import java.time.Duration;
import java.util.StringJoiner;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class DubClientBuilder {

    private String scheme = "https";
    private String hostName = "code.dlang.org";
    private int port = 443;
    private long timeout = 2_000;
    private String userAgent = "Java DUB Client";
    @Nullable
    private SSLContext sslContext;

    // should be created via the DubClient interface
    DubClientBuilder() {}

    public DubClientBuilder withScheme(@NotNull final String scheme) {
        this.scheme = scheme;
        return this;
    }

    public DubClientBuilder withHostname(@NotNull final String hostName) {
        this.hostName = hostName;
        return this;
    }

    public DubClientBuilder withPort(final int port) {
        this.port = port;
        return this;
    }

    public DubClientBuilder withTimeout(final long milliseconds) {
        this.timeout = milliseconds;
        return this;
    }

    public DubClientBuilder withTimeout(@NotNull final Duration duration) {
        return this.withTimeout(duration.toMillis());
    }

    public DubClientBuilder withUserAgent(@NotNull final String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public DubClientBuilder withSslContext(final SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public DubClient build() {
        return new DubClientImpl(scheme, hostName, port, timeout, userAgent, sslContext);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DubClientBuilder.class.getSimpleName() + "[", "]")
            .add("scheme='" + scheme + "'")
            .add("hostName='" + hostName + "'")
            .add("port=" + port)
            .add("timeout=" + timeout)
            .add("userAgent='" + userAgent + "'")
            .toString();
    }
}
