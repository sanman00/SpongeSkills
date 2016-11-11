package com.comze.sanman00.spongeskills.sponge.config;

import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.Main;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Event;

/**
 * Handles what causes skills to be triggered.
 */
public final class SkillTriggerDataManager {
    private static final Map<Skill, Set<Event>> SKILL_TRIGGERS = Maps.newHashMap();
    //Defaults are provided in case the config does not contain a set of block triggers
    public static final Set<BlockType> DEFAULT_MINING_BLOCK_TRIGGERS = ImmutableSet.of(BlockTypes.STONE, BlockTypes.COBBLESTONE, BlockTypes.IRON_ORE, BlockTypes.GOLD_ORE, BlockTypes.DIAMOND_ORE, BlockTypes.REDSTONE_ORE, BlockTypes.COAL_ORE, BlockTypes.OBSIDIAN, BlockTypes.END_STONE);
    public static final Set<BlockType> DEFAULT_WOOD_CUTTING_BLOCK_TRIGGERS = ImmutableSet.of(BlockTypes.LOG, BlockTypes.LOG2);

    private SkillTriggerDataManager() {
        
    }
    
    public static void registerTrigger(Skill skill, Event event) {
        if (!SKILL_TRIGGERS.containsKey(skill)) {
            SKILL_TRIGGERS.put(skill, Sets.newHashSet(event));
            return;
        }
        Set<Event> events = SKILL_TRIGGERS.get(skill);
        
        if (!events.contains(event)) {
            events.add(event);
        }
        else {
            Main.INSTANCE.getLogger().warn("Event " + event + "already registered with skill " + skill + "!");
        }
    }
    
    public static Set<Event> getTriggersFor(Skill skill) {
        return ImmutableSet.copyOf(SKILL_TRIGGERS.getOrDefault(skill, ImmutableSet.of()));
    }

    public static Map<Skill, Set<Event>> getAllTriggers() {
        return ImmutableMap.copyOf(SKILL_TRIGGERS);
    }
}
