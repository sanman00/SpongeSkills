package com.comze.sanman00.spongeskills.sponge.skill;

import com.comze.sanman00.spongeskills.api.skill.Skill;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.CatalogType;

public final class SpongeSkill implements Skill, CatalogType {
    private final String id;
    private final String name;

    public SpongeSkill(String id, String name) {
        this.id = Objects.requireNonNull(id, "Expected a non-null id!");
        this.name = StringUtils.isBlank(name) ? id : name;
    }
    
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "SpongeSkill{" + "id=" + this.id + ", name=" + this.name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpongeSkill other = (SpongeSkill) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.name, other.name);
    }
}
