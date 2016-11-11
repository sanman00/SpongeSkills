package com.comze.sanman00.spongeskills.sponge.data;

import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractMappedData;
import org.spongepowered.api.data.merge.MergeFunction;

public class SkillExperienceData extends AbstractMappedData<Skill, SkillExperience, SkillExperienceData, ImmutableSkillExperienceData> {
    public SkillExperienceData() {
        this(Maps.newHashMap());
    }
    
    public SkillExperienceData(Map<Skill, SkillExperience> map) {
        super(map, PluginKeys.SKILL_EXPERIENCE);
    }
    
    @Override
    public ImmutableSkillExperienceData asImmutable() {
        return new ImmutableSkillExperienceData(this.asMap());
    }

    @Override
    public Optional<SkillExperienceData> fill(DataHolder dataHolder, MergeFunction overlap) {
        return Optional.of(setValue(overlap.merge(this.copy(), dataHolder.get(SkillExperienceData.class).orElse(null)).asMap()));
    }

    @Override
    public Optional<SkillExperienceData> from(DataContainer container) {
        if (!container.contains(PluginKeys.SKILL_EXPERIENCE)) {
            return Optional.empty();
        }
        @SuppressWarnings("unchecked")
        Optional<Map<Skill, SkillExperience>> map = (Optional<Map<Skill, SkillExperience>>) container.getMap(PluginKeys.SKILL_EXPERIENCE.getQuery());
        return Optional.ofNullable(map.isPresent() ? setValue(map.get()) : null);
    }

    @Override
    public SkillExperienceData copy() {
        return new SkillExperienceData(this.asMap());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public Optional<SkillExperience> get(Skill key) {
        return Optional.ofNullable(this.asMap().get(key));
    }

    @Override
    public Set<Skill> getMapKeys() {
        return ImmutableSet.copyOf(this.asMap().keySet());
    }

    @Override
    public SkillExperienceData put(Skill key, SkillExperience value) {
        return performMapOperation(map -> map.put(key, value));
    }

    @Override
    public SkillExperienceData putAll(Map<? extends Skill, ? extends SkillExperience> map) {
        return performMapOperation(m -> m.putAll(map));
    }

    @Override
    public SkillExperienceData remove(Skill key) {
        return performMapOperation(map -> map.remove(key));
    }

    SkillExperienceData performMapOperation(Consumer<? super Map<Skill, SkillExperience>> op) {
        Map<Skill, SkillExperience> map = this.asMap();
        op.accept(map);
        return setValue(map);
    }
}
