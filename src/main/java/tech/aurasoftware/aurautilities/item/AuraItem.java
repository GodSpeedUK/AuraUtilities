package tech.aurasoftware.aurautilities.item;

import lombok.Builder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.aurasoftware.aurautilities.configuration.serialization.Serializable;
import tech.aurasoftware.aurautilities.util.Placeholder;
import tech.aurasoftware.aurautilities.util.Text;

import java.util.ArrayList;
import java.util.List;


public class AuraItem implements Serializable {

    private String material;
    private String name;
    private List<String> lore;
    private int amount = 1;
    private int data = 0;
    private boolean unbreakable = false;
    private boolean hideEnchants = false;
    private boolean hideAttributes = false;
    private List<String> enchantments;

    public AuraItem(){

    }

    public AuraItem material(String material){
        this.material = material;
        return this;
    }

    public AuraItem name(String name){
        this.name = name;
        return this;
    }

    public AuraItem lore(List<String> lore){
        this.lore = lore;
        return this;
    }

    public AuraItem amount(int amount){
        this.amount = amount;
        return this;
    }

    public AuraItem data(int data){
        this.data = data;
        return this;
    }

    public AuraItem unbreakable(boolean unbreakable){
        this.unbreakable = unbreakable;
        return this;
    }

    public AuraItem hideEnchants(boolean hideEnchants){
        this.hideEnchants = hideEnchants;
        return this;
    }

    public AuraItem hideAttributes(boolean hideAttributes){
        this.hideAttributes = hideAttributes;
        return this;
    }

    public AuraItem enchantments(List<String> enchantments){
        this.enchantments = enchantments;
        return this;
    }


    public ItemStack toBukkitItem(Placeholder... placeholders){

        if(material == null){
            throw new IllegalArgumentException("Material cannot be null");
        }

        Material bukkitMaterial = Material.getMaterial(material.toUpperCase());

        if(bukkitMaterial == null){
            throw new IllegalArgumentException("Material not found");
        }

        ItemStack itemStack = new ItemStack(bukkitMaterial, amount, (short) data);

        ItemMeta itemMeta = itemStack.getItemMeta();

        if(name != null){
            itemMeta.setDisplayName(Text.c(Placeholder.apply(name, placeholders)));
        }

        if(lore != null){
            List<String> loreModified = new ArrayList<>();
            for(String loreLine : lore){
                loreModified.add(Text.c(Placeholder.apply(loreLine, placeholders)));
            }
            itemMeta.setLore(loreModified);
        }

        itemMeta.setUnbreakable(unbreakable);

        if(enchantments != null){
            for(String enchantment : enchantments){
                String[] enchantmentSplit = enchantment.split(":");
                if(enchantmentSplit.length != 2){
                    throw new IllegalArgumentException("Invalid enchantment format");
                }

                int level;

                try{
                    level = Integer.parseInt(enchantmentSplit[1]);
                }catch (NumberFormatException e){
                    throw new IllegalArgumentException("Invalid enchantment level");
                }

                Enchantment bukkitEnchantment = Enchantment.getByName(enchantmentSplit[0].toUpperCase());

                if(bukkitEnchantment == null){
                    throw new IllegalArgumentException("Enchantment not found");
                }

                itemMeta.addEnchant(bukkitEnchantment, level, true);

            }
        }

        if(hideEnchants){
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if(hideAttributes){
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }


        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }


}
