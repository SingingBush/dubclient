package com.singingbush.dubclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.singingbush.dubclient.data.PackageInfo;
import com.singingbush.dubclient.data.PackageStats;
import com.singingbush.dubclient.data.SearchResult;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
class DubClientImpl implements DubClient {

    private static final Logger log = LoggerFactory.getLogger(DubClientImpl.class);

    private final String repositoryUrl;
    private final CloseableHttpClient httpClient;
    private final Gson gson;

    DubClientImpl(@NotNull String repositoryUrl,
                         long timeout,
                         @NotNull String userAgent) {
        this.repositoryUrl = repositoryUrl;

        httpClient = HttpClientBuilder.create()
            .setConnectionTimeToLive(timeout, TimeUnit.MILLISECONDS)
            .setUserAgent(userAgent)
            .build();

        gson = new GsonBuilder().create();
    }

    @Override
    public Stream<SearchResult> search(@NotNull final String text) throws DubRepositoryException {
        if (text.isEmpty()) throw new IllegalArgumentException("Search text cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/search?q=%s", repositoryUrl, text));

        return Stream.of(callApi(request, SearchResult[].class));
    }

    @Override
    public PackageInfo packageInfo(@NotNull final String packageName) throws DubRepositoryException {
        if (packageName.isEmpty()) throw new IllegalArgumentException("Package Name cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/%s/info", repositoryUrl, packageName));

        return callApi(request, PackageInfo.class);
    }

    @Override
    public PackageInfo packageInfo(@NotNull final String packageName, @NotNull final String version) throws DubRepositoryException {
        if (packageName.isEmpty() || version.isEmpty()) throw new IllegalArgumentException("args cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/%s/%s/info", repositoryUrl, packageName, version));

        return callApi(request, PackageInfo.class);
    }

    @Override
    public PackageStats packageStats(@NotNull final String packageName) throws DubRepositoryException {
        if (packageName.isEmpty()) throw new IllegalArgumentException("Package Name cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/%s/stats", repositoryUrl, packageName));

        return callApi(request, PackageStats.class);
    }

    @Override
    public PackageStats packageStats(@NotNull final String packageName, @NotNull final String version) throws DubRepositoryException {
        if (packageName.isEmpty() || version.isEmpty()) throw new IllegalArgumentException("args cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/%s/%s/stats", repositoryUrl, packageName, version));

        return callApi(request, PackageStats.class);
    }

    @Override
    public String latestVersion(@NotNull final String packageName) throws DubRepositoryException {
        if (packageName.isEmpty()) throw new IllegalArgumentException("Package Name cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/%s/latest", repositoryUrl, packageName));

        return callApi(request, String.class);
    }

    private <T> T callApi(final HttpUriRequest request, final Class<T> clazz) throws DubRepositoryException {
        log.info("making HTTP request to " + request.getURI());

        try (final CloseableHttpResponse response = httpClient.execute(request)) {
            if(response != null) {
                final int status = response.getStatusLine().getStatusCode();
                if(status == 200) {
                    return gson.fromJson(EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8")), clazz);
                } else {
                    log.warn(String.format("DUB repository returned %s", status));
                }
            } else {
                log.warn("no response from DUB repository");
            }
        } catch (final IOException e) {
            log.error(e.getMessage());
        }

        throw new DubRepositoryException("");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DubClientImpl{");
        sb.append("repositoryUrl='").append(repositoryUrl).append('\'');
        sb.append(", httpClient=").append(httpClient);
        sb.append(", gson=").append(gson);
        sb.append('}');
        return sb.toString();
    }
}
