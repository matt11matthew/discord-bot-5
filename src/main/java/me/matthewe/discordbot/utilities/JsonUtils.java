package me.matthewe.discordbot.utilities;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.File;

/**
 * Created by Matthew E on 5/1/2018.
 */
public class JsonUtils {
    public static  GsonBuilder GSON = new GsonBuilder().setPrettyPrinting();

    public static void setGSON(GsonBuilder GSON) {
        JsonUtils.GSON = GSON;
    }

    public static <Value> Value loadJsonObjectFromFile(File file, Class<Value> valueClass, Value defaultValue) {
        if (!file.exists()) {
            FileUtils.createFile(file);
            saveJsonObjectToFile(file, defaultValue);
            return defaultValue;
        }
        return GSON.create().fromJson(FileUtils.readFileToString(file), valueClass);
    }

    public static String convertJsonStringToPrettyString(String json) {
        JsonParser jsonParser = new JsonParser();
        return GSON.create().toJson(jsonParser.parse(json));
    }

    public static <Value> void saveJsonObjectToFile(File file, Value value) {
        FileUtils.writeStringToFile(file, GSON.create().toJson(value, value.getClass()));
    }
}
