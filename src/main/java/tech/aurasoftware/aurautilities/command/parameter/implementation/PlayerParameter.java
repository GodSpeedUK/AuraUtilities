package tech.aurasoftware.aurautilities.command.parameter.implementation;

import tech.aurasoftware.aurautilities.command.parameter.Parameter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerParameter extends Parameter<Player> {

    public PlayerParameter() {
        super(Player.class);
    }
    @Override
    public Player parse(String input) {

        return Bukkit.getPlayer(input);
    }


}
