package com.comze.sanman00.spongeskills.sponge;

import com.comze.sanman00.spongeskills.api.skill.DefaultSkills;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.config.ConfigManager;
import com.comze.sanman00.spongeskills.sponge.config.SkillTriggerDataManager;
import com.comze.sanman00.spongeskills.sponge.event.SpongePlayerExperienceChangeEvent;
import com.comze.sanman00.spongeskills.sponge.event.SpongePlayerLevelChangeEvent;
import com.comze.sanman00.spongeskills.sponge.event.SpongeSkillTriggerEvent;
import com.comze.sanman00.spongeskills.sponge.player.SpongePlayerWrapper;
import com.comze.sanman00.spongeskills.sponge.skill.SpongeDefaultSkills;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
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
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

@Plugin(id = Main.PLUGIN_ID, name = Main.PLUGIN_NAME, version = Main.PLUGIN_VERSION, description = Main.PLUGIN_DESC)
public final class Main {
    public static final String PLUGIN_ID = "spongeskills";
    public static final String PLUGIN_NAME = "SpongeSkills";
    public static final String PLUGIN_VERSION = "0.0.2";
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
        if (event instanceof ChangeBlockEvent) {
            BlockType type = ((ChangeBlockEvent) event).getTransactions().get(0).getOriginal().getState().getType();
            
            if (this.acceptedBlocks.get(skill).contains(type)) {
                e.getPlayer().giveExperience(1, skill);
                //TODO Check how much experience this block gives
            }
        }
    }
}
