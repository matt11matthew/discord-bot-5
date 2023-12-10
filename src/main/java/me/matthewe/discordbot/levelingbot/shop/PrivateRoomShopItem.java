package me.matthewe.discordbot.levelingbot.shop;

import lombok.Getter;
import lombok.Setter;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.managers.GuildController;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:22 PM for the project LevelingBot
 */
@Getter
@Setter
public class PrivateRoomShopItem extends ShopItem {
    private long category;
    private long textChannel;
    private long voiceChannel;
    private List<Long> members = new ArrayList<>();

    public PrivateRoomShopItem(long userId, long purchaseDate, long category, long textChannel, long voiceChannel, List<Long> members) {
        super(ShopItems.PRIVATE_ROOM, userId, purchaseDate);
        this.members = members;
        this.category = category;
        this.textChannel = textChannel;
        this.voiceChannel = voiceChannel;
    }

    public void clearMembers() {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.clear();

    }

    public void addMember(long id) {
        if (members == null) {
            members = new ArrayList<>();
        }
        this.members.add(id);
    }

    public boolean isMember(long id) {
        if (members == null) {
            members = new ArrayList<>();
        }
        return members.contains(id);
    }

    public void removeMember(long id) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.remove(id);
    }

    public PrivateRoomShopItem(ShopItems item) {
        super(item);
    }

    @Override
    protected void onPurchase(LevelingBot bot, LevelUser levelUser, Member member) {
        this.userId = member.getUser().getIdLong();

        levelUser.getShopData().addPurchase(this);
        Guild guild = bot.getGuild(bot.getConfig().getGuildId());

        GuildController controller = guild.getController();
        controller.createCategory(member.getUser().getName() + "-" + new Random().nextInt(34343))
                .addPermissionOverride(guild.getPublicRole(), new ArrayList<>(), Arrays.asList(Permission.MESSAGE_READ))
                .addPermissionOverride(member, Arrays.asList(Permission.MESSAGE_READ), new ArrayList<>())
                .queue(channel -> {
                    Category category = (Category) channel;
                    this.category = channel.getIdLong();
                    category.createTextChannel("text channel").queue(channel1 -> {
                        this.textChannel = channel1.getIdLong();
                        TextChannel textChannel = (TextChannel) channel1;
                        EmbedBuilder embedBuilder = new EmbedBuilder()
                                .setTitle("Thank you!")
                                .setColor(Color.YELLOW.darker())
                                .setDescription("Thank you for purchasing a private room");
                        textChannel.sendMessage(member.getAsMention()).queue(message -> {
                            textChannel.sendMessage(embedBuilder.build()).queue();

                            category.createVoiceChannel("voice channel").queue(channel2 -> {
                                this.voiceChannel = channel2.getIdLong();
                            });
                        });
                    });
                });

    }

    @Override
    protected void removePurchase(LevelingBot bot, LevelUser levelUser, Member member) {
        Guild guildById = member.getJDA().getGuildById(bot.getConfig().getGuildId());
        guildById.getTextChannelById(textChannel).delete().queue();
        guildById.getVoiceChannelById(voiceChannel).delete().queue();
        guildById.getCategoryById(category).delete().queue();
        levelUser.getShopData().removePurchase(this);

    }
}
