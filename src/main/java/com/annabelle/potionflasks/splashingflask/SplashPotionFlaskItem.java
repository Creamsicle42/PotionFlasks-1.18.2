package com.annabelle.potionflasks.splashingflask;

import com.annabelle.potionflasks.config.PotionFlasksCommonConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SplashPotionFlaskItem extends Item {
    public SplashPotionFlaskItem(Properties pProperties) {
        super(pProperties);
    }

    public static int getMaxFillLevel(){return PotionFlasksCommonConfig.FLASK_MAX_FILL_LEVEL.get();}

    public boolean isBarVisible(ItemStack pStack) {
        return pStack.getTag().getInt("potionflasks:fill_level") != 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return (int)(((float)pStack.getTag().getInt("potionflasks:fill_level")/PotionFlasksCommonConfig.FLASK_MAX_FILL_LEVEL.get()) * 13);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        float stackMaxDamage = this.getMaxDamage(pStack);
        float f = Math.max(0.0F, (float)pStack.getTag().getInt("potionflasks:fill_level") / (float)PotionFlasksCommonConfig.FLASK_MAX_FILL_LEVEL.get());
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

}
