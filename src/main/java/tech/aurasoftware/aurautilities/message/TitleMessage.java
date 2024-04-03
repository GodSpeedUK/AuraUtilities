package tech.aurasoftware.aurautilities.message;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.aurasoftware.aurautilities.configuration.serialization.Serializable;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class TitleMessage implements Serializable {

    private final String header;
    private final String footer;
    private int duration = 20;

}
