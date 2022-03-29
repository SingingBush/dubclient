package com.singingbush.dubclient;

import com.singingbush.dubclient.data.DubPackage;
import com.singingbush.dubclient.data.DubProject;
import com.singingbush.sdl.Parser;
import com.singingbush.sdl.SDLParseException;
import com.singingbush.sdl.Tag;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.List;

class SdlDubFileParser implements DubFileParser {

    @Override
    public DubProject parse(@NotNull final File dubFile) throws FileNotFoundException {
        return parse(new FileReader(dubFile));
    }

    @Override
    public DubProject parse(@NotNull Reader reader) {
        try {
            final List<Tag> tags = new Parser(reader).parse();
            return convertToDubProject(tags);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SDLParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DubProject convertToDubProject(List<Tag> tags) {
        final DubProject project = new DubProject();
        for (final Tag t : tags) {
            switch (t.getName()) {
                case "name":
                    project.setName((String) t.getValue());
                    break;
                case "targetType":
                    project.setTargetType((String) t.getValue());
                    break;
                case "importPaths":
                    project.setImportPaths(Arrays.asList((String)t.getValue()));
                    break;
                case "sourcePaths":
                    project.setSourcePaths(Arrays.asList((String)t.getValue()));
                    break;
                case "description":
                    project.setDescription((String) t.getValue());
                    break;
                case "homepage":
                    project.setHomepage((String) t.getValue());
                    break;
                case "authors":
                    project.setAuthors(Arrays.asList((String)t.getValue()));
                    break;
                case "copyright":
                    project.setCopyright((String) t.getValue());
                    break;
                case "license":
                    project.setLicense((String) t.getValue());
                    break;
                case "buildRequirements":
                    project.setBuildRequirements(Arrays.asList((String)t.getValue()));
                    break;
                case "toolchainRequirements":
                    // todo: project.setToolchainRequirements();
                    break;
                case "dependency":
                    project.addDependency((String) t.getValue(), (String) t.getAttribute("version"));
                    break;
                case "subPackage":
                    final DubPackage subPackage = new DubPackage();
                    subPackage.setName((String) t.getValue());
                    subPackage.setTargetType((String) t.getAttribute("targetType"));
                    subPackage.setSourcePaths(Arrays.asList((String)t.getAttribute("sourcePaths")));
                    subPackage.setImportPaths(Arrays.asList((String)t.getAttribute("importPaths")));
                    project.addSubPackage(subPackage);
                    break;
            }
        }
        return project;
    }
}
