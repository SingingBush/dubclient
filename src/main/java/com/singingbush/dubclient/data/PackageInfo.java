package com.singingbush.dubclient.data;

import org.jetbrains.annotations.Nullable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class PackageInfo {

    private final Repository repository;
    private final String dateAdded;
    private final String name;
    private final List<String> categories;
    private final String owner;
    private final String id;
    private final String documentationURL;
    private final Set<Version> versions;

    public PackageInfo(Repository repository, String dateAdded, String name, List<String> categories, String owner, String id, String documentationURL, Set<Version> versions) {
        this.repository = repository;
        this.dateAdded = dateAdded;
        this.name = name;
        this.categories = categories;
        this.owner = owner;
        this.id = id;
        this.documentationURL = documentationURL;
        this.versions = versions;
    }

    public Repository getRepository() {
        return repository;
    }

    @Nullable
    public ZonedDateTime getDateAdded() {
        if(dateAdded != null && !dateAdded.isEmpty()) {
            // datetimes are generally ISO-8601 but may be missing timezone, default to UTC
            return ZonedDateTime.parse(dateAdded, DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("UTC")));
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public String getDocumentationURL() {
        return documentationURL;
    }

    public Set<Version> getVersions() {
        return versions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageInfo that = (PackageInfo) o;
        return Objects.equals(repository, that.repository) &&
            Objects.equals(dateAdded, that.dateAdded) &&
            Objects.equals(name, that.name) &&
            Objects.equals(categories, that.categories) &&
            Objects.equals(owner, that.owner) &&
            Objects.equals(id, that.id) &&
            Objects.equals(documentationURL, that.documentationURL) &&
            Objects.equals(versions, that.versions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repository, dateAdded, name, categories, owner, id, documentationURL, versions);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PackageInfo{");
        sb.append("repository=").append(repository);
        sb.append(", dateAdded='").append(dateAdded).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", categories=").append(categories);
        sb.append(", owner='").append(owner).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", documentationURL='").append(documentationURL).append('\'');
        sb.append(", versions=").append(versions);
        sb.append('}');
        return sb.toString();
    }
}
