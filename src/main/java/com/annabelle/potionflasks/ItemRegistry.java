package com.annabelle.potionflasks;

import com.annabelle.potionflasks.potionflask.PotionFlaskItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PotionFlasks.MOD_ID);

    public static final RegistryObject<Item> POTION_FLASK = ITEMS.register("potion_flask",
            () -> new PotionFlaskItem(new Item.Properties()
                    .tab(CreativeModeTab.TAB_BREWING)
                    .stacksTo(1))
    );

    public static final RegistryObject<Item> EMPTY_POTION_FLASK = ITEMS.register("empty_potion_flask",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
