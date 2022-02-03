package com.singingbush.dubclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.singingbush.dubclient.data.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
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
    public DubProject parseProjectFile(@NotNull File dubFile) {
        if (!dubFile.exists()) throw new IllegalArgumentException("dub file does not exist");
        if (!dubFile.isFile()) throw new IllegalArgumentException("dub file does not a valid file");
        if (!dubFile.canRead()) throw new IllegalArgumentException("dub file cannot be read");

        final DubFileParser parser = dubFile.getName().endsWith(".json") ? new JsonDubFileParser() : new SdlDubFileParser();

        try {
            return parser.parse(dubFile);
        } catch (final FileNotFoundException e) {
            log.error(String.format("The file '%s' was not found", dubFile.getName()), e);
        }
        return null; // todo: don't return null, throw a custom exception
    }

    @Override
    public DubProject parseDubJsonFile(@NotNull Reader reader) {
        final DubFileParser parser = new JsonDubFileParser();
        return parser.parse(reader);
    }

    @Override
    public DubProject parseDubSdlFile(@NotNull Reader reader) {
        final DubFileParser parser = new SdlDubFileParser();
        return parser.parse(reader);
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
    public VersionInfo packageInfo(@NotNull final String packageName, @NotNull final String version) throws DubRepositoryException {
        if (packageName.isEmpty() || version.isEmpty()) throw new IllegalArgumentException("args cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/%s/%s/info", repositoryUrl, packageName, version));

        return callApi(request, VersionInfo.class);
    }

    @Override
    public PackageStats packageStats(@NotNull final String packageName) throws DubRepositoryException {
        if (packageName.isEmpty()) throw new IllegalArgumentException("Package Name cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/%s/stats", repositoryUrl, packageName));

        return callApi(request, PackageStats.class);
    }

    @Override
    public DownloadStats packageStats(@NotNull final String packageName, @NotNull final String version) throws DubRepositoryException {
        if (packageName.isEmpty() || version.isEmpty()) throw new IllegalArgumentException("args cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/%s/%s/stats", repositoryUrl, packageName, version));

        return callApi(request, PackageStats.class).getDownloads();
    }

    @Override
    public String latestVersion(@NotNull final String packageName) throws DubRepositoryException {
        if (packageName.isEmpty()) throw new IllegalArgumentException("Package Name cannot be blank");

        final HttpUriRequest request = new HttpGet(String.format("%s/api/packages/%s/latest", repositoryUrl, packageName));

        return callApi(request, String.class);
    }

    private <T> T callApi(@NotNull final HttpUriRequest request, @NotNull final Class<T> clazz) throws DubRepositoryException {
        log.info("making HTTP request to " + request.getURI());

        try (final CloseableHttpResponse response = httpClient.execute(request)) {
            if(response != null) {
                final int status = response.getStatusLine().getStatusCode();
                final HttpEntity httpEntity = response.getEntity();
                if(httpEntity != null) {
                    if(status == 200) {
                        return parseResponse(httpEntity, clazz);
                    } else {
                        log.warn(String.format("DUB repository returned %s", status));
                        log.debug("Server status message: {}", parseResponse(httpEntity, ErrorMessage.class).getStatusMessage());
                    }
                }
            } else {
                log.warn("no response from DUB repository");
            }
        } catch (final IOException e) {
            log.error(e.getMessage());
        }

        throw new DubRepositoryException("");
    }

    private <T> T parseResponse(@NotNull final HttpEntity entity, @NotNull final Class<T> clazz) throws IOException, DubRepositoryException {
        try {
            return gson.fromJson(EntityUtils.toString(entity, Charset.forName("UTF-8")), clazz);
        } catch (final JsonSyntaxException e) {
            log.error("unable to parse the json response from the dub repository", e);
            throw new DubRepositoryException("the json response was not as expected", e);
        }
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
