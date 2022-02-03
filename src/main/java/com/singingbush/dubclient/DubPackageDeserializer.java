package com.singingbush.dubclient;

import com.google.gson.*;
import com.singingbush.dubclient.data.DubPackage;

import java.lang.reflect.Type;

public class DubPackageDeserializer implements JsonDeserializer<DubPackage> {

    @Override
    public DubPackage deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(json.isJsonNull()) {
            return null;
        }

        DubPackage dubPackage;

        if(json.isJsonObject()) {
            final JsonObject obj = json.getAsJsonObject();
            dubPackage = new DubPackage(obj.get("name").getAsString());
            dubPackage.setTargetType(obj.get("targetType").getAsString());
        } else {
            dubPackage = new DubPackage(json.getAsString());
        }
        return dubPackage;
    }
}
