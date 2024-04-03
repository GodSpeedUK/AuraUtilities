package tech.aurasoftware.aurautilities.gui;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import tech.aurasoftware.aurautilities.configuration.serialization.Serializable;
import tech.aurasoftware.aurautilities.util.Text;

import java.util.List;

@Builder
@Getter
public class AuraGUI implements Serializable, Cloneable {

    private String id;
    private String name;
    private int size;
    private List<AuraGUIItem> items;


    @Override
    public AuraGUI clone() {
        return AuraGUI.builder()
                .name(name)
                .size(size)
                .items(items)
                .build();
    }

    public Inventory createInventory(AuraGUIItem... additionalItems){

        Inventory inventory = Bukkit.createInventory(null, size, Text.c(name));

        for(AuraGUIItem auraGUIItem : items){
            inventory.setItem(auraGUIItem.getSlot(), auraGUIItem.getAuraItem().toBukkitItem());
        }

        for(AuraGUIItem auraGUIItem : additionalItems){
            inventory.setItem(auraGUIItem.getSlot(), auraGUIItem.getAuraItem().toBukkitItem());
        }

        return inventory;
    }

}
