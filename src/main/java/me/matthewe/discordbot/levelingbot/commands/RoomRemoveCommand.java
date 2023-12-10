package me.matthewe.discordbot.levelingbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.shop.PrivateRoomShopItem;
import me.matthewe.discordbot.levelingbot.shop.ShopItem;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.awt.*;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:14 PM for the project LevelingBot
 */
@CommandInfo(name = "remove", description = "Remove from private room", usage = ">remove (user)")
public class RoomRemoveCommand extends Command {

    private LevelingBot levelingBot;

    public RoomRemoveCommand(LevelingBot levelingBot) {
        this.levelingBot = levelingBot;
        this.name = "remove";
        this.cooldown = 5;
        this.arguments = "<user>";
        this.help = "Removes user from your private chat room";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        LevelUser user = levelingBot.getUser(event.getAuthor().getIdLong());
        LevelUser.ShopData shopData = user.getShopData();

        for (ShopItem purchase : shopData.getPurchases()) {
            if (purchase instanceof PrivateRoomShopItem) {
                PrivateRoomShopItem privateRoomShopItem = (PrivateRoomShopItem) purchase;
                if (privateRoomShopItem.getTextChannel() == event.getMessage().getChannel().getIdLong()) {

                    for (Member mentionedMember : event.getMessage().getMentionedMembers()) {
                        if (privateRoomShopItem.isMember(mentionedMember.getUser().getIdLong())) {
                            privateRoomShopItem.removeMember(mentionedMember.getUser().getIdLong());
                            event.getGuild().getCategoryById(privateRoomShopItem.getCategory()).getPermissionOverride(mentionedMember).delete().queue(aVoid-> {
                                EmbedBuilder embedBuilder = new EmbedBuilder()
                                        .setTitle("Removed User")
                                        .setColor(Color.YELLOW.darker())
                                        .setDescription("You have removed " + mentionedMember.getAsMention());
                                event.reply(embedBuilder.build());

                            });

                        }
                    }
                    break;
                }
            }
        }
    }
}
