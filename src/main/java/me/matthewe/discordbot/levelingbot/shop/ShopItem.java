package me.matthewe.discordbot.levelingbot.shop;

import lombok.Getter;
import lombok.Setter;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.entities.Member;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:21 PM for the project LevelingBot
 */
@Getter
@Setter
public abstract class ShopItem  {
    protected final ShopItems item;
    protected long userId;
    protected long purchaseDate;


    public ShopItem(ShopItems item, long userId, long purchaseDate) {
        this.item = item;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
    }

    public ShopItem(ShopItems item) {
        this.item = item;
    }

    public void purchase(LevelingBot bot, LevelUser levelUser, Member member) {
        this.userId = member.getUser().getIdLong();
        this.purchaseDate = System.currentTimeMillis();
        this.onPurchase(bot,levelUser,member);
    }

    protected abstract void onPurchase(LevelingBot bot, LevelUser levelUser, Member member);

    protected  abstract void removePurchase(LevelingBot bot, LevelUser levelUser, Member member);
}
