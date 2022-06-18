package com.annabelle.potionflasks.lingeringflask;

import com.annabelle.potionflasks.ItemRegistry;
import com.annabelle.potionflasks.config.PotionFlasksCommonConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.level.Level;

public class LingeringFlaskItem extends LingeringPotionItem {
    public LingeringFlaskItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (!pLevel.isClientSide) {
            createThrownPotion(itemstack, pPlayer, pLevel);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            if(itemstack.getTag().getInt("potionflasks:fill_level") != 0){
                itemstack.getTag().putInt("potionflasks:fill_level",
                        itemstack.getTag().getInt("potionflasks:fill_level") - 1);
            } else {
                itemstack.getTag().putInt("potionflasks:fill_level", PotionFlasksCommonConfig.FLASK_MAX_FILL_LEVEL.get() - 1);
            }
            if(itemstack.getTag().getInt("potionflasks:fill_level") <= 0){
                itemstack = getEmptyItemStack();

            }
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    public void createThrownPotion(ItemStack itemStack, Player player, Level level){
        ThrownPotion thrownpotion = new ThrownPotion(level, player);
        // A bit of a hacky workaround, but it's what I'll have to do since lingering potions are hardcoded to be the only
        //  potions to create an effect cloud
        ItemStack lingeringPot = new ItemStack(Items.LINGERING_POTION);
        lingeringPot.setTag(new CompoundTag());
        lingeringPot.getTag().putString("Potion", itemStack.getTag().getString("Potion"));
        thrownpotion.setItem(lingeringPot);
        thrownpotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);

        level.addFreshEntity(thrownpotion);
    }

    public ItemStack getEmptyItemStack(){return new ItemStack(ItemRegistry.EMPTY_LINGERING_POTION_FLASK.get());}

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
