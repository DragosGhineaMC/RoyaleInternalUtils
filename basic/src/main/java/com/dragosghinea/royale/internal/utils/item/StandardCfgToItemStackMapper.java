package com.dragosghinea.royale.internal.utils.item;

import com.cryptomorin.xseries.XMaterial;
import com.dragosghinea.royale.internal.utils.material.MaterialStringProcessor;
import com.dragosghinea.royale.internal.utils.material.StandardMaterialStringProcessor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StandardCfgToItemStackMapper implements CfgToItemStackMapper {
    private final MaterialStringProcessor materialStringProcessor;

    public StandardCfgToItemStackMapper() {
        this.materialStringProcessor = new StandardMaterialStringProcessor();
    }

    public StandardCfgToItemStackMapper(MaterialStringProcessor materialStringProcessor) {
        this.materialStringProcessor = materialStringProcessor;
    }

    @Override
    public ItemStack mapItemStack(ItemStackCfg itemStackCfg) {
        XMaterial xMaterial = materialStringProcessor.processXMaterial(itemStackCfg.getMaterial());
        ItemStack itemStack = xMaterial.parseItem();
        if (itemStack == null) {
            throw new RuntimeException("Invalid material: " + itemStackCfg.getMaterial());
        }

        itemStack.setAmount(itemStackCfg.getAmount());

        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            if (itemStackCfg.getName() != null) {
                itemMeta.setDisplayName(itemStackCfg.getName());
            }
            if (itemStackCfg.getLore() != null) {
                itemMeta.setLore(itemStackCfg.getLore());
            }
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }
}
