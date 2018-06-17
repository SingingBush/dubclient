package com.singingbush.dubclient.data;

import java.util.Objects;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class Version {

    private String name;
    private String version;
    private String description;
    private String license;
    private String readme;
    private String commitID;
    private String targetType;
    private String date;
    private String homepage;

    public Version(String name, String version, String description, String license, String readme, String commitID, String targetType, String date, String homepage) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.license = license;
        this.readme = readme;
        this.commitID = commitID;
        this.targetType = targetType;
        this.date = date;
        this.homepage = homepage;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getLicense() {
        return license;
    }

    public String getReadme() {
        return readme;
    }

    public String getCommitID() {
        return commitID;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getDate() {
        return date;
    }

    public String getHomepage() {
        return homepage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version1 = (Version) o;
        return Objects.equals(name, version1.name) &&
            Objects.equals(version, version1.version) &&
            Objects.equals(description, version1.description) &&
            Objects.equals(license, version1.license) &&
            Objects.equals(readme, version1.readme) &&
            Objects.equals(commitID, version1.commitID) &&
            Objects.equals(targetType, version1.targetType) &&
            Objects.equals(date, version1.date) &&
            Objects.equals(homepage, version1.homepage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, description, license, readme, commitID, targetType, date, homepage);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Version{");
        sb.append("name='").append(name).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", license='").append(license).append('\'');
        sb.append(", readme='").append(readme).append('\'');
        sb.append(", commitID='").append(commitID).append('\'');
        sb.append(", targetType='").append(targetType).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", homepage='").append(homepage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
