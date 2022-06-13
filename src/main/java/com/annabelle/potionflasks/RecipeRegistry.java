package com.annabelle.potionflasks;

import com.annabelle.potionflasks.potionflask.PotionFlaskRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PotionFlasks.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PotionFlaskRecipe>> POTION_FLASK_RECIPE =
            SERIALIZERS.register("fill_potion_flask",
                    PotionFlaskRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);

    }
}
