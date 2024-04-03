package tech.aurasoftware.aurautilities.gui;

import lombok.Getter;
import tech.aurasoftware.aurautilities.item.AuraItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class OpenedAuraGUI {

    private final AuraGUI auraGUI;

    public OpenedAuraGUI(AuraGUI auraGUI) {
        this.auraGUI = auraGUI;
        this.additionalItems = new ArrayList<>();
    }

    private final List<AuraGUIItem> additionalItems;

    public void addAdditionalItems(AuraGUIItem... auraItems){
        additionalItems.addAll(Arrays.asList(auraItems));
    }

    public AuraGUIItem getAuraGUIItem(int slot){

        for(AuraGUIItem auraGUIItem : additionalItems){
            if(auraGUIItem.getSlot() == slot){
                return auraGUIItem;
            }
        }

        for(AuraGUIItem auraGUIItem : auraGUI.getItems()){
            if(auraGUIItem.getSlot() == slot){
                return auraGUIItem;
            }
        }
        return null;
    }

}
