package com.singingbush.dubclient.data;

import java.util.Objects;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class RepoStats {

    private Integer stars;
    private Integer watchers;
    private Integer forks;
    private Integer issues;

    public RepoStats(Integer stars, Integer watchers, Integer forks, Integer issues) {
        this.stars = stars;
        this.watchers = watchers;
        this.forks = forks;
        this.issues = issues;
    }

    public Integer getStars() {
        return stars;
    }

    public Integer getWatchers() {
        return watchers;
    }

    public Integer getForks() {
        return forks;
    }

    public Integer getIssues() {
        return issues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepoStats repoStats = (RepoStats) o;
        return Objects.equals(stars, repoStats.stars) &&
            Objects.equals(watchers, repoStats.watchers) &&
            Objects.equals(forks, repoStats.forks) &&
            Objects.equals(issues, repoStats.issues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stars, watchers, forks, issues);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RepoStats{");
        sb.append("stars=").append(stars);
        sb.append(", watchers=").append(watchers);
        sb.append(", forks=").append(forks);
        sb.append(", issues=").append(issues);
        sb.append('}');
        return sb.toString();
    }
}
