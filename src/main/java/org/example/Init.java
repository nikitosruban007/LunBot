package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.ButtonInteraction;
import org.example.LunBot;
import org.example.ModalInteraction;

public class Init {
    public static JDA initializeBot(String token, LunBot plugin) throws InterruptedException {
        try {
            JDA jda = JDABuilder.createDefault(token)
                    .setEnabledIntents(
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.DIRECT_MESSAGES,
                            GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                            GatewayIntent.SCHEDULED_EVENTS,
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.MESSAGE_CONTENT
                    )
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.MEMBER_OVERRIDES)
                    .addEventListeners(new ModalInteraction(), new ButtonInteraction())
                    .addEventListeners(
                            new ModalInteraction(),
                            new ButtonInteraction()
                    )
                    .build();

            jda.awaitReady();

            plugin.initialize(jda);


            return jda;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
