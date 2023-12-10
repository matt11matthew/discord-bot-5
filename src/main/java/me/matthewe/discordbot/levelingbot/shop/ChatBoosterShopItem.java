package me.matthewe.discordbot.levelingbot.shop;

import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.booster.Booster;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.entities.Member;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 7:08 PM for the project LevelingBot
 */
public class ChatBoosterShopItem extends ShopItem {
    public ChatBoosterShopItem(ShopItems item, long userId, long purchaseDate) {
        super(item, userId, purchaseDate);
    }

    public ChatBoosterShopItem(ShopItems item) {
        super(item);
    }

    @Override
    protected void onPurchase(LevelingBot bot, LevelUser levelUser, Member member) {
        bot.getBoosterStorage().addBooster(new Booster(UUID.randomUUID(),member.getUser().getIdLong(), TimeUnit.HOURS.toSeconds(6),1.80D,0));

    }

    @Override
    protected void removePurchase(LevelingBot bot, LevelUser levelUser, Member member) {

    }
}
