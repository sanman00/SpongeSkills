package com.comze.sanman00.spongeskills.sponge.data;

import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.api.skill.experience.SkillExperience;
import java.util.Map;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableMappedData;

public class ImmutableSkillExperienceData extends AbstractImmutableMappedData<Skill, SkillExperience, ImmutableSkillExperienceData, SkillExperienceData> {
    public ImmutableSkillExperienceData(Map<Skill, SkillExperience> map) {
        super(map, PluginKeys.SKILL_EXPERIENCE);
    }
    
    @Override
    public SkillExperienceData asMutable() {
        return new SkillExperienceData(this.asMap());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }
}
