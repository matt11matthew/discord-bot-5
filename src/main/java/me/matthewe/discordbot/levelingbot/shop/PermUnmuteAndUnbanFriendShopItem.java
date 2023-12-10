package me.matthewe.discordbot.levelingbot.shop;

import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.awt.*;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 4:05 PM for the project LevelingBot
 */
public class PermUnmuteAndUnbanFriendShopItem extends ShopItem {
    public PermUnmuteAndUnbanFriendShopItem(ShopItems item, long userId, long purchaseDate) {
        super(item, userId, purchaseDate);
    }
    public PermUnmuteAndUnbanFriendShopItem(ShopItems item) {
        super(item);
    }
    @Override
    protected void onPurchase(LevelingBot bot, LevelUser levelUser, Member member) {
        levelUser.getShopData().addPurchase(this);

        Role role = member.getGuild().getRolesByName(bot.getConfig().getStaffRole(), true).get(0);
        for (Member membersWithRole : bot.getGuild(bot.getConfig().getGuildId()).getMembersWithRoles(role)) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("Notification")
                    .setColor(Color.YELLOW.darker())
                    .setDescription(membersWithRole.getAsMention()+" has purchased **"+item.getName()+"**");
            bot.sendPrivateEmbedMessage(membersWithRole.getUser().getIdLong(), embedBuilder.build());
        }
    }

    @Override
    protected void removePurchase(LevelingBot bot, LevelUser levelUser, Member member) {
        levelUser.getShopData().removePurchase(this);
    }
}
