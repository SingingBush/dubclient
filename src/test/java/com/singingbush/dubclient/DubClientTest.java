package com.singingbush.dubclient;

import com.singingbush.dubclient.data.SearchResult;
import it.ReflectionTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Samael Bate (singingbush)
 * created on 20/06/18
 */
public class DubClientTest {

    private DubClient client;

    @Mock
    private HttpClient httpClient;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        client = DubClient.builder()
            .withScheme("http")
            .withHostname("localhost")
            .build();

        ReflectionTestUtils.setField(client, "httpClient", httpClient);
    }

    @DisplayName("Successful Search")
    @Test
    public void testSearch() throws DubRepositoryException, IOException, InterruptedException {
        final InputStream json = this.getClass().getClassLoader().getResourceAsStream("search-results.json");
        final HttpResponse<Object> response = mockResponse(200, new String(json.readAllBytes(), StandardCharsets.UTF_8));
        when(httpClient.send(any(HttpRequest.class), any())).thenReturn(response);

        final Stream<SearchResult> stream = client.search("test");

        final long count = stream.peek(r -> assertNotNull(r.getName())).count();
        assertEquals(85L, count, "There's 85 results in the test data");
    }

    @DisplayName("Search Handles Unexpected ResponseBody")
    @Test
    public void testSearchHandlesUnexpectedResponseBody() throws IOException, InterruptedException {
        final InputStream json = new ByteArrayInputStream("something other than json".getBytes());
        final HttpResponse<Object> response = mockResponse(200, new String(json.readAllBytes(), StandardCharsets.UTF_8));
        when(httpClient.send(any(HttpRequest.class), any())).thenReturn(response);

        assertThrows(DubRepositoryException.class, () -> client.search("test"));
    }

    @DisplayName("Search Handles 500 response")
    @Test
    public void testSearchHandles500() throws IOException, InterruptedException {
        final HttpResponse<Object> response = mockResponse(500, null);
        when(httpClient.send(any(HttpRequest.class), any())).thenReturn(response);

        assertThrows(DubRepositoryException.class, () -> client.search("test"));
    }

    @DisplayName("Search Handles IOException")
    @Test
    public void testSearchHandlesIOException() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any())).thenThrow(IOException.class);
        assertThrows(DubRepositoryException.class, () -> client.search("test"));
    }

    @DisplayName("Search Handles ClientProtocolException")
    @Test
    public void testSearchHandlesClientProtocolException() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any())).thenThrow(InterruptedException.class);
        assertThrows(DubRepositoryException.class, () -> client.search("test"));
    }

//    @Test
//    public void packageInfo() {
//    }
//
//    @Test
//    public void packageVersionInfo() {
//    }
//
//    @Test
//    public void packageStats() {
//    }
//
//    @Test
//    public void packageVersionStats() {
//    }
//
//    @Test
//    public void latestVersion() {
//    }

    private HttpResponse<Object> mockResponse(final int status, final String body) {
        @SuppressWarnings("unchecked")
        final HttpResponse<Object> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(status);

        if(body != null) {
            when(response.body()).thenReturn(body);
        }

        return response;
    }
}
