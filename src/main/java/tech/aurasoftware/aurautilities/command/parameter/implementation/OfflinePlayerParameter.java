package tech.aurasoftware.aurautilities.command.parameter.implementation;

import org.bukkit.entity.Player;
import tech.aurasoftware.aurautilities.command.parameter.Parameter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class OfflinePlayerParameter extends Parameter<OfflinePlayer> {

    public OfflinePlayerParameter() {
        super(OfflinePlayer.class);
    }


    @Override
    public OfflinePlayer parse(String input) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(input);
        return offlinePlayer;
    }

    @Override
    public boolean isParsable(String input){
        if(input == null){
            return false;
        }
        return parse(input).hasPlayedBefore();
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
