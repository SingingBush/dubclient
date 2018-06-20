package com.singingbush.dubclient.data;

import java.util.Objects;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class Repository {

    private final String project;
    private final String owner;
    private final String kind;

    public Repository(String project, String owner, String kind) {
        this.project = project;
        this.owner = owner;
        this.kind = kind;
    }

    public String getProject() {
        return project;
    }

    public String getOwner() {
        return owner;
    }

    public String getKind() {
        return kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Repository that = (Repository) o;
        return Objects.equals(project, that.project) &&
            Objects.equals(owner, that.owner) &&
            Objects.equals(kind, that.kind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, owner, kind);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Repository{");
        sb.append("project='").append(project).append('\'');
        sb.append(", owner='").append(owner).append('\'');
        sb.append(", kind='").append(kind).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
