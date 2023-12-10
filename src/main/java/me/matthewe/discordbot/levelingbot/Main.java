package me.matthewe.discordbot.levelingbot;

import com.google.gson.GsonBuilder;
import me.matthewe.discordbot.levelingbot.shop.*;
import me.matthewe.discordbot.utilities.JsonUtils;

/**
 * Created by Matthew Eisenberg on 7/27/2018 at 6:13 PM for the project LevelingBot
 */
public class Main {
    private static RuntimeTypeAdapterFactory<ShopItem> shopItemRuntimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(ShopItem.class, "type")
            .registerSubtype(PrivateRoomShopItem.class, "PrivateRoomShopItem")
            .registerSubtype(RankUpgradeShopItem.class, "RankUpgradeShopItem")
            .registerSubtype(PermUnmuteShopItem.class, "PermUnmuteShopItem")
            .registerSubtype(TempUnmuteShopItem.class, "TempUnmuteShopItem")
            .registerSubtype(ChatBoosterShopItem.class, "ChatBoosterShopItem")
            .registerSubtype(PermUnmuteAndUnbanFriendShopItem.class, "PermUnmuteAndUnbanFriendShopItem")

            .registerSubtype(RankLoungeAccessShopItem.class,"RankLoungeAccessShopItem");

    public static void main(String[] args) {
        JsonUtils.setGSON(new GsonBuilder().setPrettyPrinting().registerTypeAdapterFactory(shopItemRuntimeTypeAdapterFactory));
        LevelingBot levelingBot = new LevelingBot();
        levelingBot.start();
    }
}
