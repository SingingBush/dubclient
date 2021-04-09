package com.singingbush.dubclient.data;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
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
    private String copyright;
    private String readme;
    private String commitID;
    private String targetType;
    private String date;
    private String homepage;
    private String packageDescriptionFile;
    private String[] authors;

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

    @Nullable
    public String getCopyright() {
        return copyright;
    }

    public String getReadme() {
        return readme;
    }

    public String getCommitID() {
        return commitID;
    }

    @Nullable
    public String getTargetType() {
        return targetType;
    }

    public String getDate() {
        return date;
    }

    @Nullable
    public String getHomepage() {
        return homepage;
    }

    public String getPackageDescriptionFile() {
        return packageDescriptionFile;
    }

    public String[] getAuthors() {
        return authors;
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
            Objects.equals(copyright, version1.copyright) &&
            Objects.equals(readme, version1.readme) &&
            Objects.equals(commitID, version1.commitID) &&
            Objects.equals(targetType, version1.targetType) &&
            Objects.equals(date, version1.date) &&
            Objects.equals(homepage, version1.homepage) &&
            Objects.equals(packageDescriptionFile, version1.packageDescriptionFile) &&
            Arrays.equals(authors, version1.authors);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, version, description, license, copyright, readme, commitID, targetType, date, homepage, packageDescriptionFile);
        result = 31 * result + Arrays.hashCode(authors);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Version{");
        sb.append("name='").append(name).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", license='").append(license).append('\'');
        sb.append(", copyright='").append(copyright).append('\'');
        sb.append(", readme='").append(readme).append('\'');
        sb.append(", commitID='").append(commitID).append('\'');
        sb.append(", targetType='").append(targetType).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", homepage='").append(homepage).append('\'');
        sb.append(", packageDescriptionFile='").append(packageDescriptionFile).append('\'');
        sb.append(", authors=").append(Arrays.toString(authors));
        sb.append('}');
        return sb.toString();
    }
}
