package tech.aurasoftware.aurautilities.message;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.aurasoftware.aurautilities.configuration.serialization.Serializable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TitleMessage implements Serializable {

    private String header;
    private String footer;
    private int duration = 20;

    public TitleMessage setHeader(String header) {
        this.header = header;
        return this;
    }

    public TitleMessage setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public TitleMessage setDuration(int duration) {
        this.duration = duration;
        return this;
    }

}
