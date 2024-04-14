package tech.aurasoftware.aurautilities;

import lombok.Getter;
import tech.aurasoftware.aurautilities.command.parameter.ParameterManager;
import tech.aurasoftware.aurautilities.command.parameter.implementation.*;
import tech.aurasoftware.aurautilities.configuration.Configuration;
import tech.aurasoftware.aurautilities.file.YamlFile;
import tech.aurasoftware.aurautilities.gui.AuraGUI;
import tech.aurasoftware.aurautilities.gui.AuraGUIItem;
import tech.aurasoftware.aurautilities.gui.listener.InventoryClickListener;
import tech.aurasoftware.aurautilities.gui.listener.InventoryCloseListener;
import tech.aurasoftware.aurautilities.item.AuraItem;
import tech.aurasoftware.aurautilities.main.AuraPlugin;
import tech.aurasoftware.aurautilities.message.TitleMessage;
import tech.aurasoftware.aurautilities.message.UtilityMessages;
import tech.aurasoftware.aurautilities.util.Schedulers;

@Getter
public final class AuraUtilities extends AuraPlugin {

    @Getter
    private static AuraUtilities instance;

    private ParameterManager parameterManager;

    @Override
    public void onEnable() {
        instance = this;
        this.parameterManager = new ParameterManager();
        this.registerParameters(
                new DoubleParameter(),
                new IntegerParameter(),
                new OfflinePlayerParameter(),
                new PlayerParameter(),
                new StringParameter()
        );


        registerSerializables(TitleMessage.class, AuraItem.class, AuraGUIItem.class, AuraGUI.class);
        registerListener(new InventoryClickListener(), new InventoryCloseListener());
        Configuration.loadConfig(new YamlFile("messages.yml", this.getDataFolder().getAbsolutePath(), null, this), UtilityMessages.values());
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
