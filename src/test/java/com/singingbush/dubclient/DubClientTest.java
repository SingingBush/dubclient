package com.singingbush.dubclient;

import com.singingbush.dubclient.data.SearchResult;
import it.ReflectionTestUtils;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
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
        MockitoAnnotations.openMocks(this);
        client = DubClient.builder().withUrl("http://localhost").build();

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
        final CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getCode()).thenReturn(status);

        if(json != null) {
            final HttpEntity entity = mock(HttpEntity.class);
            when(entity.getContent()).thenReturn(json);
            when(response.getEntity()).thenReturn(entity);
        }

        return response;
    }
}
