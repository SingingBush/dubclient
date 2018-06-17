package it;

import com.singingbush.dubclient.DubClient;
import com.singingbush.dubclient.DubRepositoryException;
import com.singingbush.dubclient.data.DownloadStats;
import com.singingbush.dubclient.data.PackageInfo;
import com.singingbush.dubclient.data.PackageStats;
import com.singingbush.dubclient.data.RepoStats;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * These tests make HTTP calls to https://code.dlang.org
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class DubClientIT {

    @Test
    public void testCallToCodeDlangOrg() throws DubRepositoryException {
        final DubClient client = DubClient.builder().build();

        final String dunit = client.latestVersion("d-unit");
        assertTrue(dunit.split("\\.").length > 1);

        client.latestVersion("dunit");
    }

    @Test
    public void testPackageInfo() throws DubRepositoryException {
        final DubClient client = DubClient.builder().build();

        final PackageInfo info = client.packageInfo("hibernated");

        assertEquals("hibernated", info.getName());

        assertEquals(LocalDateTime.of(2013, 4, 17, 11, 37, 9), info.getDateAdded());
    }

    @Test
    public void testPackageStats() throws DubRepositoryException {
        final DubClient client = DubClient.builder().build();

        final PackageStats stats = client.packageStats("vibe-d");

        final DownloadStats downloads = stats.getDownloads();
        assertNotNull(downloads);
        assertTrue(downloads.getTotal() > 240_343);

        final RepoStats repo = stats.getRepo();
        assertNotNull(repo);
    }
}
