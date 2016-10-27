package com.comze.sanman00.spongeskills.sponge;

import com.comze.sanman00.spongeskills.api.event.PlayerExperienceChangeEvent;
import com.comze.sanman00.spongeskills.api.event.PlayerLevelChangeEvent;
import com.comze.sanman00.spongeskills.api.event.SkillTriggerEvent;
import com.comze.sanman00.spongeskills.sponge.config.SkillTriggerDataManager;
import com.comze.sanman00.spongeskills.sponge.event.SpongeSkillTriggerEvent;
import com.comze.sanman00.spongeskills.sponge.skill.SpongeDefaultSkills;
import com.google.inject.Inject;
import java.util.Arrays;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

@Plugin(id = Main.PLUGIN_ID, name = Main.PLUGIN_NAME, version = Main.PLUGIN_VERSION, description = Main.PLUGIN_DESC)
public final class Main {
    public static final String PLUGIN_ID = "spongeskills";
    public static final String PLUGIN_NAME = "SpongeSkills";
    public static final String PLUGIN_VERSION = "0.0.0";
    public static final String PLUGIN_DESC = "A plugin that adds skills to Minecraft";
    public static final Main INSTANCE = new Main();
    @Inject
    private Logger logger;

    private Main() {
        
    }
    
    // Utility methods
    
    public Logger getLogger() {
        if (this.logger == null) {
            throw new IllegalStateException("Logger is null - was the plugin not initialised?");
        }
        return this.logger;
    }
    
    // Event handlers
    
    @Listener
    public void onPreInit(GamePreInitializationEvent e) {
        SpongeDefaultSkills.init();
    }
    
    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break e) {
        e.getTransactions().forEach(transaction -> {
            SkillTriggerDataManager.getAllTriggers().values().forEach(triggerMap -> {
                triggerMap.get(SkillTriggerDataManager.TriggerType.BLOCK).forEach(obj -> this.processTriggerObject(obj, SkillTriggerDataManager.TriggerType.BLOCK, false, transaction));
            });
        });
    }
    
    /**
     * Processes the given objects by performing the appropriate action based on 
     * their type.
     * 
     * The first object must be the trigger object, the second should be the trigger
     * type and the third should be whether or not the trigger object is executable
     * (this parameter may be ignored for some trigger types).
     * 
     * @param objects Objects to be processed
     */
    @SuppressWarnings("unchecked")
    void processTriggerObject(Object... objects) {
        if (objects[0] == null) {
            throw new IllegalArgumentException("Trigger object must not be null!");
        }
        switch ((SkillTriggerDataManager.TriggerType) objects[1]) {
            case BLOCK:
                BlockType type = (BlockType) objects[0];
                if (type == ((Transaction<BlockSnapshot>) objects[3]).getOriginal().getState().getType()) {
                    Sponge.getEventManager().post(SpongeSkillTriggerEvent.builder()
                            .cause(Cause.of(NamedCause.of("TriggerObject", objects[0]), NamedCause.of("TriggerType", objects[1])))
                            .build());
                }
                return;
        }
        Object[] arguments = Arrays.copyOfRange(objects, 1, objects.length - 1); //copy the array except the trigger object
    }
    
    // Plugin Event Handlers
    
    @Listener
    public void onPlayerExperienceChange(PlayerExperienceChangeEvent e) {
        ((Player) e.getPlayer().getWrappedPlayer()).sendMessage(ChatTypes.ACTION_BAR, Text.of(TextColors.YELLOW, "You got" + e.getDifference() + " experience!")); //test
    }
    
    @Listener
    public void onPlayerLevelChange(PlayerLevelChangeEvent e) {
        
    }
    @Listener
    public void onSkillTrigger(SkillTriggerEvent e) {
        
    }
}
