package com.singingbush.dubclient.data;

import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class PackageInfo {

    private Repository repository;
    private String dateAdded;
    private String name;
    private List<String> categories;
    private String owner;
    private String id;
    private String documentationURL;
    private Set<Version> versions;

    public Repository getRepository() {
        return repository;
    }

    @Nullable
    public ZonedDateTime getDateAdded() {
        return Utils.parseDatetime(dateAdded);
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
