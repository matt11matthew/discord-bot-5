package me.matthewe.discordbot.levelingbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.rank.Rank;
import me.matthewe.discordbot.levelingbot.shop.RankUpgradeShopItem;
import me.matthewe.discordbot.levelingbot.shop.ShopItem;
import me.matthewe.discordbot.levelingbot.shop.ShopItems;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:14 PM for the project LevelingBot
 */
@CommandInfo(name = "shop", description = "The shop command", usage = ">shop")
public class ShopCommand extends Command {

    private LevelingBot levelingBot;

    public ShopCommand(LevelingBot levelingBot) {
        this.levelingBot = levelingBot;
        this.name = "shop";
        this.cooldown = 5;
        this.help = "View the shop";
        this.guildOnly = true;
    }

    private final boolean inline = false;

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Gold Coin Shop")
                .setColor(Color.YELLOW.darker())
                .setDescription("You can purchase items from the shop using gold coins.\nType `>purchase <name>` to purchase a shop item.");

        LevelUser user = levelingBot.getUser(event.getAuthor().getIdLong());
        if (user == null) {
            return;
        }
        int index = 0;
        for (ShopItems value : ShopItems.values()) {
            if (value != ShopItems.RANK_UPGRADE) {
                embedBuilder = embedBuilder.addField(value.getName(), "**" + new DecimalFormat("#,###").format(value.getCost()) + "**", inline);


            }
        }
//        embedBuilder = embedBuilder.addBlankField(inline);
        String currentRank = null;
        for (ShopItem purchase : user.getShopData().getPurchases()) {
            if (purchase instanceof RankUpgradeShopItem) {
                currentRank = ((RankUpgradeShopItem) purchase).getCurrentRank();
            }
        }
        Rank nextRank = null;
        if (currentRank != null) {
            nextRank = levelingBot.getRankStorage().getNextRank(levelingBot.getRankStorage().getRank(currentRank));
        } else {
            nextRank = levelingBot.getRankStorage().getFirstRank();

        }
        if (nextRank != null) {
            if (currentRank == null) {
                embedBuilder = embedBuilder.addField("Rank " + nextRank.getName(), "**" + new DecimalFormat("#,###").format(nextRank.getUpgradeCost()) + "**", inline);
            } else {
                embedBuilder = embedBuilder.addField("" + currentRank + " to " + nextRank.getName() + " Upgrade", "**" + new DecimalFormat("#,###").format(nextRank.getUpgradeCost()) + "**", inline);
            }
        }

        event.reply(embedBuilder.build());
    }
}
