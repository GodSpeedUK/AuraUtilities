package tech.aurasoftware.aurautilities.gui;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.aurasoftware.aurautilities.configuration.serialization.Serializable;
import tech.aurasoftware.aurautilities.item.AuraItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Getter
public class AuraGUIItem implements Serializable {

    private AuraItem auraItem;
    private List<Integer> slots = new ArrayList<>();
    private String key;

    public AuraGUIItem auraItem(AuraItem auraItem){
        this.auraItem = auraItem;
        return this;
    }

    public AuraGUIItem slot(int... slots){
        this.slots = new ArrayList<>();
        for(int slot: slots){
            this.slots.add(slot);
        }
        return this;
    }

    public AuraGUIItem key(String key){
        this.key = key;
        return this;
    }

}
