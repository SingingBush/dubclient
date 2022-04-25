package com.singingbush.dubclient.data;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class DubPackage {

    private String name;
    private String targetType;
    private List<String> sourcePaths;
    private List<String> importPaths;

    public DubPackage() {}

    public DubPackage(String name) {
        this.name = name;
        this.targetType = "autodetect";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetType() {
        return targetType;
    }

    /**
     * The following values are recognized for the "targetType" setting:
     *
     * Value	Description
     * "autodetect"	Automatically detects the target type. This is the default global value and causes dub to try and generate "application" and "library" configurations. Use of other values limits the auto-generated configurations to either of the two. This value is not allowed inside of a configuration block.
     * "none"	Does not generate an output file. This is useful for packages that are supposed to drag in other packages using the "dependencies" section.
     * "executable"	Generates an executable binary
     * "library"	Specifies that the package is to be used as a library, without limiting the actual type of library. This should be the default for most libraries.
     * "sourceLibrary"	This target type does not generate a binary, but rather forces dub to add all source files directly to the same compiler invocation as the dependent project.
     * "staticLibrary"	Forces output as a static library container.
     * "dynamicLibrary"	Forces output as a dynamic/shared library.
     *
     * @param targetType Should be one of the options documented above. Typically "executable" or "library"
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public List<String> getSourcePaths() {
        return sourcePaths;
    }

    public void setSourcePaths(List<String> sourcePaths) {
        this.sourcePaths = sourcePaths;
    }

    public List<String> getImportPaths() {
        return importPaths;
    }

    public void setImportPaths(List<String> importPaths) {
        this.importPaths = importPaths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DubPackage that = (DubPackage) o;
        return Objects.equals(name, that.name) && Objects.equals(targetType, that.targetType) && Objects.equals(sourcePaths, that.sourcePaths) && Objects.equals(importPaths, that.importPaths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, targetType, sourcePaths, importPaths);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DubPackage.class.getSimpleName() + "[", "]")
            .add("name='" + name + "'")
            .add("targetType='" + targetType + "'")
            .add("sourcePaths=" + sourcePaths)
            .add("importPaths=" + importPaths)
            .toString();
    }
}
