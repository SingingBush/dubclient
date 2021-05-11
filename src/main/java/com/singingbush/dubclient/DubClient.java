package com.singingbush.dubclient;

import com.singingbush.dubclient.data.DownloadStats;
import com.singingbush.dubclient.data.PackageInfo;
import com.singingbush.dubclient.data.PackageStats;
import com.singingbush.dubclient.data.SearchResult;
import com.singingbush.dubclient.data.VersionInfo;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public interface DubClient {

    /**
     * make call to https://code.dlang.org/api/packages/search?q=
     * @param text string to search for
     * @return a Stream created from the SearchResult[] that the API returns
     * @throws DubRepositoryException if a problem occurs
     */
    Stream<SearchResult> search(@NotNull String text) throws DubRepositoryException;

    /**
     * make a call to https://code.dlang.org/api/packages/{package}/info
     * @param packageName The name of a package to look for
     * @return Information about the latest version of the given package
     * @throws DubRepositoryException if a problem occurs
     */
    PackageInfo packageInfo(@NotNull String packageName) throws DubRepositoryException;

    /**
     * make a call to https://code.dlang.org/api/packages/{package}/{version}/info
     * @param packageName The name of a package to look for
     * @param version The version number for the given package
     * @return Information about the specified version of the given package
     * @throws DubRepositoryException if a problem occurs
     */
    VersionInfo packageInfo(@NotNull String packageName, @NotNull String version) throws DubRepositoryException;

    /**
     * make a call to https://code.dlang.org/api/packages/{package}/stats
     * @param packageName The name of a package to look for
     * @return Statistics about the latest version of the given package
     * @throws DubRepositoryException if a problem occurs
     */
    PackageStats packageStats(@NotNull String packageName) throws DubRepositoryException;

    /**
     * make a call to https://code.dlang.org/api/packages/{package}/{version}/stats
     * @param packageName The name of a package to look for
     * @param version The version number for the given package
     * @return Statistics about downloads for the specified version of the given package
     * @throws DubRepositoryException if a problem occurs
     */
    DownloadStats packageStats(@NotNull String packageName, @NotNull String version) throws DubRepositoryException;

    /**
     * make a call to https://code.dlang.org/api/packages/{package}/latest
     * @param packageName The name of a package to look for
     * @return The version number for the latest release of the given package
     * @throws DubRepositoryException if a problem occurs
     */
    String latestVersion(@NotNull String packageName) throws DubRepositoryException;

    static DubClientBuilder builder() {
        return new DubClientBuilder();
    }
}
