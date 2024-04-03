package tech.aurasoftware.aurautilities.gui;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.aurasoftware.aurautilities.configuration.serialization.Serializable;
import tech.aurasoftware.aurautilities.item.AuraItem;

@NoArgsConstructor
@Getter
public class AuraGUIItem implements Serializable {

    private AuraItem auraItem;
    private int slot;
    private String key;

    public AuraGUIItem auraItem(AuraItem auraItem){
        this.auraItem = auraItem;
        return this;
    }

    public AuraGUIItem slot(int slot){
        this.slot = slot;
        return this;
    }

    public AuraGUIItem key(String key){
        this.key = key;
        return this;
    }

}
