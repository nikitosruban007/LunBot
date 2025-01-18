package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class ButtonInteraction extends ListenerAdapter {
    LunBot plugin = LunBot.getInstance();

    Member member;


    public void setMember(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public boolean isInGos(User user) {

        Guild guild = plugin.jda.getGuildById("968853159470592091");
        Member member = guild.getMemberById(user.getId());

        for (Member m : guild.getMembers()) {
            System.out.println(m.getEffectiveName());
        }

        return false;
    }


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("add-App")) {
            System.out.println("Обработчик кнопки с ID: " + event.getComponentId());
            System.out.println("Проверяем пользователя: " + event.getUser().getAsTag());

            if (!isInGos(event.getUser())) {
                event.reply("Вы должны быть участником LunLand ГОС для подачи заявки!")
                        .setEphemeral(true).queue();
                System.out.println("Пользователь не прошел проверку на участие в гильдии.");
                return;
            }

            System.out.println("Пользователь успешно прошел проверку на участие в гильдии.");
            Modal modal = Modal.create("modal-cw", "Подача Заявки")
                    .addComponents(
                            ActionRow.of(TextInput.create("nick", "Ник в игре", TextInputStyle.SHORT).setRequired(true).setMinLength(1).build()),
                            ActionRow.of(TextInput.create("age", "Возраст", TextInputStyle.SHORT).setRequired(true).setMinLength(1).build()),
                            ActionRow.of(TextInput.create("find", "Откуда узнали о нас?", TextInputStyle.PARAGRAPH).setRequired(true).setMinLength(1).build()),
                            ActionRow.of(TextInput.create("time", "Сколько времени готовы уделять на проект?", TextInputStyle.PARAGRAPH).setRequired(true).setMinLength(1).build()),
                            ActionRow.of(TextInput.create("plans", "Расскажите о себе и своих планах на сервер", TextInputStyle.PARAGRAPH).setRequired(true).setMinLength(1).build())
                    )
                    .build();
            event.replyModal(modal).queue();
            setMember(event.getMember());
            return;
        }
        String username = ModalInteraction.map.get(Objects.requireNonNull(getMember()).getId());

        if (event.getComponentId().equals("accept")) {
            MessageEmbed updatedEmbed = new EmbedBuilder(ModalInteraction.embed)
                    .setColor(new Color(38, 198, 1))
                    .setFooter("Проверил: " + event.getUser().getName(), event.getUser().getAvatarUrl())
                    .build();
            event.editMessageEmbeds(updatedEmbed)
                    .setComponents()
                    .queue();

            Member members = plugin.getGuild().getMemberById(updatedEmbed.getFooter().getText());

            handleAccept(event, plugin.getGuild(), plugin.getGosguild(), members, username);
        } else if (event.getComponentId().equals("decline")) {
            Modal declineModal = Modal.create("modal-reason", "Причина отказа заявки")
                    .addComponents(ActionRow.of(TextInput.create("reason", "Укажите причину отказа", TextInputStyle.PARAGRAPH).setRequired(true).setMinLength(1).build()))
                    .build();
            event.replyModal(declineModal).queue();
        }
    }

    public void handleAccept(ButtonInteractionEvent event, Guild guild, Guild gosGuild, Member member, String username) {
        event.deferEdit().queue();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("**Приветствую тебя, дорогой " + member.getEffectiveName() + "**")
                .setDescription("*Поздравляю тебя с прохождением на сервер. Тебя добавят в вайтлист в течение минуты!*\n" +
                        "Прошу ознакомиться с правилами в канале <#" + plugin.getRuleChannel() + "> и скачать сборку модов сервера в канале <#" + plugin.getInfoChannel() + ">.\n" +
                        "*IP сервера: `mc.lunland.ru`*\n" +
                        "**Удачи и приятной игры! <3**")
                .setImage("https://cdn.discordapp.com/attachments/1328795825161375836/1328812485360353301/1ee94c4632cc8b14.png")
                .setColor(new Color(148, 128, 182))
                .setFooter("Проверяющий " + event.getUser().getName(), event.getUser().getAvatarUrl());

        member.getUser().openPrivateChannel().queue(channel -> {
            channel.sendMessageEmbeds(embed.build()).queue();
        });

        guild.modifyNickname(member, username).queue();
        gosGuild.modifyNickname(member, username).queue();

        Role roleMain = guild.getRoleById(plugin.getRoleMain());
        Role roleGos = gosGuild.getRoleById(plugin.getRoleGos());
        if (roleMain != null && roleGos != null) {
            guild.addRoleToMember(member, roleMain).queue();
            gosGuild.addRoleToMember(member, roleGos).queue();
        }

        event.getHook().editOriginal("Игрок " + username + " был добавлен в вайтлист.").queue();
    }
}
