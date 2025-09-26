package it;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.singingbush.dubclient.DubClient;
import com.singingbush.dubclient.DubRepositoryException;
import com.singingbush.dubclient.data.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static it.ReflectionTestUtils.Assert.assertAllGettersNotNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Some of these tests use Wiremock with examples of real HTTP responses from the <a href="https://code.dlang.org">code.dlang.org</a> API.
 * From time to time the json should get updated to ensure it's inline with current state of the main dub server.
 *
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class DubClientIT {

    private DubClient client;
    private static final String HOSTNAME = "localhost";

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance()
        .options(wireMockConfig()
            .dynamicPort()
            .dynamicHttpsPort()
        )
        .build();

    @BeforeEach
    public void setUp() {
        this.client = DubClient.builder()
            .withScheme("http")
            .withHostname(HOSTNAME)
            .withPort(wiremock.getPort())
            .build();
    }

    @AfterEach
    public void tearDown() {
        this.client = null;
    }

    @ParameterizedTest
    @ValueSource(strings = { "dub.json/minimal.json", "dub.sdl/minimal.sdl" })
    @DisplayName("parse a minimal dub.json or dub.sdl file")
    @Timeout(value = 10L, unit = TimeUnit.MILLISECONDS)
    public void testParseMinimalProjectFile(final String testFile) throws URISyntaxException, FileNotFoundException {
        final File dubFile = Paths.get(this.getClass().getClassLoader().getResource(testFile).toURI()).toFile();

        final DubProject project = client.parseProjectFile(dubFile);

        assertEquals("myproject", project.getName());
        assertEquals("My first project", project.getDescription());
        assertEquals(Collections.singletonList("My Name"), project.getAuthors());
        assertEquals("Copyright © 2017, imadev", project.getCopyright());
        assertEquals("Boost", project.getLicense());
        assertTrue(project.getDependencies().isEmpty());
        assertTrue(project.getSubPackages().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "dub.json/average.json", "dub.sdl/average.sdl" })
    @DisplayName("parse an average dub.json or dub.sdl file")
    @Timeout(value = 10L, unit = TimeUnit.MILLISECONDS)
    public void testParseAverageProjectFile(final String testFile) throws URISyntaxException, FileNotFoundException {
        final File dubFile = Paths.get(this.getClass().getClassLoader().getResource(testFile).toURI()).toFile();

        final DubProject project = client.parseProjectFile(dubFile);

        assertEquals("myproject", project.getName());
        assertEquals("My first project", project.getDescription());
        assertEquals(Collections.singletonList("My Name"), project.getAuthors());
        assertEquals("Copyright © 2017, imadev", project.getCopyright());
        assertEquals("Boost", project.getLicense());
        assertFalse(project.getDependencies().isEmpty());
        assertEquals(2, project.getDependencies().size());
        assertFalse(project.getSubPackages().isEmpty());
        assertEquals(2, project.getSubPackages().size());
    }

    @ParameterizedTest
    @ValueSource(strings = { "dub.json/detailed.json", "dub.sdl/detailed.sdl" })
    @DisplayName("parse a detailed dub.json or dub.sdl file")
    @Timeout(value = 80L, unit = TimeUnit.MILLISECONDS)
    public void testParseDetailedProjectFile(final String testFile) throws URISyntaxException, FileNotFoundException {
        final File dubFile = Paths.get(this.getClass().getClassLoader().getResource(testFile).toURI()).toFile();

        final DubProject project = client.parseProjectFile(dubFile);

        assertEquals("myproject", project.getName());
        assertNotNull(project.getTargetType()); // doesn't have to be defined
        assertNotNull(project.getSourcePaths());
        assertNotNull(project.getImportPaths());
        assertEquals("My first project", project.getDescription());
        assertEquals(Collections.singletonList("My Name"), project.getAuthors());
        assertEquals("Copyright © 2017, imadev", project.getCopyright());
        assertEquals("Boost", project.getLicense());
        assertFalse(project.getDependencies().isEmpty());
        assertEquals(2, project.getDependencies().size());
        assertEquals(2, project.getSubPackages().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        ".dub/packages/mysql-native-3.1.0/mysql-native/dub.json",
        ".dub/packages/taggedalgebraic-0.11.22/taggedalgebraic/dub.json",
        ".dub/packages/vibe-d-0.9.4/vibe-d/dub.json",
        ".dub/packages/ddbc-0.5.4/ddbc/dub.json",
    })
    @DisplayName("parse example dub files from well known packages")
    @Timeout(value = 80L, unit = TimeUnit.MILLISECONDS)
    public void testParseRealProjectFiles(final String testFile) throws URISyntaxException, FileNotFoundException {
        final File dubFile = Paths.get(this.getClass().getClassLoader().getResource(testFile).toURI()).toFile();

        final DubProject project = client.parseProjectFile(dubFile);

        assertNotNull(project.getName());
        //assertNotNull(project.getTargetType()); // doesn't have to be defined
        assertNotNull(project.getSourcePaths());
        assertNotNull(project.getImportPaths());
        assertNotNull(project.getDescription());
        assertNotNull(project.getAuthors());
        //assertNotNull(project.getCopyright()); // DDBC doesn't define copyright
        assertNotNull(project.getLicense());
        //assertNotNull(project.getVersions());
        //assertNotNull(project.getBuildRequirements()); // not all the included projects specify build requirements
        assertNotNull(project.getAllDependencies());
        assertNotNull(project.getSubPackages());
        assertNotNull(project.getConfigurations());
        assertFalse(project.getConfigurations().isEmpty());
        //assertNotNull(project.getToolchainRequirements()); // not all the included projects specify toolchainRequirements
    }

    @Test
    @DisplayName("parse dub.json content via reader")
    @Timeout(value = 80L, unit = TimeUnit.MILLISECONDS)
    public void testParseDubJsonViaReader() throws URISyntaxException, IOException {
        final BufferedReader reader = Files.newBufferedReader(Paths.get(this.getClass().getClassLoader().getResource("dub.json/average.json").toURI()));

        final DubProject project = client.parseDubJsonFile(reader);

        assertEquals("myproject", project.getName());
        assertEquals("My first project", project.getDescription());
        assertEquals(Collections.singletonList("My Name"), project.getAuthors());
        assertEquals("Copyright © 2017, imadev", project.getCopyright());
        assertEquals("Boost", project.getLicense());
        assertFalse(project.getDependencies().isEmpty());
        assertEquals(2, project.getDependencies().size());
    }

    @Test
    @DisplayName("parse dub.sdl content via reader")
    @Timeout(value = 80L, unit = TimeUnit.MILLISECONDS)
    public void testParseDubSdlViaReader() throws URISyntaxException, IOException {
        final BufferedReader reader = Files.newBufferedReader(Paths.get(this.getClass().getClassLoader().getResource("dub.sdl/average.sdl").toURI()));

        final DubProject project = client.parseDubSdlFile(reader);

        assertEquals("myproject", project.getName());
        assertEquals("My first project", project.getDescription());
        assertEquals(Collections.singletonList("My Name"), project.getAuthors());
        assertEquals("Copyright © 2017, imadev", project.getCopyright());
        assertEquals("Boost", project.getLicense());
        assertFalse(project.getDependencies().isEmpty());
        assertEquals(2, project.getDependencies().size());
    }

    @Test
    @DisplayName("get latest version of a dependency and strip surrounding double-quotes")
    public void testGetLatestVersion() throws DubRepositoryException {
        wiremock.stubFor(get(urlPathEqualTo("/api/packages/d-unit/latest"))
            .withHeader("Accept", equalTo("application/json"))
            .withHost(equalTo(HOSTNAME))
            .willReturn(
                ok("\"0.10.2\"")
                    .withHeader("Content-Type", "application/json")
            ));

        final String result = client.latestVersion("d-unit");
        assertEquals("0.10.2", result);
    }

    @Test
    @DisplayName("get search results for querying 'unit'")
    public void testSearch() throws DubRepositoryException, URISyntaxException, IOException {
        final String searchResults = Files.readString(Paths.get(this.getClass().getClassLoader().getResource("search-results.json").toURI()));

        wiremock.stubFor(get(urlPathEqualTo("/api/packages/search")).withQueryParam("q", equalTo("unit"))
            .withHost(equalTo(HOSTNAME))
            .willReturn(
                ok(searchResults)
                    .withHeader("Content-Type", "application/json")
            ));

        final Stream<SearchResult> results = client.search("unit");

        assertTrue(results.allMatch(sr -> !sr.getName().isEmpty() && !sr.getDescription().isEmpty() && !sr.getVersion().isEmpty()));
    }

    @Test
    @DisplayName("get info for a package")
    public void testPackageInfo() throws DubRepositoryException, URISyntaxException, IOException {
        final String infoResponse = Files.readString(Paths.get(this.getClass().getClassLoader().getResource("info-response.json").toURI()));
        final String packageName = "vibe-d";

        wiremock.stubFor(get(urlPathEqualTo(String.format("/api/packages/%s/info", packageName)))
            .withHost(equalTo(HOSTNAME))
            .willReturn(
                ok(infoResponse)
                    .withHeader("Content-Type", "application/json")
            ));

        final PackageInfo info = client.packageInfo(packageName);

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
    @DisplayName("get info for a specific version of a package")
    public void testPackageVersionInfo() throws DubRepositoryException, URISyntaxException, IOException {
        final String infoResponse = Files.readString(Paths.get(this.getClass().getClassLoader().getResource("version-info.json").toURI()));
        final String packageName = "ddbc";
        final String packageVersion = "0.5.9";

        wiremock.stubFor(get(urlPathEqualTo(String.format("/api/packages/%s/%s/info", packageName, packageVersion)))
            .withHost(equalTo(HOSTNAME))
            .willReturn(
                ok(infoResponse)
                    .withHeader("Content-Type", "application/json")
            ));

        final VersionInfo info = client.packageInfo(packageName, packageVersion);

        assertNotNull(info);
        assertAllGettersNotNull(info);
        assertEquals("0.5.9", info.getVersion());
        assertEquals("blah", info.getReadme());
        assertTrue(info.getReadmeMarkdown());
        assertFalse(info.getCommitID().isEmpty());
        assertNotNull(info.getDate());
        assertEquals(OffsetDateTime.of(2023, 10, 23, 14, 27, 36, 0, ZoneOffset.UTC).toZonedDateTime(), info.getDate());
        assertEquals(ZonedDateTime.parse("2023-10-23T14:27:36Z"), info.getDate());

        assertAllGettersNotNull(info.getInfo());
    }

    @Test
    @DisplayName("get stats for a package")
    public void testPackageStats() throws DubRepositoryException, URISyntaxException, IOException {
        final String statsResponse = Files.readString(Paths.get(this.getClass().getClassLoader().getResource("stats-response.json").toURI()));

        final String packageName = "vibe-d";
        wiremock.stubFor(get(urlPathEqualTo(String.format("/api/packages/%s/stats", packageName)))
            .withHost(equalTo(HOSTNAME))
            .willReturn(
                ok(statsResponse)
                    .withHeader("Content-Type", "application/json")
            ));

        final PackageStats stats = client.packageStats(packageName);

        assertNotNull(stats.getUpdatedAt());

        final DownloadStats downloads = stats.getDownloads();
        assertNotNull(downloads);
        assertAllGettersNotNull(downloads);
        assertEquals(Integer.valueOf(1_547_952), downloads.getTotal());
        assertEquals(Integer.valueOf(26099), downloads.getMonthly());
        assertEquals(Integer.valueOf(17969), downloads.getWeekly());
        assertEquals(Integer.valueOf(3636), downloads.getDaily());

        final RepoStats repo = stats.getRepo();
        assertNotNull(repo);
        assertAllGettersNotNull(repo);
        assertEquals(Integer.valueOf(1190), repo.getStars());
        assertEquals(Integer.valueOf(58), repo.getWatchers());
        assertEquals(Integer.valueOf(281), repo.getForks());
        assertEquals(Integer.valueOf(438), repo.getIssues());

        assertNotNull(stats.getScore());
    }

    @Test
    @DisplayName("get stats for a specific version of a package")
    public void testPackageVersionStats() throws DubRepositoryException {
        final String packageName = "vibe-d";
        final String version = "0.8.1";

        wiremock.stubFor(get(urlPathEqualTo(String.format("/api/packages/%s/%s/stats", packageName, version)))
            .withHost(equalTo(HOSTNAME))
            .willReturn(
                ok("{\n" +
                    "downloads: {\n" +
                    "total: 26300,\n" +
                    "monthly: 0,\n" +
                    "weekly: 0,\n" +
                    "daily: 0\n" +
                    "}\n" +
                    "}")
                    .withHeader("Content-Type", "application/json")
            ));

        final DownloadStats downloads = client.packageStats(packageName, version);

        assertNotNull(downloads);
        assertAllGettersNotNull(downloads);
        assertEquals(Integer.valueOf(26_300), downloads.getTotal());
        assertEquals(Integer.valueOf(0), downloads.getMonthly());
        assertEquals(Integer.valueOf(0), downloads.getWeekly());
        assertEquals(Integer.valueOf(0), downloads.getDaily());
    }

    @Test
    @DisplayName("handle 404 response from dlang.org for non-existing package")
    public void testHandling404() {
        wiremock.stubFor(get(urlPathEqualTo("/api/packages/GARBAGE/info"))
            .withHost(equalTo(HOSTNAME))
            .willReturn(
                notFound()
                    .withBody("{\"statusMessage\":\"Package not found\"}")
                    .withHeader("Content-Type", "application/json")
            ));

        assertThrows(DubRepositoryException.class, () -> client.packageInfo("GARBAGE"));
    }

    @Test
    @DisplayName("handle 500 response from dlang.org")
    public void testHandlingServerError() {
        wiremock.stubFor(get(urlPathEqualTo("/api/packages/anything/info"))
            .withHost(equalTo(HOSTNAME))
            .willReturn(
                serverError()
            ));

        assertThrows(DubRepositoryException.class, () -> client.packageInfo("anything"));
    }
}
