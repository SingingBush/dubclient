package com.singingbush.dubclient;

import com.singingbush.dubclient.data.SearchResult;
import it.ReflectionTestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private CloseableHttpClient httpClient;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        client = DubClient.builder().build();

        ReflectionTestUtils.setField(client, "httpClient", httpClient);
    }

    @DisplayName("Successful Search")
    @Test
    public void testSearch() throws DubRepositoryException, IOException {
        final InputStream json = this.getClass().getClassLoader().getResourceAsStream("search-results.json");
        final CloseableHttpResponse response = mockResponse(200, json);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);

        final Stream<SearchResult> stream = client.search("test");

        final long count = stream.peek(r -> assertNotNull(r.getName())).count();
        assertEquals(85L, count, "There's 85 results in the test data");
    }

    @DisplayName("Search Handles Unexpected ResponseBody")
    @Test
    public void testSearchHandlesUnexpectedResponseBody() throws IOException {
        final InputStream json = new ByteArrayInputStream("something other than json".getBytes());
        final CloseableHttpResponse response = mockResponse(200, json);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);

        assertThrows(DubRepositoryException.class, () -> client.search("test"));
    }

    @DisplayName("Search Handles 500 response")
    @Test
    public void testSearchHandles500() throws IOException {
        final CloseableHttpResponse response = mockResponse(500, null);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);

        assertThrows(DubRepositoryException.class, () -> client.search("test"));
    }

    @DisplayName("Search Handles IOException")
    @Test
    public void testSearchHandlesIOException() throws IOException {
        when(httpClient.execute(any(HttpGet.class))).thenThrow(IOException.class);
        assertThrows(DubRepositoryException.class, () -> client.search("test"));
    }

    @DisplayName("Search Handles ClientProtocolException")
    @Test
    public void testSearchHandlesClientProtocolException() throws IOException {
        when(httpClient.execute(any(HttpGet.class))).thenThrow(ClientProtocolException.class);
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

    private CloseableHttpResponse mockResponse(final int status, final InputStream json) throws IOException {
        final StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(status);

        final CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(statusLine);

        if(json != null) {
            final HttpEntity entity = mock(HttpEntity.class);
            when(entity.getContent()).thenReturn(json);
            when(response.getEntity()).thenReturn(entity);
        }

        return response;
    }
}
