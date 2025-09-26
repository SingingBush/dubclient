package com.singingbush.dubclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.singingbush.dubclient.data.DubPackage;
import com.singingbush.dubclient.data.DubProject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

class JsonDubFileParser implements DubFileParser {

    private final Gson gson;

    public JsonDubFileParser() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(DubPackage.class, new DubPackageDeserializer())
                .create();
    }

    @Override
    public DubProject parse(@NotNull final File dubFile) throws IOException {
        return parse(new FileReader(dubFile, StandardCharsets.UTF_8));
    }

    @Override
    public DubProject parse(@NotNull final Reader reader) {
        return gson.fromJson(reader, DubProject.class);
    }
}
