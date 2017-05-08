/*
 * This file was taken from the previous attempt at this plugin (March/April 2016).
 */
package com.comze.sanman00.spongeskills.sponge.config;

import com.comze.sanman00.spongeskills.sponge.Main;
import java.io.IOException;
import java.nio.file.Path;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;

/**
 * Manages the configuration files of the plugin.
 */
public class ConfigManager {
    private final HoconConfigurationLoader configLoader;
    private CommentedConfigurationNode config;
    private final Path configFile;
    private final Logger logger;
    
    public ConfigManager(Path configFile) {
        this(Main.getInstance().getLogger(), configFile);
    }
    
    public ConfigManager(Logger logger, Path configFile) {
        this(logger, configFile, HoconConfigurationLoader.builder().setPath(configFile).build());
    }
    
    public ConfigManager(Logger logger, Path configFile, HoconConfigurationLoader configLoader) {
        if (configFile.toFile().isDirectory()) {
            throw new IllegalArgumentException("Expected a file for a config directory!");
        }
        
        this.logger = logger;
        this.configFile = configFile;
        this.configLoader = configLoader;
    }
    
    public void load() {
        load(this.logger);
    }
    
    public void load(Logger logger) {
        try {
            this.config = null;
            this.config = this.configLoader.load();
        }
        catch (IOException ex) {
            logger.error("Could not load config. Printing stacktrace and loading empty config... ");
            ex.printStackTrace();
            this.config = this.configLoader.createEmptyNode();
        }
    }
    
    public CommentedConfigurationNode getConfig() {
        return this.config;
    }
    
    public Path getConfigPath() {
        return this.configFile;
    }
    
    public void save() {
        save(this.logger);
    }
    
    public void save(Logger logger) {
        try {
            this.configLoader.save(this.config);
        }
        catch (IOException ex) {
            logger.error("Could not save config. Printing stack trace... ");
            ex.printStackTrace();
        }
    }
}
