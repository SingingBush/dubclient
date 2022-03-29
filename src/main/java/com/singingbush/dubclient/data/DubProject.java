package com.singingbush.dubclient.data;

import java.util.*;

public class DubProject extends DubPackage {

    private String description;
    private Map<String,String> toolchainRequirements; // more recent versions of dub
    private String homepage;
    private List<String> authors;
    private String copyright;
    private String license;
    private List<String> versions;
    // buildType (not same as target type)
    // targetName
    // targetPath
    private String workingDirectory;
    // subConfiguration
    private List<String> buildRequirements;
//    private List<String> buildOptions;
//    private List<String> libs;
//    private List<String> sourceFiles;
    private Map<String,Object> dependencies = new TreeMap<>(); // sometimes it's "name":"123.0" whilst others it's "name":{"version":"123.0","optional":true}
    private List<DubPackage> subPackages = new ArrayList<>();
    private List<DubProject> configurations = new ArrayList<>();

    public DubProject() {
        super();
    }

    public DubProject(String name) {
        super(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String,String> getToolchainRequirements() {
        return toolchainRequirements;
    }

    public void setToolchainRequirements(Map<String,String> toolchainRequirements) {
        this.toolchainRequirements = toolchainRequirements;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    public List<String> getBuildRequirements() {
        return buildRequirements;
    }

    public void setBuildRequirements(List<String> buildRequirements) {
        this.buildRequirements = buildRequirements;
    }

    public Map<String,Object> getDependencies() {
        return dependencies;
    }

    /**
     * Sometimes dependencies are configured as optional.
     * @return all dependencies regardless of them being optional or not
     */
    public Map<String,String> getAllDependencies() {
        if(dependencies != null) {
            final Map<String,String> deps = new TreeMap<>();
            for (final String name : deps.keySet()) {
                final Object val = dependencies.get(name);

                final String version = (String) (String.class.isAssignableFrom(val.getClass()) ? val :
                    ((Map<String,Object>)val).get("version"));

                deps.put(name, version);
            }
            return deps;
        }
        return null;
    }

    public void addDependency(String name, String version) {
        this.dependencies.put(name, version);
    }

    public void addDependency(String name, String version, boolean optional) {
        final Map<String,Object> value = new TreeMap<>();
        value.put("version", version);
        value.put("optional", optional);

        this.dependencies.put(name, value);
    }

    public List<DubPackage> getSubPackages() {
        return subPackages;
    }

    public void addSubPackage(final DubPackage subPackage) {
        this.subPackages.add(subPackage);
    }

    public List<DubProject> getConfigurations() {
        return configurations;
    }

    public void addConfigurations(DubProject configuration) {
        this.configurations.add(configuration);
    }
}
