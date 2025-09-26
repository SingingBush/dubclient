package com.singingbush.dubclient;

import com.singingbush.dubclient.data.DubProject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

interface DubFileParser {

    DubProject parse(@NotNull File dubFile) throws IOException;

    DubProject parse(@NotNull Reader reader);
}
