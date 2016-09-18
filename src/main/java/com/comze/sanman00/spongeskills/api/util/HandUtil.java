package com.comze.sanman00.spongeskills.api.util;

import java.util.Optional;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

public final class HandUtil {
    private HandUtil() {
        
    }
    
    public static <T> Optional<ItemStack> getItemFromHand(Player p) {
        return Optional.ofNullable(p.getItemInHand(HandTypes.MAIN_HAND).orElse(p.getItemInHand(HandTypes.OFF_HAND).orElse(null)));
    }
}
