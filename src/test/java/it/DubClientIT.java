package it;

import com.singingbush.dubclient.DubClient;
import com.singingbush.dubclient.DubRepositoryException;
import com.singingbush.dubclient.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static it.ReflectionTestUtils.Assert.assertAllGettersNotNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests makes real HTTP calls to https://code.dlang.org API to ensure the code
 * works with the current REST API
 *
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class DubClientIT {

    private DubClient client;

    @BeforeEach
    public void setUp() throws Exception {
        client = DubClient.builder().build();
    }

    @Test
    @DisplayName("call dlang.org for latest dependency version")
    @Timeout(value = 3L, unit = TimeUnit.SECONDS)
    public void testCallToCodeDlangOrg() throws DubRepositoryException {
        final String dunit = client.latestVersion("d-unit");
        assertTrue(dunit.split("\\.").length > 1);

        client.latestVersion("dunit");
    }

    @Test
    @DisplayName("call dlang.org for search")
    @Timeout(value = 3L, unit = TimeUnit.SECONDS)
    public void testSearch() throws DubRepositoryException {
        final Stream<SearchResult> results = client.search("unit");

        assertTrue(results.allMatch(sr -> !sr.getName().isEmpty() && !sr.getDescription().isEmpty() && !sr.getVersion().isEmpty()));
    }

    @Test
    @DisplayName("call dlang.org for package info")
    @Timeout(value = 3L, unit = TimeUnit.SECONDS)
    public void testPackageInfo() throws DubRepositoryException {
        final PackageInfo info = client.packageInfo("vibe-d");

        assertNotNull(info);

        assertAllGettersNotNull(info);
        assertAllGettersNotNull(info.getRepository());
        info.getVersions().parallelStream()
            .forEach(v -> {
                assertNotNull(v.getName(), "Version should have Name");
                assertNotNull(v.getDescription(), "Version should have Description");
                assertNotNull(v.getVersion(), "Version should have Version Number");
                assertNotNull(v.getDate(), "Version should have Date");
                assertNotNull(v.getAuthors(), "Version should have Authors");
                //assertNotNull(v.getHomepage(), "Version should have Homepage");
                assertNotNull(v.getCopyright(), "Version should have Copyright");
                assertNotNull(v.getLicense(), "Version should have License");
                assertNotNull(v.getReadme(), "Version should have Readme");
                assertNotNull(v.getPackageDescriptionFile(), "Version should have Package Description File");
            });

        assertEquals("vibe-d", info.getName());
        assertEquals(OffsetDateTime.of(2013, 1, 17, 11, 52, 21, 0, ZoneOffset.UTC).toZonedDateTime(), info.getDateAdded());
        assertEquals(ZonedDateTime.parse("2013-01-17T11:52:21Z"), info.getDateAdded());
    }

    @Test
    @DisplayName("call dlang.org for package info (gitlab)")
    @Timeout(value = 3L, unit = TimeUnit.SECONDS)
    public void testPackageInfoGitlab() throws DubRepositoryException {
        // ensure projects on Gitlab don't have any weird quirks
        final PackageInfo info = client.packageInfo("ggplotd-cli");

        assertNotNull(info);
        assertAllGettersNotNull(info);
        assertAllGettersNotNull(info.getRepository());
        info.getVersions().parallelStream()
            .forEach(v -> {
                assertNotNull(v.getName(),"Version should have Name");
                assertNotNull(v.getDescription(),"Version should have Description");
                assertNotNull(v.getVersion(),"Version should have Version Number");
                assertNotNull(v.getDate(),"Version should have Date");
                assertNotNull(v.getAuthors(), "Version should have Authors");
                //assertNotNull(v.getHomepage(), "Version should have Homepage");
                assertNotNull(v.getCopyright(), "Version should have Copyright");
                assertNotNull(v.getLicense(), "Version should have License");
                assertNotNull(v.getReadme(), "Version should have Readme");
                assertNotNull(v.getPackageDescriptionFile(), "Version should have Package Description File");
            });

        assertEquals("ggplotd-cli", info.getName());

        assertEquals(OffsetDateTime.of(2021, 4, 8, 6, 32, 59, 0, ZoneOffset.UTC).toZonedDateTime(), info.getDateAdded());
        assertEquals(ZonedDateTime.parse("2021-04-08T06:32:59Z"), info.getDateAdded());
    }

    @Test
    @DisplayName("call dlang.org for package version info")
    @Timeout(value = 3L, unit = TimeUnit.SECONDS)
    public void testPackageVersionInfo() throws DubRepositoryException {
        final VersionInfo info = client.packageInfo("ddbc", "0.2.4");

        assertNotNull(info);
        assertAllGettersNotNull(info);
        assertEquals("0.2.4", info.getVersion());
        assertFalse(info.getReadme().isEmpty());
        assertNotNull(info.getReadmeMarkdown());
        assertFalse(info.getCommitID().isEmpty());
        assertNotNull(info.getDate());
        assertEquals(OffsetDateTime.of(2013, 4, 17, 9, 38, 57, 0, ZoneOffset.UTC).toZonedDateTime(), info.getDate());
        assertEquals(ZonedDateTime.parse("2013-04-17T09:38:57Z"), info.getDate());

        assertAllGettersNotNull(info.getInfo());
    }

    @Test
    @DisplayName("call dlang.org for package stats")
    @Timeout(value = 3L, unit = TimeUnit.SECONDS)
    public void testPackageStats() throws DubRepositoryException {
        final PackageStats stats = client.packageStats("vibe-d");

        assertNotNull(stats.getUpdatedAt());

        final DownloadStats downloads = stats.getDownloads();
        assertNotNull(downloads);
        assertAllGettersNotNull(downloads);
        assertTrue(downloads.getTotal() > 240_343);
        assertNotNull(downloads.getMonthly());
        assertNotNull(downloads.getWeekly());
        assertNotNull(downloads.getDaily());

        final RepoStats repo = stats.getRepo();
        assertNotNull(repo);
        assertAllGettersNotNull(repo);
        assertNotNull(repo.getStars());
        assertNotNull(repo.getWatchers());
        assertNotNull(repo.getForks());
        assertNotNull(repo.getIssues());

        assertNotNull(stats.getScore());
    }

    @Test
    @DisplayName("call dlang.org for package version stats")
    @Timeout(value = 3L, unit = TimeUnit.SECONDS)
    public void testPackageVersionStats() throws DubRepositoryException {
        final DownloadStats downloads = client.packageStats("vibe-d", "0.8.1");

        assertNotNull(downloads);
        assertAllGettersNotNull(downloads);
        assertTrue(downloads.getTotal() > 26_000);
        assertNotNull(downloads.getMonthly());
        assertNotNull(downloads.getWeekly());
        assertNotNull(downloads.getDaily());
    }
}
