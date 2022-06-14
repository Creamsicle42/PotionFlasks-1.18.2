package com.annabelle.potionflasks.splashingflask;

import com.annabelle.potionflasks.ItemRegistry;
import com.annabelle.potionflasks.config.PotionFlasksCommonConfig;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;

public class SplashPotionFlaskItem extends SplashPotionItem {
    public SplashPotionFlaskItem(Properties pProperties) {
        super(pProperties);
    }

    public static int getMaxFillLevel(){return PotionFlasksCommonConfig.FLASK_MAX_FILL_LEVEL.get();}

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (!pLevel.isClientSide) {
            ThrownPotion thrownpotion = new ThrownPotion(pLevel, pPlayer);
            thrownpotion.setItem(itemstack);
            thrownpotion.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), -20.0F, 0.5F, 1.0F);
            pLevel.addFreshEntity(thrownpotion);
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
                itemstack = new ItemStack(ItemRegistry.EMPTY_SPLASH_POTION_FLASK.get());

            }
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pStack.getTag().getInt("potionflasks:fill_level") < 0){
            //pEntity.setItemSlot(getEquipmentSlot(pStack) ,new ItemStack(ItemRegistry.EMPTY_SPLASH_POTION_FLASK.get()));
        }
    }

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
