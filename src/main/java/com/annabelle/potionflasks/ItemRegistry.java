package com.annabelle.potionflasks;

import com.annabelle.potionflasks.lingeringflask.LingeringFlaskItem;
import com.annabelle.potionflasks.potionflask.PotionFlaskItem;
import com.annabelle.potionflasks.regeneratingflask.RegeneratingPotionFlaskItem;
import com.annabelle.potionflasks.splashingflask.SplashPotionFlaskItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PotionFlasks.MOD_ID);

    public static final RegistryObject<Item> POTION_FLASK = ITEMS.register("potion_flask",
            () -> new PotionFlaskItem(new Item.Properties()
                    .tab(CreativeModeTab.TAB_BREWING)
                    .stacksTo(1))
    );
    public static final RegistryObject<Item> SPLASH_POTION_FLASK = ITEMS.register("splash_potion_flask",
            () -> new SplashPotionFlaskItem(new Item.Properties()
                    .tab(CreativeModeTab.TAB_BREWING)
                    .stacksTo(1))
    );
    public static final RegistryObject<Item> LINGERING_POTION_FLASK = ITEMS.register("lingering_potion_flask",
            () -> new LingeringFlaskItem(new Item.Properties()
                    .tab(CreativeModeTab.TAB_BREWING)
                    .stacksTo(1))
    );

    public static final RegistryObject<Item> EMPTY_POTION_FLASK = ITEMS.register("empty_potion_flask",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
                    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
                    if(!Screen.hasShiftDown()){return;}
                    pTooltipComponents.add(new TranslatableComponent("tooltip.potionflasks.empty_potion_flask").withStyle(ChatFormatting.GRAY));
                }
            });
    public static final RegistryObject<Item> EMPTY_SPLASH_POTION_FLASK = ITEMS.register("empty_splash_potion_flask",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
                    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
                    if(!Screen.hasShiftDown()){return;}
                    pTooltipComponents.add(new TranslatableComponent("tooltip.potionflasks.empty_potion_flask").withStyle(ChatFormatting.GRAY));
                }
            });
    public static final RegistryObject<Item> EMPTY_LINGERING_POTION_FLASK = ITEMS.register("empty_lingering_potion_flask",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
                    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
                    if(!Screen.hasShiftDown()){return;}
                    pTooltipComponents.add(new TranslatableComponent("tooltip.potionflasks.empty_potion_flask").withStyle(ChatFormatting.GRAY));
                }
            });

    public static final RegistryObject<Item> REGENERATING_POTION_FLASK = ITEMS.register("regenerating_potion_flask",
            () -> new RegeneratingPotionFlaskItem(new Item.Properties()
                    .tab(CreativeModeTab.TAB_BREWING)
                    .stacksTo(1))
    );




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
