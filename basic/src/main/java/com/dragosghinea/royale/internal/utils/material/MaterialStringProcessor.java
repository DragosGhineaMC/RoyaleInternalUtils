package com.dragosghinea.royale.internal.utils.material;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;

public interface MaterialStringProcessor {

    Material processMaterial(String materialString);

    XMaterial processXMaterial(String materialString);

}
