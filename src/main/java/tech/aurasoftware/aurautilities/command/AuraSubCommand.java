package tech.aurasoftware.aurautilities.command;

import lombok.Getter;
import lombok.Setter;
import tech.aurasoftware.aurautilities.command.annotation.Alias;
import tech.aurasoftware.aurautilities.command.annotation.Permission;
import tech.aurasoftware.aurautilities.command.annotation.Usage;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class AuraSubCommand implements AuraCommandFrame{
    private final String name;

    private String usage;

    private String permission;

    private List<String> aliases;



    public AuraSubCommand(String name) {
        Constructor<?> constructor;
        this.name = name;
        try {
            constructor = getClass().getConstructor();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (constructor.isAnnotationPresent(Alias.class)) {
            Alias alias = constructor.getAnnotation(Alias.class);
            setAliases(Arrays.asList(alias.value()));
        }
        if (constructor.isAnnotationPresent(Usage.class)) {
            Usage usage = constructor.getAnnotation(Usage.class);
            setUsage(usage.value());
        }
        if (constructor.isAnnotationPresent(Permission.class)) {
            Permission permission = constructor.getAnnotation(Permission.class);
            setPermission(permission.value());
        }
    }
}
