package com.singingbush.dubclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.singingbush.dubclient.data.DubPackage;
import com.singingbush.dubclient.data.DubProject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

class JsonDubFileParser implements DubFileParser {

    private final Gson gson;

    public JsonDubFileParser() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(DubPackage.class, new DubPackageDeserializer())
                .create();
    }

    @Override
    public DubProject parse(@NotNull final File dubFile) throws FileNotFoundException {
        return parse(new FileReader(dubFile));
    }

    @Override
    public DubProject parse(@NotNull final Reader reader) {
        return gson.fromJson(reader, DubProject.class);
    }
}
