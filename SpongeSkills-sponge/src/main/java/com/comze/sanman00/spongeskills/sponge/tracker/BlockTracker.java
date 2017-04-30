package com.comze.sanman00.spongeskills.sponge.tracker;

import com.comze.sanman00.spongeskills.sponge.Main;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

/**
 * A class that represents a block tracker. A block tracker is an object that
 * tracks which blocks are modified by which players in a certain world.
 */
public final class BlockTracker {
    private final Map<UUID, Set<Vector3i>> blockOwnerMap = new HashMap<>();
    private final UUID worldID;

    public BlockTracker(World world) {
        this(world.getUniqueId());
    }

    public BlockTracker(UUID worldID) {
        Sponge.getServer().getWorld(worldID).orElseThrow(() -> new IllegalStateException("World ID must be valid!"));
        this.worldID = worldID;
    }

    public void addBlock(Vector3i loc, Player player) {
        this.addBlock(loc, player.getUniqueId());
    }

    public void addBlock(Vector3i loc, UUID uuid) {
        if (this.getWorld().containsBlock(loc)) {
            if (!this.blockOwnerMap.containsKey(uuid)) {
                this.blockOwnerMap.put(uuid, new HashSet<>());
            }
            Set<Vector3i> set = this.blockOwnerMap.get(uuid);
            if (!set.contains(loc)) {
                set.add(loc);
            }
            else {
                Main.INSTANCE.getLogger().debug("Location already being tracked!");
            }
        }
    }

    public boolean isBeingTracked(Vector3i loc, Player player) {
        return isBeingTracked(loc, player.getUniqueId());
    }

    public boolean isBeingTracked(Vector3i loc, UUID uuid) {
        return this.getWorld().containsBlock(loc) && this.blockOwnerMap.containsKey(uuid) && this.blockOwnerMap.get(uuid).contains(loc);
    }

    public void removeBlock(Vector3i loc, Player player) {
        this.removeBlock(loc, player.getUniqueId());
    }

    public void removeBlock(Vector3i loc, UUID uuid) {
        if (this.getWorld().containsBlock(loc)) {
            Set<Vector3i> set = this.blockOwnerMap.get(uuid);
            if (set != null) {
                if (!set.remove(loc)) {
                    Main.INSTANCE.getLogger().debug("Location " + loc + " not being tracked");
                }
            }

            else {
                Main.INSTANCE.getLogger().debug("Player UUID " + uuid + " not in map");
            }
        }
    }

    public World getWorld() {
        return Sponge.getServer().getWorld(this.worldID).get();
    }

    public Map<UUID, Set<Vector3i>> getBlockOwnerMap() {
        return ImmutableMap.copyOf(this.blockOwnerMap);
    }
}
