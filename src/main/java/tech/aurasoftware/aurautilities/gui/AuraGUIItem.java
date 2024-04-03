package tech.aurasoftware.aurautilities.gui;

import lombok.Builder;
import lombok.Getter;
import tech.aurasoftware.aurautilities.configuration.serialization.Serializable;
import tech.aurasoftware.aurautilities.item.AuraItem;

@Builder
@Getter
public class AuraGUIItem implements Serializable {

    private AuraItem auraItem;
    private int slot;
    private String key;

}
