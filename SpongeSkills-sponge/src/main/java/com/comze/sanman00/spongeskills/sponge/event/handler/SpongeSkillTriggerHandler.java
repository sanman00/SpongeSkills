package com.comze.sanman00.spongeskills.sponge.event.handler;

import com.comze.sanman00.spongeskills.api.event.SkillTriggerEvent;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.config.PluginDataManager;
import com.comze.sanman00.spongeskills.sponge.player.SpongePlayerWrapper;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;

public class SpongeSkillTriggerHandler implements Consumer<SkillTriggerEvent> {
    @Override
    public void accept(SkillTriggerEvent e) {
        Event event = ((Cause) e.getEventCause().get()).first(Event.class).get();
        Skill skill = e.getSkill();
        SpongePlayerWrapper player = (SpongePlayerWrapper) e.getPlayer();
        if (event instanceof ChangeBlockEvent.Break) {
            BlockSnapshot snapshot = ((ChangeBlockEvent.Break) event).getTransactions().get(0).getOriginal();
            BlockType type = ((ChangeBlockEvent.Break) event).getTransactions().get(0).getOriginal().getState().getType();
            UUID world = ((ChangeBlockEvent.Break) event).getTransactions().get(0).getDefault().getWorldUniqueId();
            Map<BlockType, Integer> acceptedBlocksForSkill = PluginDataManager.getAcceptedBlocks().get(skill);
            if (acceptedBlocksForSkill != null && acceptedBlocksForSkill.containsKey(type) && !PluginDataManager.getBlockTrackers().get(world).isBeingTracked(snapshot.getPosition(), player.getPlayerUUID())) {
                Integer expToGive = acceptedBlocksForSkill.get(type);
                if (expToGive != null) {
                    player.giveExperience(expToGive, skill);
                }
            }
            
            if (PluginDataManager.getBlockTrackers().get(world).isBeingTracked(snapshot.getPosition(), player.getPlayerUUID())) {
                PluginDataManager.getBlockTrackers().get(world).removeBlock(snapshot.getPosition(), player.getPlayerUUID());
            }
        }
        
        if (event instanceof ChangeBlockEvent.Place) {
            PluginDataManager.getBlockTrackers().get(((ChangeBlockEvent.Place) event).getTransactions().get(0).getDefault().getWorldUniqueId()).addBlock(((ChangeBlockEvent.Place) event).getTransactions().get(0).getOriginal().getPosition(), player.getPlayerUUID());
        }
    }
}
