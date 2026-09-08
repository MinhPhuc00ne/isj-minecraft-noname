package com.yourname.darkgathering.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class SewingNeedleItem extends Item {
    public SewingNeedleItem(Properties properties) {
        super(properties.stacksTo(1).durability(64).rarity(Rarity.COMMON));
    }
}
