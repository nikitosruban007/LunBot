package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModalInteraction extends ListenerAdapter {
    LunBot plugin = LunBot.getInstance();

    static Map<String, String> map = new HashMap<>();

    static EmbedBuilder embed = new EmbedBuilder();

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("modal-cw")) {
            String nickValue = Objects.requireNonNull(event.getValue("nick")).getAsString();
            String age = Objects.requireNonNull(event.getValue("age")).getAsString();
            String findValue = Objects.requireNonNull(event.getValue("find")).getAsString();
            String timeValue = Objects.requireNonNull(event.getValue("time")).getAsString();
            String plansValue = Objects.requireNonNull(event.getValue("plans")).getAsString();

            map.put(event.getMember().getId(), nickValue);


                    embed
                    .setFooter(event.getUser().getId(), event.getUser().getAvatarUrl())
                    .setTitle("Заявка на сервер игрока " + event.getUser().getName())
                    .addField("Ник: ", nickValue, false)
                    .addField("Возраст: ", age, false)
                    .addField("Откуда узнал о нас: ", findValue, false)
                    .addField("Сколько времени готов уделять на проект: ", timeValue, false)
                    .addField("О игроке и его планах на сервер: ", plansValue, false)
                    .setColor(new Color(189, 189, 189));

            plugin.getRv().sendMessageEmbeds(embed.build())
                    .setActionRow(Button.success("accept", "Принять"), Button.danger("decline", "Отклонить"))
                    .queue();

            event.reply("Заявка успешно отправлена на рассмотрение!").setEphemeral(true).queue();
        } else if (event.getModalId().equals("modal-reason")) {
            String reason = Objects.requireNonNull(event.getValue("reason")).getAsString();
            Member member = event.getMember();

            EmbedBuilder embed1 = new EmbedBuilder()
                    .setTitle("**Приветствую тебя, дорогой " + member.getEffectiveName() + "**")
                    .setDescription("*К сожалению, ваша заявка была отклонена.*\n\n**Причина:** *" + reason + "*\nПожалуйста, попробуйте подать заявку снова позже!")
                    .setColor(new Color(198, 1, 1))
                    .setFooter("Проверил: " + event.getUser().getName(), event.getUser().getAvatarUrl());

            Objects.requireNonNull(member).getUser().openPrivateChannel().queue(channel -> {
                channel.sendMessageEmbeds(embed1.build()).queue();
            });

            embed
                    .setColor(new Color(198, 1, 1))
                    .setFooter("Проверил: " + event.getUser().getName(), event.getUser().getAvatarUrl());

            event.getMessage().editMessageEmbeds(embed.build())
                    .setComponents()
                    .queue();

            event.reply("Вы успешно отказали игроку в заявке.").setEphemeral(true).queue();
        }

    }
}
