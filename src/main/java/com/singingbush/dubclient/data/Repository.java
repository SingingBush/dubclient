package com.singingbush.dubclient.data;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Samael Bate (singingbush)
 * created on 16/06/18
 */
public class Repository {

    private String project;
    private String owner;
    private String kind;

    @NotNull
    public String getProject() {
        return project;
    }

    @NotNull
    public String getOwner() {
        return owner;
    }

    @NotNull
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
