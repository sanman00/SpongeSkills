package com.comze.sanman00.spongeskills.sponge;

import com.comze.sanman00.spongeskills.api.skill.DefaultSkills;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.config.ConfigManager;
import com.comze.sanman00.spongeskills.sponge.config.SkillTriggerDataManager;
import com.comze.sanman00.spongeskills.sponge.data.ImmutableSkillExperienceData;
import com.comze.sanman00.spongeskills.sponge.data.SkillExperienceData;
import com.comze.sanman00.spongeskills.sponge.data.SkillExperienceDataManipulatorBuilder;
import com.comze.sanman00.spongeskills.sponge.event.SpongePlayerExperienceChangeEvent;
import com.comze.sanman00.spongeskills.sponge.event.SpongePlayerLevelChangeEvent;
import com.comze.sanman00.spongeskills.sponge.event.SpongeSkillTriggerEvent;
import com.comze.sanman00.spongeskills.sponge.player.SpongePlayerWrapper;
import com.comze.sanman00.spongeskills.sponge.skill.SpongeDefaultSkills;
import com.comze.sanman00.spongeskills.sponge.tracker.BlockTracker;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.event.world.UnloadWorldEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

@Plugin(id = Main.PLUGIN_ID, name = Main.PLUGIN_NAME, version = Main.PLUGIN_VERSION, description = Main.PLUGIN_DESC)
public final class Main {
    public static final String PLUGIN_ID = "spongeskills";
    public static final String PLUGIN_NAME = "SpongeSkills";
    public static final String PLUGIN_VERSION = "0.0.5";
    public static final String PLUGIN_DESC = "A plugin that adds skills to Minecraft";
    public static final Main INSTANCE = new Main();
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    @Inject
    private Logger logger;
    private ConfigManager configManager;
    private CommentedConfigurationNode acceptedBlocksNode;
    private Map<Skill, Set<BlockType>> acceptedBlocks;
    private Map<UUID, BlockTracker> blockTrackers;
    private Path blockOwnershipDataFile;

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
        this.acceptedBlocksNode = this.configManager.getConfig().getNode("Skills", "AcceptedBlocks");
        try {
            Map<Skill, Set<BlockType>> skillBlockMap = new HashMap<>();
            skillBlockMap.put(DefaultSkills.MINING, SkillTriggerDataManager.DEFAULT_MINING_BLOCK_TRIGGERS);
            skillBlockMap.put(DefaultSkills.WOOD_CUTTING, SkillTriggerDataManager.DEFAULT_WOOD_CUTTING_BLOCK_TRIGGERS);
            this.acceptedBlocks = this.acceptedBlocksNode.getValue(new TypeToken<Map<Skill, Set<BlockType>>>() {}, skillBlockMap);
        }
        catch (ObjectMappingException ex) {
            ex.printStackTrace();
        }
        this.blockTrackers = new HashMap<>();
        this.blockOwnershipDataFile = this.configDir.resolve("block_ownership.dat");
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
            this.blockTrackers.put(world.getUniqueId(), new BlockTracker(world));
        }
    }
    
    @Listener
    public void onWorldUnload(UnloadWorldEvent e) {
        //Remove world as it is not being used
        this.blockTrackers.remove(e.getTargetWorld().getUniqueId());
    }
    
    @Listener
    public void onEventFire(Event event, @First Player player) {
        SkillTriggerDataManager.getAllTriggers().entrySet().stream()
                .filter(entry -> entry.getValue().contains(event))
                .forEach(entry -> {
                    Sponge.getEventManager().post(SpongeSkillTriggerEvent.builder()
                            .cause(Cause.of(NamedCause.of("Event", event), NamedCause.of("Firer", Sponge.getPluginManager().getPlugin(PLUGIN_ID).get())))
                            .skill(entry.getKey())
                            .player(new SpongePlayerWrapper(player))
                            .build()
                    );
                });
    }
    
    @Listener
    public void onServerStopping(GameStoppingServerEvent e) {
        this.configManager.save();
        this.writeWorldData();
    }
    
    // Plugin Event Handlers
    
    @Listener
    public void onPlayerExperienceChange(SpongePlayerExperienceChangeEvent e) {
        e.getPlayer().getWrappedPlayer().sendMessage(ChatTypes.ACTION_BAR, Text.of(TextColors.YELLOW, "You got" + e.getDifference() + " experience!")); //test
    }
    
    @Listener
    public void onPlayerLevelChange(SpongePlayerLevelChangeEvent e) {
        
    }
    
    @Listener
    public void onSkillTrigger(SpongeSkillTriggerEvent e) {
        Event event = e.getCause().first(Event.class).get();
        Skill skill = e.getSkill();
        SpongePlayerWrapper player = e.getPlayer();
        if (event instanceof ChangeBlockEvent.Break) {
            BlockSnapshot snapshot = ((ChangeBlockEvent.Break) event).getTransactions().get(0).getOriginal();
            BlockType type = ((ChangeBlockEvent.Break) event).getTransactions().get(0).getOriginal().getState().getType();
            UUID world = ((ChangeBlockEvent.Break) event).getTransactions().get(0).getDefault().getWorldUniqueId();
            
            if (this.acceptedBlocks.get(skill).contains(type) && !this.blockTrackers.get(world).isBeingTracked(snapshot.getPosition(), player.getPlayerUUID())) {
                player.giveExperience(1, skill);
                //TODO Check how much experience this block gives
            }
            
            if (this.blockTrackers.get(world).isBeingTracked(snapshot.getPosition(), player.getPlayerUUID())) {
                this.blockTrackers.get(world).removeBlock(snapshot.getPosition(), player.getPlayerUUID());
            }
        }
        
        if (event instanceof ChangeBlockEvent.Place) {
            this.blockTrackers.get(((ChangeBlockEvent.Place) event).getTransactions().get(0).getDefault().getWorldUniqueId()).addBlock(((ChangeBlockEvent.Place) event).getTransactions().get(0).getOriginal().getPosition(), player.getPlayerUUID());
        }
    }

    private void readWorldData() {
        int bytesRead = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(this.blockOwnershipDataFile.toFile()))))) {
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
                        this.blockTrackers.put(uuid, new BlockTracker(uuid));
                        playerFoundLast = false;
                        break;
                    case "P":
                        if (worldUUID == null) {
                            throw new IOException("Player UUID read but no world UUID has been read");
                        }
                        this.blockTrackers.get(worldUUID).addBlock(blockPos, uuid);
                        playerFoundLast = true;
                }
            }
        }
        catch (IOException ex) {
            this.logger.error("Unable to read world data file", ex);
            this.logger.debug("Clearing block tracker map...");
            this.blockTrackers.clear();
        }
    }
    
    private void writeWorldData() {
        this.blockTrackers.forEach((worldID, tracker) -> {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(this.blockOwnershipDataFile.toFile()))))) {
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
