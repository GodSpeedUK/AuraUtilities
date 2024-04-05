package tech.aurasoftware.aurautilities.command.parameter.implementation;

import tech.aurasoftware.aurautilities.command.parameter.Parameter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerParameter extends Parameter<Player> {

    public PlayerParameter() {
        super(Player.class);
    }
    @Override
    public Player parse(String input) {

        return Bukkit.getPlayer(input);
    }

    @Override
    public List<String> tabComplete() {
        List<String> list = new ArrayList<>();
        for(Player offlinePlayer: Bukkit.getOnlinePlayers()){
            list.add(offlinePlayer.getName());
        }
        return list;
    }


}
