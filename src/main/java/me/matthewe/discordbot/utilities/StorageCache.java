package me.matthewe.discordbot.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static me.matthewe.discordbot.utilities.JsonUtils.convertJsonStringToPrettyString;

/**
 * Created by Matthew Eisenberg on 6/18/2018 at 1:41 PM for the project discordbot
 */
public abstract class StorageCache<K, O> extends ListenerAdapter {
    protected Map<K, O> map = new ConcurrentHashMap<>();
    private String key;
    private String fileName;

    public StorageCache(String key, String fileName) {
        this.key = key;
        this.fileName = fileName;
    }

    public boolean exists(K k) {
        return map.containsKey(k);
    }

    public void load() {
        this.map = new ConcurrentHashMap<>();
        File file = new File(fileName);
        if (!file.exists()) {
            FileUtils.createFile(file);
            return;
        }
        String readFileToString = FileUtils.readFileToString(file);
        if (readFileToString == null || readFileToString.isEmpty()) {
            return;
        }
        JsonObject asJsonObject = new JsonParser().parse(readFileToString).getAsJsonObject();

        for (JsonElement jsonElement : asJsonObject.get(key).getAsJsonArray()) {
            JsonObject asJsonObject1 = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> stringJsonElementEntry : asJsonObject1.entrySet()) {
                K k = parseFromString(stringJsonElementEntry.getKey());
                O user = JsonUtils.GSON.create().fromJson(stringJsonElementEntry.getValue(), getEntityClass());
                map.put(k, user);
            }
        }
        System.out.println("Loaded " + map.keySet().size() + " " + key);
    }

    public O get(K k) {
        return map.get(k);
    }

    public O create(K k, O defaultO) {
        if (map.containsKey(k)) {
            return map.get(k);
        }
        map.put(k, defaultO);
        return map.get(k);
    }

    public void save() {
        File file = new File(fileName);
        if (!file.exists()) {
            FileUtils.createFile(file);
        }
        JsonObject jsonObject = new JsonObject();
        JsonArray users = new JsonArray();
        int count = 0;
        for (Map.Entry<K, O> userEntry : this.map.entrySet()) {
            JsonObject userJsonObject = new JsonObject();
            userJsonObject.add(userEntry.getKey().toString(), new JsonParser().parse(JsonUtils.GSON.create().toJson(userEntry.getValue(), userEntry.getValue().getClass())));
            users.add(userJsonObject);
            count++;
        }
        jsonObject.add(key, users);
        FileUtils.writeStringToFile(file, convertJsonStringToPrettyString(jsonObject.toString()));
        System.out.println("Saved " + count + " " + key);
    }

    public abstract K parseFromString(String key);

    public abstract Class<O> getEntityClass();
}
