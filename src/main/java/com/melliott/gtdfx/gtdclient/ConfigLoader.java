package com.melliott.gtdfx.gtdclient;

import com.melliott.gtdfx.gtdclient.service.JsonService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class ConfigLoader {
    private final JsonService jsonService;
    @Getter private Config config;

    public ConfigLoader(JsonService service) {
        this.jsonService = service;
        if (Files.exists(Path.of("config.json"))) loadConfigFromFile();
        else loadDefaults();
    }

    private void loadDefaults() {
        config = Config.DEFAULT;
    }

    private void loadConfigFromFile() {
        try {
            String fileContents = Files.readString(Path.of("config.json"));
            config = jsonService.parseConfig(fileContents);
        } catch (IOException e) {
            log.error("Failed to read config file: {}", e.getMessage());
            loadDefaults();
        }
    }
}
