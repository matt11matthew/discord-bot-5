package me.matthewe.discordbot.levelingbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:14 PM for the project LevelingBot
 */
@CommandInfo(name = "givecoins", description = "givecoins balance", usage = ">givecoins (user) (amount)")
public class GiveCoinsCommand extends Command {

    private LevelingBot levelingBot;

    public GiveCoinsCommand(LevelingBot levelingBot) {
        this.levelingBot = levelingBot;
        this.name = "givecoins";
        this.cooldown = 5;
        this.help = "Give coins";
        this.arguments = "(user) (amount)";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            return;
        } else if (event.getArgs().split(" ").length >= 2) {
            long amount = 0;
            try {
                amount = Long.parseLong(event.getArgs().split(" ")[1]);
            } catch (Exception e) {
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("Error")
                        .setColor(Color.RED.darker())
                        .setDescription("Please enter a valid amount.");
            event.reply(embedBuilder.build());
                return;
            }
            for (Member mentionedMember : event.getMessage().getMentionedMembers()) {
                if (mentionedMember.getUser().isBot()) {
                    continue;
                }

                LevelUser levelUser = levelingBot.getUser(mentionedMember.getUser().getIdLong());
                levelUser.setCoins(levelUser.getCoins()+amount
                );
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor(mentionedMember.getUser().getName(), mentionedMember.getUser().getAvatarUrl(), mentionedMember.getUser().getAvatarUrl())
                        .setColor(Color.YELLOW.darker())
                        .setDescription(mentionedMember.getAsMention() + " now as **" + new DecimalFormat("#,###").format(levelUser.getCoins()) + "** coins.");
                event.reply(embedBuilder.build());
            }
        }
    }
}

