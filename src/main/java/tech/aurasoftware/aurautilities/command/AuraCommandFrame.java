package tech.aurasoftware.aurautilities.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import tech.aurasoftware.aurautilities.command.annotation.Alias;
import tech.aurasoftware.aurautilities.command.annotation.Usage;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;


public interface AuraCommandFrame {

    Class<?>[] getParameters();

    String getPermission();

    String getUsage();

    boolean isRequiresPlayer();

    boolean[] getOptional();

    boolean run(CommandSender commandSender, String[] args);


}
