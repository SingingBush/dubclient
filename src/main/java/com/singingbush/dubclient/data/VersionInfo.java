package com.singingbush.dubclient.data;

import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;
import java.util.Arrays;
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
    private final Info info;

    public VersionInfo(String readme, String commitID, String version, String docFolder, Boolean readmeMarkdown, String date, final Info info) {
        this.readme = readme;
        this.commitID = commitID;
        this.version = version;
        this.docFolder = docFolder;
        this.readmeMarkdown = readmeMarkdown;
        this.date = date;
        this.info = info;
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

    public Info getInfo() {
        return info;
    }

    public class Info {
        private final String packageDescriptionFile;
        private final String[] authors;
        private final String name;
        private final String targetType;
        private final String license;
        private final String description;
        private final String homepage;

        public Info(String packageDescriptionFile, String[] authors, String name, String targetType, String license, String description, String homepage) {
            this.packageDescriptionFile = packageDescriptionFile;
            this.authors = authors;
            this.name = name;
            this.targetType = targetType;
            this.license = license;
            this.description = description;
            this.homepage = homepage;
        }

        public String getPackageDescriptionFile() {
            return packageDescriptionFile;
        }

        public String[] getAuthors() {
            return authors;
        }

        public String getName() {
            return name;
        }

        public String getTargetType() {
            return targetType;
        }

        public String getLicense() {
            return license;
        }

        public String getDescription() {
            return description;
        }

        public String getHomepage() {
            return homepage;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Info info = (Info) o;
            return Objects.equals(packageDescriptionFile, info.packageDescriptionFile) &&
                Arrays.equals(authors, info.authors) &&
                Objects.equals(name, info.name) &&
                Objects.equals(targetType, info.targetType) &&
                Objects.equals(license, info.license) &&
                Objects.equals(description, info.description) &&
                Objects.equals(homepage, info.homepage);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(packageDescriptionFile, name, targetType, license, description, homepage);
            result = 31 * result + Arrays.hashCode(authors);
            return result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Info{");
            sb.append("packageDescriptionFile='").append(packageDescriptionFile).append('\'');
            sb.append(", authors=").append(Arrays.toString(authors));
            sb.append(", name='").append(name).append('\'');
            sb.append(", targetType='").append(targetType).append('\'');
            sb.append(", license='").append(license).append('\'');
            sb.append(", description='").append(description).append('\'');
            sb.append(", homepage='").append(homepage).append('\'');
            sb.append('}');
            return sb.toString();
        }
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
