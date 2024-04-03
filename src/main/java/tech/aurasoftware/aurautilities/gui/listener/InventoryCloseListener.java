package tech.aurasoftware.aurautilities.gui.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import tech.aurasoftware.aurautilities.gui.AuraGUIUtility;
public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {

        if (!(e.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getPlayer();

        AuraGUIUtility.removeGUI(player);
    }

}
