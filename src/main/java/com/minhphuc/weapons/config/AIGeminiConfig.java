package com.minhphuc.weapons.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.minhphuc.weapons.WeaponsMod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;

public class AIGeminiConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static String apiKey = "YOUR_GEMINI_API_KEY_HERE";
    private static String model = "gemini-1.5-flash";

    public static void loadConfig() {
        try {
            Path configPath = FMLPaths.CONFIGDIR.get().resolve("weapons_ai_config.json");
            File configFile = configPath.toFile();

            if (!configFile.exists()) {
                saveConfig(configFile);
                WeaponsMod.LOGGER.info("Created default AI Gemini Config at: {}", configFile.getAbsolutePath());
                return;
            }

            try (FileReader reader = new FileReader(configFile)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                if (json != null) {
                    if (json.has("apiKey")) {
                        apiKey = json.get("apiKey").getAsString().trim();
                    }
                    if (json.has("model")) {
                        model = json.get("model").getAsString().trim();
                    }
                }
            }
        } catch (Exception e) {
            WeaponsMod.LOGGER.error("Failed to load weapons_ai_config.json", e);
        }
    }

    private static void saveConfig(File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            JsonObject json = new JsonObject();
            json.addProperty("apiKey", apiKey);
            json.addProperty("model", model);
            json.addProperty("_instruction", "Replace YOUR_GEMINI_API_KEY_HERE with your Google Gemini API key from Google AI Studio.");
            GSON.toJson(json, writer);
        } catch (Exception e) {
            WeaponsMod.LOGGER.error("Failed to save weapons_ai_config.json", e);
        }
    }

    public static String getApiKey() {
        loadConfig(); // Reload dynamically to allow on-the-fly config edits
        return apiKey;
    }

    public static String getModel() {
        return model;
    }

    public static boolean isApiKeyValid() {
        String key = getApiKey();
        return key != null && !key.isEmpty() && !key.equals("YOUR_GEMINI_API_KEY_HERE");
    }

    public static String getConfigAbsolutePath() {
        try {
            return FMLPaths.CONFIGDIR.get().resolve("weapons_ai_config.json").toFile().getAbsolutePath();
        } catch (Exception e) {
            return "config/weapons_ai_config.json";
        }
    }
}
