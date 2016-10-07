package com.comze.sanman00.spongeskills.sponge.skill;

import com.comze.sanman00.spongeskills.api.skill.DefaultSkills;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * A class that handles the registering and various other aspects of skills.
 */
public final class SpongeDefaultSkills {
    private SpongeDefaultSkills() {
        
    }
    
    public static void init() {
        setSkillFieldValue("MINING", new SpongeSkill("sanman.mining", "Mining"));
    }
    
    private static void setSkillFieldValue(String skillName, Object value) {
        try {
            Field field = DefaultSkills.class.getDeclaredField(skillName);
            field.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, value);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
