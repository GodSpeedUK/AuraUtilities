package tech.aurasoftware.aurautilities.command.parameter.implementation;

import tech.aurasoftware.aurautilities.command.parameter.Parameter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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


}
