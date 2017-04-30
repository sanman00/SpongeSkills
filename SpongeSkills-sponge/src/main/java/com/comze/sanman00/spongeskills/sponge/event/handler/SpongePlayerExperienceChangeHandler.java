package com.comze.sanman00.spongeskills.sponge.event.handler;

import com.comze.sanman00.spongeskills.api.event.PlayerExperienceChangeEvent;
import java.util.function.Consumer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

public class SpongePlayerExperienceChangeHandler implements Consumer<PlayerExperienceChangeEvent> {
    @Override
    public void accept(PlayerExperienceChangeEvent e) {
        ((Player) e.getPlayer().getWrappedPlayer()).sendMessage(ChatTypes.ACTION_BAR, Text.of(TextColors.YELLOW, "You got" + e.getDifference() + " experience!")); //test
    }
}
