package com.singingbush.dubclient.data;

import java.util.Objects;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class PackageStats {

    private final DownloadStats downloads;
    private final RepoStats repo;
    private final String updatedAt;
    private final Double score;

    public PackageStats(DownloadStats downloads, RepoStats repo, String updatedAt, Double score) {
        this.downloads = downloads;
        this.repo = repo;
        this.updatedAt = updatedAt;
        this.score = score;
    }

    public DownloadStats getDownloads() {
        return downloads;
    }

    public RepoStats getRepo() {
        return repo;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PackageStats stats = (PackageStats) o;
        return Objects.equals(downloads, stats.downloads) &&
            Objects.equals(repo, stats.repo) &&
            Objects.equals(updatedAt, stats.updatedAt) &&
            Objects.equals(score, stats.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(downloads, repo, updatedAt, score);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PackageStats{");
        sb.append("downloads=").append(downloads);
        sb.append(", repo=").append(repo);
        sb.append(", updatedAt='").append(updatedAt).append('\'');
        sb.append(", score=").append(score);
        sb.append('}');
        return sb.toString();
    }
}
