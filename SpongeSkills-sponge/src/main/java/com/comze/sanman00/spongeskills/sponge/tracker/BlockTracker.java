package com.comze.sanman00.spongeskills.sponge.tracker;

import com.comze.sanman00.spongeskills.sponge.Main;
import com.comze.sanman00.spongeskills.sponge.data.PluginKeys;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

/**
 * A class that represents a block tracker. A block tracker is an object that
 * tracks which blocks are modified by which players in a certain world.
 */
public final class BlockTracker {
    private final Map<UUID, Set<Location<?>>> blockOwnerMap = new HashMap<>();
    private final Extent extent;

    public BlockTracker(Extent extent) {
        this.extent = extent;
    }

    public void addBlock(Location<?> loc, Player player) {
        this.addBlock(loc, player.getUniqueId());
    }

    public void addBlock(Location<?> loc, UUID uuid) {
        if (this.extent.containsBlock(loc.getBlockPosition())) {
            Set<Location<?>> set = this.blockOwnerMap.getOrDefault(uuid, new HashSet<>());
            if (!set.contains(loc)) {
                loc.offer(PluginKeys.OWNED_BLOCK_MARKER, true); //mark the block for persistence
                set.add(loc);
            }
            else {
                Main.INSTANCE.getLogger().debug("Location already being tracked!");
            }
        }
    }

    public boolean isBeingTracked(Location<?> loc, Player player) {
        return isBeingTracked(loc, player.getUniqueId());
    }

    public boolean isBeingTracked(Location<?> loc, UUID uuid) {
        return this.extent.containsBlock(loc.getBlockPosition()) && this.blockOwnerMap.containsKey(uuid) && this.blockOwnerMap.get(uuid).contains(loc);
    }

    public void removeBlock(Location<?> loc, Player player) {
        this.removeBlock(loc, player.getUniqueId());
    }

    public void removeBlock(Location<?> loc, UUID uuid) {
        if (this.extent.containsBlock(loc.getBlockPosition())) {
            Set<Location<?>> set = this.blockOwnerMap.get(uuid);
            if (set != null) {
                if (!set.remove(loc)) {
                    Main.INSTANCE.getLogger().debug("Location " + loc + " not being tracked!");
                }
            }

            else {
                Main.INSTANCE.getLogger().debug("Player UUID " + uuid + "not in map");
            }
        }
    }

    public Extent getExtent() {
        return this.extent;
    }

    public Map<UUID, Set<Location<?>>> getBlockOwnerMap() {
        return ImmutableMap.copyOf(this.blockOwnerMap);
    }
}
