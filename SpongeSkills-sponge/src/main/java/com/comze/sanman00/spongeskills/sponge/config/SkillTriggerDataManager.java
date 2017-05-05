package com.comze.sanman00.spongeskills.sponge.config;

import com.comze.sanman00.spongeskills.api.skill.DefaultSkills;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.Main;
import com.comze.sanman00.spongeskills.sponge.util.MapUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.action.FishingEvent;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.tileentity.BrewingEvent;
import org.spongepowered.api.event.entity.AttackEntityEvent;
import org.spongepowered.api.event.entity.TameEntityEvent;

/**
 * Handles what causes skills to be triggered.
 */
public final class SkillTriggerDataManager {
    private static final Map<Skill, Set<Class<? extends Event>>> SKILL_TRIGGERS = Maps.newHashMap();
    //Defaults are provided in case the config does not contain a map of block triggers to experience amounts
    public static final Map<BlockType, Integer> DEFAULT_MINING_BLOCK_TRIGGERS = MapUtil.zip(
            ImmutableSet.of(BlockTypes.STONE, BlockTypes.COBBLESTONE, BlockTypes.IRON_ORE, BlockTypes.GOLD_ORE, BlockTypes.DIAMOND_ORE, BlockTypes.REDSTONE_ORE, BlockTypes.COAL_ORE, BlockTypes.OBSIDIAN, BlockTypes.END_STONE), 
            ImmutableSet.of(1, 1, 10, 15, 30, 20, 5, 40, 45)
    );
    public static final Map<BlockType, Integer> DEFAULT_WOOD_CUTTING_BLOCK_TRIGGERS = MapUtil.zip(ImmutableSet.of(BlockTypes.LOG, BlockTypes.LOG2), ImmutableSet.of(5, 10));

    private SkillTriggerDataManager() {
        throw new UnsupportedOperationException("Cannot create SkillTriggerDataManager instance");
    }
    
    public static void registerTrigger(Skill skill, Class<? extends Event> event) {
        if (!SKILL_TRIGGERS.containsKey(skill)) {
            SKILL_TRIGGERS.put(skill, Sets.newHashSet(event));
            return;
        }
        Set<Class<? extends Event>> events = SKILL_TRIGGERS.get(skill);
        
        if (!events.contains(event)) {
            events.add(event);
        }
        else {
            Main.INSTANCE.getLogger().warn("Event " + event + "already registered with skill " + skill + "!");
        }
    }
    
    public static Set<Class<? extends Event>> getTriggersFor(Skill skill) {
        return ImmutableSet.copyOf(SKILL_TRIGGERS.getOrDefault(skill, ImmutableSet.of()));
    }

    public static Map<Skill, Set<Class<? extends Event>>> getAllTriggers() {
        return ImmutableMap.copyOf(SKILL_TRIGGERS);
    }
    
    static {
        registerTrigger(DefaultSkills.MINING, ChangeBlockEvent.Break.class);
        registerTrigger(DefaultSkills.WOOD_CUTTING, ChangeBlockEvent.Break.class);
        registerTrigger(DefaultSkills.ALCHEMY, BrewingEvent.Finish.class);
        registerTrigger(DefaultSkills.FARMING, ChangeBlockEvent.Break.class);
        registerTrigger(DefaultSkills.AXE_COMBAT, AttackEntityEvent.class);
        registerTrigger(DefaultSkills.SWORD_COMBAT, AttackEntityEvent.class);
        registerTrigger(DefaultSkills.UNARMED_COMBAT, AttackEntityEvent.class);
        registerTrigger(DefaultSkills.TAMING, TameEntityEvent.class);
        registerTrigger(DefaultSkills.FISHING, FishingEvent.class);
        registerTrigger(DefaultSkills.ARCHERY, AttackEntityEvent.class);
    }
}
