package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

/**
 * Singleton configuration for CCRM.
 */
public final class AppConfig {
    private static volatile AppConfig instance;
    private final Path dataFolder;
    private final Instant startedAt;

    private AppConfig() {
        this.dataFolder = Paths.get(System.getProperty("user.home"), "ccrm_data");
        this.startedAt = Instant.now();
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) instance = new AppConfig();
            }
        }
        return instance;
    }

    public Path getDataFolder() { return dataFolder; }
    public Instant getStartedAt() { return startedAt; }

    @Override
    public String toString() {
        return "AppConfig{dataFolder=" + dataFolder + ", startedAt=" + startedAt + "}";
    }
}
