package com.comze.sanman00.spongeskills.sponge.data;

import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import com.google.common.reflect.TypeToken;
import java.util.Map;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.Value;

public final class PluginKeys {
    private PluginKeys() {
        throw new RuntimeException("Tried to create PluginKeys instance");
    }
    
    public static final Key<MapValue<Skill, SkillExperience>> SKILL_EXPERIENCE = KeyFactory.makeMapKey(new TypeToken<Map<Skill, SkillExperience>>() {}, new TypeToken<MapValue<Skill, SkillExperience>>() {}, DataQuery.of("SkillExperience"), "sanman.skillexperience", "Skill Experience");
    public static final Key<Value<Boolean>> OWNED_BLOCK_MARKER = KeyFactory.makeSingleKey(TypeToken.of(Boolean.class), new TypeToken<Value<Boolean>>() {}, DataQuery.of("OwnedBlockMarker"), "sanman.ownedblockmarker", "Owned Block Marker");
}
