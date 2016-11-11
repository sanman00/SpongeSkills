package com.comze.sanman00.spongeskills.sponge.data;

import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import java.util.Map;
import java.util.Optional;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.entity.living.player.Player;

public class SkillExperienceDataManipulatorBuilder extends AbstractDataBuilder<SkillExperienceData> implements DataManipulatorBuilder<SkillExperienceData, ImmutableSkillExperienceData> {
    public SkillExperienceDataManipulatorBuilder() {
        super(SkillExperienceData.class, 1);
    }
    
    @Override
    public SkillExperienceData create() {
        return new SkillExperienceData();
    }

    @Override
    public Optional<SkillExperienceData> createFrom(DataHolder dataHolder) {
        if (!(dataHolder instanceof Player)) {
            return Optional.empty();
        }
        
        return create().fill(dataHolder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<SkillExperienceData> buildContent(DataView container) throws InvalidDataException {
        if (!container.contains(PluginKeys.SKILL_EXPERIENCE)) {
            return Optional.empty();
        }
        
        return Optional.ofNullable(new SkillExperienceData().set(PluginKeys.SKILL_EXPERIENCE, (Map<Skill, SkillExperience>) container.getMap(PluginKeys.SKILL_EXPERIENCE.getQuery()).get()));
    }
}
