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
import tech.aurasoftware.aurautilities.util.Util;


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
    private boolean requiresPlayer = false;
    private Class<?>[] parameters = new Class<?>[0];
    private boolean[] optional = new boolean[0];

    public AuraCommand(String name) {
        super(name);

        this.auraSubCommands = new ArrayList<>();
        Constructor<?> constructor;
        try {
            constructor = this.getClass().getConstructors()[0];
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

        if (constructor.isAnnotationPresent(RequiresPlayer.class)) {
            this.requiresPlayer = true;
        }

        if (constructor.isAnnotationPresent(Parameters.class)) {
            Parameters parameters = constructor.getAnnotation(Parameters.class);
            this.parameters = parameters.value();
            this.optional = parameters.optional();
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

        Placeholder namePlaceholder = new Placeholder("{name}", this.getName());

        String usageString = Placeholder.apply(usage, namePlaceholder);

        String permissionString = null;

        if(auraCommandFrame.getPermission() != null){
            permissionString = Placeholder.apply(permission, namePlaceholder);
        }

        Method runMethod;

        try {
            runMethod = auraCommandFrame.getClass().getDeclaredMethod("run", CommandSender.class, String[].class);
            runMethod.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (permissionString != null && !commandSender.hasPermission(permissionString)) {
            UtilityMessages.NO_PERMISSION.send(commandSender);
            return true;
        }

        if (this.requiresPlayer && !(commandSender instanceof Player)) {
            UtilityMessages.INVALID_SENDER.send(commandSender);
            return true;
        }

        for (int i = 0; i < parameters.length; i++) {

            boolean optional = this.optional[i];
            Class<?> parameterClass = parameters[i];

            if (strings.length <= i && !optional) {
                UtilityMessages.INVALID_USAGE.send(commandSender, new Placeholder("{usage}", usageString));
                return true;
            }

            if (strings.length <= i) {
                continue;
            }

            String arg = strings[i];

            Parameter<?> parameter = AuraUtilities.getInstance().getParameterManager().get(parameterClass);

            if(!parameter.isParsable(arg)){
                UtilityMessages.INVALID_USAGE.send(commandSender, new Placeholder("{usage}", usageString));
                return true;
            }

        }


        return auraCommandFrame.run(commandSender, strings);
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {

        List<String> completions = new ArrayList<>();

        AuraCommandFrame completeFrom = this;
        int indexShift = 0;

        if (args.length > 0) {
            if (!getAuraSubCommands().isEmpty()) {
                if (args.length == 1) {
                    for (AuraSubCommand auraSubCommand : getAuraSubCommands()) {
                        completions.add(auraSubCommand.getName());
                        if(auraSubCommand.getAliases() != null){
                            completions.addAll(auraSubCommand.getAliases());
                        }
                    }
                    return completions;
                }
                for (AuraSubCommand auraSubCommand : getAuraSubCommands()) {
                    if (auraSubCommand.getName().equalsIgnoreCase(args[0]) || auraSubCommand.getAliases().contains(args[0])) {
                        completeFrom = auraSubCommand;
                        indexShift = 1;
                        break;
                    }
                }
            }
        }

        int index = args.length - indexShift -1;

        if (completeFrom.getParameters().length <= index) {
            return super.tabComplete(sender, alias, args);
        }

        Class<?> parameterClass = completeFrom.getParameters()[index];

        Parameter<?> parameter = AuraUtilities.getInstance().getParameterManager().get(parameterClass);

        if (parameter == null) {
            return super.tabComplete(sender, alias, args);
        }


        return parameter.tabComplete();
    }
}


