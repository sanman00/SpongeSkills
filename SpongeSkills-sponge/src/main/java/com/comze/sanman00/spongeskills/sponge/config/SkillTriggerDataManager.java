package com.comze.sanman00.spongeskills.sponge.config;

import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.Main;
import com.comze.sanman00.spongeskills.sponge.util.MapUtil;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.event.Event;

/**
 * Handles what causes skills to be triggered.
 */
public final class SkillTriggerDataManager {
    /**
     * Enumerates all valid trigger types.
     */
    public static enum TriggerType {
        BLOCK(o -> o instanceof BlockType), 
        EVENT(o -> o instanceof Event), 
        OTHER(Predicates.alwaysTrue()::apply);
        private final Predicate<Object> validTypeTest;
        
        TriggerType(Predicate<Object> validTypeTest) {
            this.validTypeTest = validTypeTest;
        }
        
        public boolean testObject(Object obj) {
            return this.validTypeTest.test(obj);
        }
    }
    private static final Map<Skill, Map<TriggerType, Set<Object>>> SKILL_TRIGGERS = Maps.newHashMap();

    private SkillTriggerDataManager() {
        
    }
    
    public static void registerTrigger(Skill skill, TriggerType triggerType, Object obj) {
        if (!SKILL_TRIGGERS.containsKey(skill)) {
            if (!triggerType.testObject(obj)) {
                Main.INSTANCE.getLogger().warn("Could not add trigger to skill: object did not pass test set by trigger");
                return;
            }
            SKILL_TRIGGERS.put(skill, MapUtil.zip(triggerType, Sets.newHashSet(obj)));
        }
        
        Map<TriggerType, Set<Object>> triggerTypes = SKILL_TRIGGERS.get(skill);
        
        if (!triggerTypes.containsKey(triggerType)) {
            triggerTypes.put(triggerType, Sets.newHashSet(obj));
        }
        
        if (!triggerTypes.get(triggerType).add(obj)) {
            Main.INSTANCE.getLogger().warn("Trigger object already registered with this trigger type!");
        }
    }
    
    public static Map<TriggerType, Object> getTriggersFor(Skill skill) {
        return ImmutableMap.copyOf(SKILL_TRIGGERS.getOrDefault(skill, Collections.emptyMap()));
    }

    public static Map<Skill, Map<TriggerType, Set<Object>>> getAllTriggers() {
        return ImmutableMap.copyOf(SKILL_TRIGGERS);
    }
}
