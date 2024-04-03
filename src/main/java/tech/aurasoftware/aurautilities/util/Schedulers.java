package tech.aurasoftware.aurautilities.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import tech.aurasoftware.aurautilities.AuraUtilities;

@UtilityClass
public class Schedulers {

    public int async(Runnable runnable){
        return Bukkit.getScheduler().runTaskAsynchronously(AuraUtilities.getInstance(), runnable).getTaskId();
    }

    public int sync(Runnable runnable){
        return Bukkit.getScheduler().runTask(AuraUtilities.getInstance(), runnable).getTaskId();
    }

    public int later(Runnable runnable, long delay){
        return Bukkit.getScheduler().runTaskLater(AuraUtilities.getInstance(), runnable, delay).getTaskId();
    }

    public int repeat(Runnable runnable, long delay){
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(AuraUtilities.getInstance(), runnable, delay, delay);
    }


}
