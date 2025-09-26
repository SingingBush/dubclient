package com.singingbush.dubclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.singingbush.dubclient.data.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
class DubClientImpl implements DubClient {

    private static final Logger log = LoggerFactory.getLogger(DubClientImpl.class);

    private static final String APPLICATION_JSON_VALUE = "application/json";

    private final String scheme;
    private final String hostName;
    private final int port;
    private final Gson gson;

    private final HttpClient httpClient;
    final HttpRequest.Builder requestBuilder;

    DubClientImpl(
        @NotNull final String scheme,
        @NotNull String hostName,
        int port,
        long timeout,
        @NotNull String userAgent,
        @Nullable final SSLContext sslContext
    ) {
        this.scheme = !scheme.isBlank() ? scheme : "https";
        this.hostName = !hostName.isBlank() ? hostName : "code.dlang.org";
        this.port = port;

        final HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.ALWAYS);

        if(sslContext != null) {
            httpClientBuilder.sslContext(sslContext);
        }

        this.httpClient = httpClientBuilder
            .build();

        this.requestBuilder = HttpRequest.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .timeout(Duration.of(timeout, MILLIS))
            .header("Content-Type", APPLICATION_JSON_VALUE)
            .header("Accept", APPLICATION_JSON_VALUE)
            .header("User-Agent", userAgent)
        //.header("Accept-Language", "en-GB")
        ;
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
        } catch (final IOException e) {
            log.error("IO exception parsing '{}'", dubFile.getName(), e);
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

        try {
            final HttpRequest request = this.makeGetRequest("/api/packages/search", String.format("q=%s", text));

            return Stream.of(gson.fromJson(this.sendRequest(request, HttpResponse.BodyHandlers.ofString()), SearchResult[].class));
        } catch (URISyntaxException | JsonSyntaxException e) {
            log.error("There was a problem building the URI", e);
            throw new DubRepositoryException("There was a problem building the URI", e);
        }
    }

    @Override
    public PackageInfo packageInfo(@NotNull final String packageName) throws DubRepositoryException {
        if (packageName.isEmpty()) throw new IllegalArgumentException("Package Name cannot be blank");

        try {
            final HttpRequest request = this.makeGetRequest(String.format("/api/packages/%s/info", packageName), null);

            return gson.fromJson(sendRequest(request, HttpResponse.BodyHandlers.ofString()), PackageInfo.class);
        } catch (URISyntaxException | JsonSyntaxException e) {
            log.error("There was a problem building the URI", e);
            throw new DubRepositoryException("There was a problem building the URI", e);
        }
    }

    @Override
    public VersionInfo packageInfo(@NotNull final String packageName, @NotNull final String version) throws DubRepositoryException {
        if (packageName.isEmpty() || version.isEmpty()) throw new IllegalArgumentException("args cannot be blank");

        try {
            final HttpRequest request = this.makeGetRequest(String.format("/api/packages/%s/%s/info", packageName, version), null);

            return gson.fromJson(sendRequest(request, HttpResponse.BodyHandlers.ofString()), VersionInfo.class);
        } catch (URISyntaxException | JsonSyntaxException e) {
            log.error("There was a problem building the URI", e);
            throw new DubRepositoryException("There was a problem building the URI", e);
        }
    }

    @Override
    public PackageStats packageStats(@NotNull final String packageName) throws DubRepositoryException {
        if (packageName.isEmpty()) throw new IllegalArgumentException("Package Name cannot be blank");

        try {
            final HttpRequest request = this.makeGetRequest(String.format("/api/packages/%s/stats", packageName), null);

            return gson.fromJson(sendRequest(request, HttpResponse.BodyHandlers.ofString()), PackageStats.class);
        } catch (URISyntaxException | JsonSyntaxException e) {
            log.error("There was a problem building the URI", e);
            throw new DubRepositoryException("There was a problem building the URI", e);
        }
    }

    @Override
    public DownloadStats packageStats(@NotNull final String packageName, @NotNull final String version) throws DubRepositoryException {
        if (packageName.isEmpty() || version.isEmpty()) throw new IllegalArgumentException("args cannot be blank");

        try {
            final HttpRequest request = this.makeGetRequest(String.format("/api/packages/%s/%s/stats", packageName, version), null);

            return gson.fromJson(sendRequest(request, HttpResponse.BodyHandlers.ofString()), PackageStats.class).getDownloads();
        } catch (URISyntaxException | JsonSyntaxException e) {
            log.error("There was a problem building the URI", e);
            throw new DubRepositoryException("There was a problem building the URI", e);
        }
    }

    @Override
    public String latestVersion(@NotNull final String packageName) throws DubRepositoryException {
        if (packageName.isEmpty()) throw new IllegalArgumentException("Package Name cannot be blank");

        try {
            final HttpRequest request = this.makeGetRequest(String.format("/api/packages/%s/latest", packageName), null);

            return sendRequest(request, HttpResponse.BodyHandlers.ofString()).replaceAll("\"", "");
        } catch (URISyntaxException | JsonSyntaxException e) {
            log.error("There was a problem building the URI", e);
            throw new DubRepositoryException("There was a problem building the URI", e);
        }
    }

    private HttpRequest makeGetRequest(@NotNull final String path, @Nullable final String query) throws URISyntaxException {
        return this.requestBuilder
            .uri(
                new URI(this.scheme, null, this.hostName, this.port, path, query, null)
            )
            .GET()
            .build();
    }

    private <T> T sendRequest(final HttpRequest request, final HttpResponse.BodyHandler<T> handler) throws DubRepositoryException {
        try {
            final HttpResponse<T> response = this.httpClient.send(request, handler);
            if(response.statusCode() == 200) {
                final T body = response.body();
                log.debug("Dub server responded with {}, body : {}", response.statusCode(), body);
                return body;
            } else {
                final String msg = String.format("Dub server responded with %s, body : %s", response.statusCode(), response.body());
                log.error(msg);

                // todo: handle error response payloads
//                if (response.body() != null) {
//                    gson.fromJson(response.body(), ErrorMessage.class).getStatusMessage();
//                }

                throw new DubRepositoryException(msg);
            }
        } catch (IOException | InterruptedException e) {
            throw new DubRepositoryException("Error calling "+this.hostName, e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DubClientImpl{");
        sb.append("hostName='").append(hostName).append('\'');
        sb.append(", httpClient=").append(httpClient);
        sb.append(", gson=").append(gson);
        sb.append('}');
        return sb.toString();
    }
}
