package com.singingbush.dubclient;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class DubClientBuilder {

    private String repositoryUrl = "https://code.dlang.org";
    private long timeout = 5_000;
    private String userAgent = "Java DUB Client";

    // should be created via the DubClient interface
    DubClientBuilder() {}

    public DubClientBuilder withUrl(final String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
        return this;
    }

    public DubClientBuilder withTimeout(final long milliseconds) {
        this.timeout = milliseconds;
        return this;
    }

    public DubClientBuilder withUserAgent(final String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public DubClient build() {
        return new DubClientImpl(repositoryUrl, timeout, userAgent);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DubClientBuilder{");
        sb.append("repositoryUrl='").append(repositoryUrl).append('\'');
        sb.append(", timeout=").append(timeout);
        sb.append(", userAgent='").append(userAgent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
