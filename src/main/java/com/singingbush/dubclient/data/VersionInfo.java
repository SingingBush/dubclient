package com.singingbush.dubclient.data;

import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * @author Samael Bate (singingbush)
 * created on 21/06/18
 */
public class VersionInfo {

    private final String readme;
    private final String commitID;
    private final String version;
    private final String docFolder; // may be empty String
    private final Boolean readmeMarkdown;
    private final String date; // ISO-8601

    public VersionInfo(String readme, String commitID, String version, String docFolder, Boolean readmeMarkdown, String date) {
        this.readme = readme;
        this.commitID = commitID;
        this.version = version;
        this.docFolder = docFolder;
        this.readmeMarkdown = readmeMarkdown;
        this.date = date;
    }

    public String getReadme() {
        return readme;
    }

    public String getCommitID() {
        return commitID;
    }

    public String getVersion() {
        return version;
    }

    public String getDocFolder() {
        return docFolder;
    }

    public Boolean getReadmeMarkdown() {
        return readmeMarkdown;
    }

    @Nullable
    public ZonedDateTime getDate() {
        return Utils.parseDatetime(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionInfo that = (VersionInfo) o;
        return Objects.equals(readme, that.readme) &&
            Objects.equals(commitID, that.commitID) &&
            Objects.equals(version, that.version) &&
            Objects.equals(docFolder, that.docFolder) &&
            Objects.equals(readmeMarkdown, that.readmeMarkdown) &&
            Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readme, commitID, version, docFolder, readmeMarkdown, date);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VersionInfo{");
        sb.append("readme='").append(readme).append('\'');
        sb.append(", commitID='").append(commitID).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", docFolder='").append(docFolder).append('\'');
        sb.append(", readmeMarkdown=").append(readmeMarkdown);
        sb.append(", date='").append(date).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
