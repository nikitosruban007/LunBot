package org.example;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Config {
    private static final String CONFIG_FILE = "config.yml";
    public static String botToken;
    public static String guildId;
    public static String gosguildId;
    public static String appId;
    public static String rvId;
    public static String ruleChannel;
    public static String infoChannel;
    public static String roleMain;
    public static String roleGos;

    public Config() {
        botToken = "YOUR_BOT_TOKEN";
        guildId = "YOUR_GUILD_ID";
        gosguildId = "YOUR_GOS_GUILD_ID";
        appId = "YOUR_APP_ID";
        rvId = "YOUR_RV_ID";
        ruleChannel = "YOUR_RULE_CHANNEL";
        infoChannel = "YOUR_INFO_CHANNEL";
        roleMain = "YOUR_ROLE_MAIN";
        roleGos = "YOUR_ROLE_GOS";
    }

    public static void saveConfig(Config config) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            yaml.dump(config.toMap(), writer);
        }
    }

    public static Config loadConfig() throws IOException {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            Config defaultConfig = new Config();
            saveConfig(defaultConfig);
            System.out.println("Файл config.yml создан.");
            System.exit(0);
        }

        LoaderOptions loaderOptions = new LoaderOptions();
        Yaml yaml = new Yaml(loaderOptions);
        try (FileReader reader = new FileReader(file)) {
            Map<String, Object> data = yaml.load(reader);
            return fromMap(data);
        }
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "botToken", botToken,
                "guildId", guildId,
                "gosguildId", gosguildId,
                "appId", appId,
                "rvId", rvId,
                "ruleChannel", ruleChannel,
                "infoChannel", infoChannel,
                "roleMain", roleMain,
                "roleGos", roleGos
        );
    }
    public static Config fromMap(Map<String, Object> data) {
        Config config = new Config();
        botToken = (String) data.get("botToken");
        guildId = (String) data.get("guildId");
        gosguildId = (String) data.get("gosguildId");
        appId = (String) data.get("appId");
        rvId = (String) data.get("rvId");
        ruleChannel = (String) data.get("ruleChannel");
        infoChannel = (String) data.get("infoChannel");
        roleMain = (String) data.get("roleMain");
        roleGos = (String) data.get("roleGos");
        return config;
    }

}

