package com.comze.sanman00.spongeskills.sponge;

import com.comze.sanman00.spongeskills.api.event.EventBase;
import com.comze.sanman00.spongeskills.api.event.PlayerExperienceChangeEvent;
import com.comze.sanman00.spongeskills.api.event.SkillTriggerEvent;
import com.comze.sanman00.spongeskills.sponge.config.ConfigManager;
import com.comze.sanman00.spongeskills.sponge.config.PluginDataManager;
import com.comze.sanman00.spongeskills.sponge.config.SkillTriggerDataManager;
import com.comze.sanman00.spongeskills.sponge.data.ImmutableSkillExperienceData;
import com.comze.sanman00.spongeskills.sponge.data.SkillExperienceData;
import com.comze.sanman00.spongeskills.sponge.data.SkillExperienceDataManipulatorBuilder;
import com.comze.sanman00.spongeskills.sponge.event.SpongeSkillTriggerEvent;
import com.comze.sanman00.spongeskills.sponge.event.handler.SpongePlayerExperienceChangeHandler;
import com.comze.sanman00.spongeskills.sponge.event.handler.SpongeSkillTriggerHandler;
import com.comze.sanman00.spongeskills.sponge.event.registry.EventRegistryImpl;
import com.comze.sanman00.spongeskills.sponge.player.SpongePlayerWrapper;
import com.comze.sanman00.spongeskills.sponge.skill.SpongeDefaultSkills;
import com.comze.sanman00.spongeskills.sponge.tracker.BlockTracker;
import com.flowpowered.math.vector.Vector3i;
import com.google.inject.Inject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.event.world.UnloadWorldEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.World;

@Plugin(id = Main.PLUGIN_ID, name = Main.PLUGIN_NAME, version = Main.PLUGIN_VERSION, description = Main.PLUGIN_DESC)
public final class Main {
    public static final String PLUGIN_ID = "spongeskills";
    public static final String PLUGIN_NAME = "SpongeSkills";
    public static final String PLUGIN_VERSION = "@{version}";
    public static final String PLUGIN_DESC = "A plugin that adds skills to Minecraft";
    public static final Main INSTANCE = new Main();
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    @Inject
    private Logger logger;
    private ConfigManager configManager;
    private Consumer<PlayerExperienceChangeEvent> playerExpChangeHandler;
    private Consumer<SkillTriggerEvent> skillTriggerHandler;

    public Main() {
        
    }
    
    // Utility methods
    
    public Logger getLogger() {
        if (this.logger == null) {
            throw new IllegalStateException("Logger is null - was the plugin not initialised?");
        }
        return this.logger;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }
    
    // Event handlers
    
    @Listener
    public void onPreInit(GamePreInitializationEvent e) {
        Sponge.getDataManager().register(SkillExperienceData.class, ImmutableSkillExperienceData.class, new SkillExperienceDataManipulatorBuilder());
        SpongeDefaultSkills.init();
        //Load config
        this.configManager = new ConfigManager(this.configDir.toAbsolutePath().resolve("config.hocon"));
        this.configManager.load();
        PluginDataManager.init(this.configManager);
        //Create and register plugin event handlers
        this.skillTriggerHandler = new SpongeSkillTriggerHandler();
        this.playerExpChangeHandler = new SpongePlayerExperienceChangeHandler();
        EventRegistryImpl.IMPL_INSTANCE.registerEventHandler(PlayerExperienceChangeEvent.class, this.playerExpChangeHandler);
        EventRegistryImpl.IMPL_INSTANCE.registerEventHandler(SkillTriggerEvent.class, this.skillTriggerHandler);
    }
    
    @Listener
    public void onServerStarting(GameStartingServerEvent e) {
        readWorldData(); //read data here since worlds are now loaded
    }
    
    @Listener
    public void onWorldLoad(LoadWorldEvent e) {
        //Create BlockTrackers for each world
        World world = e.getTargetWorld();
        if (world != null) {
            PluginDataManager.getBlockTrackers().put(world.getUniqueId(), new BlockTracker(world));
        }
    }
    
    @Listener
    public void onWorldUnload(UnloadWorldEvent e) {
        //Remove world as it is not being used
        //TODO maybe save the data beforehand so it is not lost?
        //PluginDataManager.getBlockTrackers().remove(e.getTargetWorld().getUniqueId());
    }
    
