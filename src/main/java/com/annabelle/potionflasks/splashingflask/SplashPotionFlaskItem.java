package com.annabelle.potionflasks.splashingflask;

import com.annabelle.potionflasks.ItemRegistry;
import com.annabelle.potionflasks.config.PotionFlasksCommonConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

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
        thrownpotion.setItem(itemStack);
        thrownpotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
        level.addFreshEntity(thrownpotion);
    }

    public ItemStack getEmptyItemStack(){return new ItemStack(ItemRegistry.EMPTY_SPLASH_POTION_FLASK.get());}

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
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if(!Screen.hasShiftDown()){return;}
        pTooltipComponents.add(new TranslatableComponent("tooltip.potionflasks.potion_flask"));
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        float stackMaxDamage = this.getMaxDamage(pStack);
        float f = Math.max(0.0F, (float)pStack.getTag().getInt("potionflasks:fill_level") / (float)PotionFlasksCommonConfig.FLASK_MAX_FILL_LEVEL.get());
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }



}
