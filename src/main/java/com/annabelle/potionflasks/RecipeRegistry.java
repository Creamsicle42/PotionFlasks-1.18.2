package com.annabelle.potionflasks;

import com.annabelle.potionflasks.lingeringflask.TippedArrowFromFlaskRecipe;
import com.annabelle.potionflasks.potionfilling.PotionFillingRecipe;
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
            SERIALIZERS.register("fill_potion_flask", () -> PotionFlaskRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<PotionFillingRecipe>> POTION_FILLING_RECIPE =
            SERIALIZERS.register("fill_from_potion_flask", () -> PotionFillingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<TippedArrowFromFlaskRecipe>> TIPPED_ARROW_FROM_FLASK_RECIPE =
            SERIALIZERS.register("tipped_arrow_from_flask_recipe", () -> TippedArrowFromFlaskRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);

    }
}
