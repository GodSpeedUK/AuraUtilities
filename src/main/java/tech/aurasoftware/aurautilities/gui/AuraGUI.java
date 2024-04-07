package tech.aurasoftware.aurautilities.gui;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import tech.aurasoftware.aurautilities.configuration.serialization.Serializable;
import tech.aurasoftware.aurautilities.util.Placeholder;
import tech.aurasoftware.aurautilities.util.Text;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AuraGUI implements Serializable, Cloneable {

    private String id;
    private String name;
    private int size;
    private List<AuraGUIItem> items = new ArrayList<>();

    public AuraGUI id(String id){
        this.id = id;
        return this;
    }

    public AuraGUI name(String name){
        this.name = name;
        return this;
    }

    public AuraGUI size(int size){
        this.size = size;
        return this;
    }

    public AuraGUI items(List<AuraGUIItem> items){
        this.items = items;
        return this;
    }



    @Override
    public AuraGUI clone() {
        return new AuraGUI()
                .id(id)
                .name(name)
                .size(size)
                .items(items);
    }

    public Inventory createInventory(List<Placeholder> placeholderList, AuraGUIItem... additionalItems){

        Placeholder[] placeholders = placeholderList.toArray(new Placeholder[0]);

        Inventory inventory = Bukkit.createInventory(null, size, Text.c(Placeholder.apply(name, placeholders)));

        for(AuraGUIItem auraGUIItem : items){
            for(int slot: auraGUIItem.getSlots()){
                inventory.setItem(slot, auraGUIItem.getAuraItem().toBukkitItem(placeholders));
            }

        }

        for(AuraGUIItem auraGUIItem : additionalItems){
            for (int slot : auraGUIItem.getSlots()) {
                inventory.setItem(slot, auraGUIItem.getAuraItem().toBukkitItem(placeholders));
            }
        }

        return inventory;
    }

}