    @Listener
    public void onEventFire(Event event, @First Player player) {
        SkillTriggerDataManager.getAllTriggers().entrySet().stream()
                .filter(entry -> entry.getValue().contains(event.getClass()))
                .forEach(entry -> {
                    Sponge.getEventManager().post(SpongeSkillTriggerEvent.builder()
                            .cause(Cause.of(NamedCause.of("Event", event), NamedCause.of("Firer", Sponge.getPluginManager().getPlugin(PLUGIN_ID).get())))
                            .skill(entry.getKey())
                            .player(new SpongePlayerWrapper(player))
                            .build()
                    );
                });
        if (event instanceof EventBase) {
            @SuppressWarnings("unchecked")
            Class<EventBase> eventClass = (Class<EventBase>) (Class) event.getClass();
            EventRegistryImpl.IMPL_INSTANCE.getHandlersForEvent(eventClass).forEach(handler -> handler.accept((EventBase) event));
        }
    }
    
    @Listener
    public void onServerStopping(GameStoppingServerEvent e) {
        this.configManager.save();
        this.writeWorldData();
    }

    @Listener
    public void onGameReload(GameReloadEvent e) {
        this.readWorldData();
        this.configManager.load();
    }

    private void readWorldData() {
        int bytesRead = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(PluginDataManager.getBlockOwnershipDataFile().toFile()))))) {
            char[] readBuffer = new char[1024];
            List<char[]> chars = new ArrayList<>();
            
            while ((bytesRead += br.read(readBuffer)) != -1) {
                chars.add(readBuffer);
            }
            StringBuilder sb = new StringBuilder();
            chars.forEach(sb::append);
            UUID worldUUID = null; //used because UUID from line[1] could be a player UUID as well
            Vector3i blockPos = null;
            boolean playerFoundLast = false;
            for (String str : sb.toString().split(System.lineSeparator())) { //split string into lines
                //each line follows a similar format to either "(P/W)|uuid-of-player-or-world" or "x,y,z"
                String[] line = str.split("|");
                UUID uuid;
                try {
                    uuid = UUID.fromString(line[1]);
                }
                catch (IllegalArgumentException e) {
                    this.logger.error("Found invalid UUID", e);
                    return;
                }
                if (playerFoundLast && blockPos == null) {
                    String[] locString = str.split(",");
                    try {
                        int x = Integer.parseInt(locString[0].trim());
                        int y = Integer.parseInt(locString[1].trim());
                        int z = Integer.parseInt(locString[2].trim());
                        blockPos = new Vector3i(x, y, z);
                    }
                    catch (NumberFormatException e2) {
                        this.logger.error("Invalid location found", e2);
                    }
                }
                switch (line[0]) {
                    case "W":
                        worldUUID = uuid;
                        PluginDataManager.getBlockTrackers().put(uuid, new BlockTracker(uuid));
                        playerFoundLast = false;
                        break;
                    case "P":
                        if (worldUUID == null) {
                            throw new IOException("Player UUID read but no world UUID has been read");
                        }
                        PluginDataManager.getBlockTrackers().get(worldUUID).addBlock(blockPos, uuid);
                        playerFoundLast = true;
                }
            }
        }
        catch (IOException ex) {
            this.logger.error("Unable to read world data file", ex);
            this.logger.debug("Clearing block tracker map...");
            PluginDataManager.getBlockTrackers().clear();
        }
    }
    
    private void writeWorldData() {
        PluginDataManager.getBlockTrackers().forEach((worldID, tracker) -> {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(PluginDataManager.getBlockOwnershipDataFile().toFile()))))) {
                bw.write("W|" + worldID);
                bw.newLine();
                tracker.getBlockOwnerMap().forEach((uuid, locations) -> {
                    try {
                        bw.write("P|" + uuid);
                        bw.newLine();
                        locations.forEach(loc -> {
                            try {
                                bw.write(loc.getX() + "," + loc.getY() + "," + loc.getZ());
                                bw.newLine();
                            }
                            catch (IOException ex) {
                                throw new UncheckedIOException(ex);
                            }
                        });
                    }
                    catch (Exception ex) {
                        if (ex instanceof IOException) {
                            throw new UncheckedIOException((IOException) ex);
                        }
                        else {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
            catch (IOException ex) {
                this.logger.error("Unable to write to world data file", ex);
            }
        });
    }
}
