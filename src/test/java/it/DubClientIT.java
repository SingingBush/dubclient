package it;

import com.singingbush.dubclient.DubClient;
import com.singingbush.dubclient.DubRepositoryException;
import com.singingbush.dubclient.data.*;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.util.stream.Stream;

import static it.ReflectionTestUtils.Assert.assertAllGettersNotNull;
import static org.junit.Assert.*;

/**
 * These tests makes real HTTP calls to https://code.dlang.org API to ensure the code
 * works with the current REST API
 *
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class DubClientIT {

    private DubClient client;

    @Before
    public void setUp() throws Exception {
        client = DubClient.builder().build();
    }

    @Test(timeout = 5_000L)
    public void testCallToCodeDlangOrg() throws DubRepositoryException {
        final String dunit = client.latestVersion("d-unit");
        assertTrue(dunit.split("\\.").length > 1);

        client.latestVersion("dunit");
    }

    @Test(timeout = 5_000L)
    public void testSearch() throws DubRepositoryException {
        final Stream<SearchResult> results = client.search("unit");

        assertTrue(results.allMatch(sr -> !sr.getName().isEmpty() && !sr.getDescription().isEmpty() && !sr.getVersion().isEmpty()));
    }

    @Test(timeout = 5_000L)
    public void testPackageInfo() throws DubRepositoryException {
        final PackageInfo info = client.packageInfo("vibe-d");

        assertNotNull(info);

        assertAllGettersNotNull(info);
        assertAllGettersNotNull(info.getRepository());
        info.getVersions().parallelStream()
            .forEach(ReflectionTestUtils.Assert::assertAllGettersNotNull);

        assertEquals("vibe-d", info.getName());
        assertEquals(OffsetDateTime.of(2013, 1, 17, 11, 52, 21, 0, ZoneOffset.UTC).toZonedDateTime(), info.getDateAdded());
        assertEquals(ZonedDateTime.parse("2013-01-17T11:52:21Z"), info.getDateAdded());
    }

//    @Test(timeout = 5_000L)
//    public void testPackageInfoGitlab() throws DubRepositoryException {
//        // ensure projects on Gitlab don't have any weird quirks
//        final PackageInfo info = client.packageInfo("gitlab-test-package");
//
//        assertNotNull(info);
//        assertAllGettersNotNull(info);
//        assertAllGettersNotNull(info.getRepository());
//        info.getVersions().parallelStream()
//            .forEach(ReflectionTestUtils.Assert::assertAllGettersNotNull);
//
//        assertEquals("gitlab-test-package", info.getName());
//    }

    @Test(timeout = 5_000L)
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

    @Test(timeout = 5_000L)
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

    @Test(timeout = 5_000L)
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
