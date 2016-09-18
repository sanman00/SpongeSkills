package com.comze.sanman00.spongeskills.api.util.catalog;

import com.comze.sanman00.spongeskills.api.util.MapUtil;
import com.comze.sanman00.spongeskills.api.util.ObjectUtil;
import org.spongepowered.api.CatalogType;

public abstract class AbstractCatalogType implements CatalogType {
    protected final String id;
    protected final String name;

    protected AbstractCatalogType(String id) {
        this(id, id);
    }
    
    protected AbstractCatalogType(String id, String name) {
        ObjectUtil.expectThat(id != null && !id.isEmpty(), "Catalog type ID cannot be null!");
        this.id = id;
        this.name = (name == null || name.isEmpty()) ? id : name;
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
    public boolean equals(Object obj) {
        return ObjectUtil.equals(this, obj, t -> MapUtil.newMap(t.id, this.id, t.name, this.name));
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(3, 71, this.id, this.name);
    }

    @Override
    public String toString() {
        return getId();
    }
}
