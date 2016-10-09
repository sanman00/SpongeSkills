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
        setSkillFieldValue("WOOD_CUTTING", new SpongeSkill("sanman.woodcutting", "Woodcutting"));
        setSkillFieldValue("ALCHEMY", new SpongeSkill("sanman.alchemy", "Alchemy"));
        setSkillFieldValue("REPAIRING", new SpongeSkill("sanman.repairing", "Repairing"));
        setSkillFieldValue("SWORD_COMBAT", new SpongeSkill("sanman.swordcombat", "Sword Combat"));
        setSkillFieldValue("SMELTING", new SpongeSkill("sanman.smelting", "Smelting"));
        setSkillFieldValue("TAMING", new SpongeSkill("sanman.taming", "Taming"));
        setSkillFieldValue("AXE_COMBAT", new SpongeSkill("sanman.axecombat", "Axe Combat"));
        setSkillFieldValue("UNARMED_COMBAT", new SpongeSkill("sanman.unarmedcombat", "Unarmed Combat"));
        setSkillFieldValue("FARMING", new SpongeSkill("sanman.farming", "Farming"));
        setSkillFieldValue("ARCHERY", new SpongeSkill("sanman.archery", "Archery"));
    }
    
    private static void setFieldValue(Class<?> clazz, String skillName, Object value) {
        try {
            Field field = clazz.getDeclaredField(skillName);
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
    
    private static void setSkillFieldValue(String skillName, Object value) {
        setFieldValue(DefaultSkills.class, skillName, value);
    }
}
