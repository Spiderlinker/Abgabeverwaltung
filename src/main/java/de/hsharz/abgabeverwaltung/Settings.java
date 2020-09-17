package de.hsharz.abgabeverwaltung;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

public class Settings {

    private static Properties emailSettings = new Properties();
    private static Properties emailServerSettings = new Properties();

    private Settings() {
        // Utility class
    }

    public static Properties getEmailSettings() {
        return emailSettings;
    }

    public static Properties getEmailServerSettings() {
        return emailServerSettings;
    }

    public static void reloadEmailProperties() {
        reloadProperties(emailSettings, Config.EMAIL_CONFIGURATION_FILE);
    }

    public static void reloadEmailServerProperties() {
        reloadProperties(emailServerSettings, Config.EMAIL_SERVER_CONFIGURATION_FILE);
    }

    private static void reloadProperties(Properties properties, File propertiesFile) {
        try {
            if (!propertiesFile.exists()) {
                Files.copy(Settings.class.getResourceAsStream("/files/" + propertiesFile.getName()), propertiesFile.toPath());
            }
            try (FileInputStream input = new FileInputStream(propertiesFile)) {
                properties.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveEmailProperties() {
        saveProperties(emailSettings, Config.EMAIL_CONFIGURATION_FILE);
    }

    public static void saveEmailServerProperties() {
        saveProperties(emailServerSettings, Config.EMAIL_SERVER_CONFIGURATION_FILE);
    }

    private static void saveProperties(Properties properties, File propertiesFile) {
        try {
            propertiesFile.getParentFile().mkdirs();
            propertiesFile.createNewFile();
            try (FileOutputStream outputStream = new FileOutputStream(propertiesFile)) {
                properties.store(outputStream, "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

