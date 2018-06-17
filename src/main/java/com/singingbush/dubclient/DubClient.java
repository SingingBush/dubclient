package com.singingbush.dubclient;

import com.singingbush.dubclient.data.PackageInfo;
import com.singingbush.dubclient.data.PackageStats;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public interface DubClient {

    /**
     * make a call to https://code.dlang.org/api/packages/{package}/info
     */
    PackageInfo packageInfo(final String packageName) throws DubRepositoryException;

    /**
     * make a call to https://code.dlang.org/api/packages/{package}/stats
     */
    PackageStats packageStats(final String packageName) throws DubRepositoryException;

    /**
     * make a call to https://code.dlang.org/api/packages/{package}/latest
     */
    String latestVersion(final String packageName) throws DubRepositoryException;

    static DubClientBuilder builder() {
        return new DubClientBuilder();
    }
}
