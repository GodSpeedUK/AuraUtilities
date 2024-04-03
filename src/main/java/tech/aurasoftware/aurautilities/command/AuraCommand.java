package tech.aurasoftware.aurautilities.command;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tech.aurasoftware.aurautilities.AuraUtilities;
import tech.aurasoftware.aurautilities.command.annotation.*;
import tech.aurasoftware.aurautilities.command.parameter.Parameter;
import tech.aurasoftware.aurautilities.message.UtilityMessages;
import tech.aurasoftware.aurautilities.util.Placeholder;


import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class AuraCommand extends Command implements AuraCommandFrame {


    private final List<AuraSubCommand> auraSubCommands;

    private String usage;
    private String permission;

    public AuraCommand(String name) {
        super(name);

        this.auraSubCommands = new ArrayList<>();
        Constructor<?> constructor;
        try {
            constructor = this.getClass().getConstructor();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (constructor.isAnnotationPresent(Alias.class)) {
            Alias alias = constructor.getAnnotation(Alias.class);
            this.setAliases(Arrays.asList(alias.value()));
        }
        if (constructor.isAnnotationPresent(Usage.class)) {
            Usage usage = constructor.getAnnotation(Usage.class);
            this.usage = usage.value();
        }

        if (constructor.isAnnotationPresent(Permission.class)) {
            Permission permission = constructor.getAnnotation(Permission.class);
            this.permission = permission.value();
        }

    }

    public void addSubCommands(AuraSubCommand... subCommands) {
        auraSubCommands.addAll(Arrays.asList(subCommands));
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length > 0) {
            if (!this.getAuraSubCommands().isEmpty()) {
                for (AuraSubCommand auraSubCommand : this.getAuraSubCommands()) {
                    if (auraSubCommand.getName().equalsIgnoreCase(strings[0]) || auraSubCommand.getAliases().contains(strings[0])) {

                        String[] passThroughArray;

                        if (strings.length == 1) {
                            passThroughArray = new String[0];
                        } else {
                            passThroughArray = Arrays.copyOfRange(strings, 1, strings.length);
                        }

                        return processAnnotations(auraSubCommand, commandSender, passThroughArray);
                    }
                }

            }
        }

        return processAnnotations(this, commandSender, strings);
    }

    public boolean processAnnotations(AuraCommandFrame auraCommandFrame, CommandSender commandSender, String[] strings) {
        Method runMethod;

        try {
            runMethod = auraCommandFrame.getClass().getDeclaredMethod("run", CommandSender.class, String[].class);
            runMethod.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (this.permission != null && !commandSender.hasPermission(this.permission)) {
            UtilityMessages.NO_PERMISSION.send(commandSender);
            return true;
        }

        if (runMethod.isAnnotationPresent(RequiresPlayer.class)) {
            if (!(commandSender instanceof Player)) {
                UtilityMessages.INVALID_SENDER.send(commandSender);
                return true;
            }
        }
        if (runMethod.isAnnotationPresent(Parameters.class)) {
            Parameters parameters = runMethod.getAnnotation(Parameters.class);
            int requiredLength = parameters.value().length;

            if (parameters.index().length != requiredLength || parameters.optional().length != requiredLength) {
                UtilityMessages.INVALID_USAGE.send(commandSender, new Placeholder("{usage}", this.getUsage()));
                return true;
            }

            for (int i = 0; i < requiredLength; i++) {
                int parameterIndex = parameters.index()[i];
                boolean optional = parameters.optional()[i];
                Class<?> parameterType = parameters.value()[i];

                String argument;

                try {
                    argument = strings[parameterIndex];
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (!optional) {
                        UtilityMessages.INVALID_USAGE.send(commandSender, new Placeholder("{usage}", this.getUsage()));
                        return true;
                    }
                    continue;
                }

                Parameter<?> parameter = AuraUtilities.getInstance().getParameterManager().get(parameterType);

                if (parameter == null || !parameter.isParsable(argument)) {
                    UtilityMessages.INVALID_USAGE.send(commandSender, new Placeholder("{usage}", this.getUsage()));
                    return true;
                }
            }
        }

        return auraCommandFrame.run(commandSender, strings);
    }

}


