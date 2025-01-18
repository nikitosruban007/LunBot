package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class LunBot {

    private static LunBot instance;
    public JDA jda;
    public static Guild guild, gosguild;
    public TextChannel app, rv;
    public String roleMain, roleGos, ruleChannel, infoChannel;

    public static void main(String[] args) throws IOException, InterruptedException {
        instance = new LunBot();
        Config.loadConfig();

        Init.initializeBot(Config.botToken, instance);
        instance.initialize(instance.jda);
        instance.createApp();
    }

    public void initialize(JDA jda) {
        this.jda = jda;


        gosguild = jda.getGuildById(Config.gosguildId);
        guild = jda.getGuildById(Config.guildId);
        app = jda.getTextChannelById(Config.appId);
        rv = jda.getTextChannelById(Config.rvId);
        ruleChannel = Config.ruleChannel;
        infoChannel = Config.infoChannel;
        roleMain = Config.roleMain;
        roleGos = Config.roleGos;
    }

    public TextChannel getRv() {
        return rv;
    }

    public static LunBot getInstance() {
        return instance;
    }

    public Guild getGosguild() {
        return gosguild;
    }

    public static Guild getGuild() {
        return guild;
    }

    public String getRoleGos() {
        return roleGos;
    }

    public String getRuleChannel() {
        return ruleChannel;
    }

    public String getInfoChannel() {
        return infoChannel;
    }

    public String getRoleMain() {
        return roleMain;
    }

    public void createApp() {
        Button submit = Button.success("add-App", "✒️Создать заявку");

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Заявки на проходку", null, "https://media.discordapp.net/attachments/1328795825161375836/1328795971332739104/4379-axelot.webp?ex=6788012f&is=6786afaf&hm=839da7f665ef3fd0af5227fbbc24266587fc5a2d6095716739c250c19c541cf6&=&format=webp&width=70&height=70")
                .setTitle("Добро пожаловать!")
                .setThumbnail("https://cdn.discordapp.com/attachments/1328795825161375836/1328796060335738934/3237-axolotl.png?ex=67880144&is=6786afc4&hm=5dbcf388d9fd4912dfa13973685a16489d4cb330dbe0c14aed2da7f9f4fc78c8&")
                .setDescription("***Критерии оценивания заявок:***\n" +
                        "-Ваша адекватность\n" +
                        "-Ваше ознакомление с правилами\n" +
                        "-Ваше стремление к игре на сервере\n" +
                        "-Опыт в Ролевых играх\n" +
                        "-Придерживание схеме\n" +
                        "Вы обязаны быть в нашем Discord сервере [LunLand ГОC](https://discord.gg/Wmd96Sv5fB), для того чтобы вы смогли подать заявку!\n" +
                        "\n" +
                        "***Вам будет будет открыто окно для подачи заявки, ознакомьтесь с его схемой:***\n" +
                        "`1.` Ник в игре.\n" +
                        "`2.` Возраст\n" +
                        "`3.` Откуда узнали о нас?\n" +
                        "`4.` Сколько времени готовы уделять на проект?\n" +
                        "`5.` Расскажите о себе и своих планах на сервер:\n")
                .setImage("https://i.pinimg.com/originals/c7/21/35/c721355f7615fe0038741e3803b46390.gif")
                .setFooter("Время рассмотрения в течение суток \uD83E\uDDFE", "https://cdn.discordapp.com/attachments/1328795825161375836/1328795971043328154/hNPmaXoG85ajolotepixel.png?ex=6788012f&is=6786afaf&hm=782856bbeee54d824c7115f8b34b1d6dcf4e86c56c8292fabebedb0079805dd5&")
                .setColor(new Color(148, 128, 182));

        if (app != null) {
            app.getHistory().retrievePast(1).queue(messages -> {
                if (!messages.isEmpty() && !messages.getFirst().getEmbeds().isEmpty()) {
                    MessageEmbed existingEmbed = messages.getFirst().getEmbeds().getFirst();
                    MessageEmbed newEmbed = embed.build();

                    if (newEmbed.getTitle().equals(existingEmbed.getTitle())) {
                        return;
                    }
                }

                app.sendMessageEmbeds(embed.build())
                        .setActionRow(submit)
                        .queue();
            });
        }
    }
}
