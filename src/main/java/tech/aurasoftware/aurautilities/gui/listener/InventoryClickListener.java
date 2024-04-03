package tech.aurasoftware.aurautilities.gui.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import tech.aurasoftware.aurautilities.gui.AuraGUIItem;
import tech.aurasoftware.aurautilities.gui.AuraGUIUtility;
import tech.aurasoftware.aurautilities.gui.OpenedAuraGUI;
import tech.aurasoftware.aurautilities.gui.event.AuraGUIClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        if(!(e.getWhoClicked() instanceof Player)){
            return;
        }

        Player player = (Player) e.getWhoClicked();

        OpenedAuraGUI openedAuraGUI = AuraGUIUtility.getOpenedGUI(player);

        if(openedAuraGUI == null){
            return;
        }

        AuraGUIItem auraGUIItem = openedAuraGUI.getAuraGUIItem(e.getSlot());

        if(auraGUIItem == null){
            return;
        }

        AuraGUIClickEvent auraGUIClickEvent = new AuraGUIClickEvent(openedAuraGUI.getAuraGUI().getId(), auraGUIItem.getKey(), player);

        Bukkit.getServer().getPluginManager().callEvent(auraGUIClickEvent);

        e.setCancelled(auraGUIClickEvent.isCancelled());
    }

}
