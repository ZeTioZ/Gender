package net.poweredbyhate.gender.events;

import net.poweredbyhate.gender.Gender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Lax on 12/8/2017.
 */
public class GenderChangeEvent extends Event {

    public static final HandlerList panHandlers = new HandlerList();
    private Player player;
    private Gender newGender;
    private Gender oldGender;

    public GenderChangeEvent(Player player, Gender oldGender, Gender newGender) {
        this.player = player;
        this.oldGender = oldGender;
        this.newGender = newGender;
    }

    public static HandlerList getHandlerList() {
        return panHandlers;
    }

    @Override
    public HandlerList getHandlers() {
        return panHandlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Gender getNewGender() {
        return newGender;
    }

    public Gender getOldGender() {
        return oldGender;
    }


}
