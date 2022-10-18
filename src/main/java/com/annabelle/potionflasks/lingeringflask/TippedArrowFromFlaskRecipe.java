package com.annabelle.potionflasks.lingeringflask;

import com.annabelle.potionflasks.ItemRegistry;
import com.annabelle.potionflasks.PotionFlasks;
import com.annabelle.potionflasks.potionfilling.PotionFillingRecipe;
import com.annabelle.potionflasks.potionflask.PotionFlaskRecipe;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class TippedArrowFromFlaskRecipe implements CraftingRecipe {
    private final ResourceLocation id;

    public TippedArrowFromFlaskRecipe(ResourceLocation id){
        this.id = id;
    }


    public boolean matches(CraftingContainer pInv, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }
        if (pInv.getWidth() == 3 && pInv.getHeight() == 3) {
            for(int i = 0; i < pInv.getWidth(); ++i) {
                for(int j = 0; j < pInv.getHeight(); ++j) {
                    ItemStack itemstack = pInv.getItem(i + j * pInv.getWidth());
                    if (itemstack.isEmpty()) {
                        return false;
                    }

                    if (i == 1 && j == 1) {
                        if (!itemstack.is(ItemRegistry.LINGERING_POTION_FLASK.get())) {
                            return false;
                        }
                    } else if (!itemstack.is(Items.ARROW)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(CraftingContainer pInv) {
        ItemStack itemstack = pInv.getItem(1 + pInv.getWidth());
        if (!itemstack.is(ItemRegistry.LINGERING_POTION_FLASK.get())) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = new ItemStack(Items.TIPPED_ARROW, 8);
            PotionUtils.setPotion(itemstack1, PotionUtils.getPotion(itemstack));
            PotionUtils.setCustomEffects(itemstack1, PotionUtils.getCustomEffects(itemstack));
            return itemstack1;
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer pContainer) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(pContainer.getContainerSize(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack item = pContainer.getItem(i);
            if (item.getItem() == ItemRegistry.LINGERING_POTION_FLASK.get()) {
                if(item.getTag().getInt("potionflasks:fill_level") == 1) {
                    nonnulllist.set(i, new ItemStack(ItemRegistry.EMPTY_LINGERING_POTION_FLASK.get()));
                }else{
                    ItemStack flask = new ItemStack(ItemRegistry.LINGERING_POTION_FLASK.get());
                    flask.setTag(item.getTag());
                    flask.getTag().putInt("potionflasks:fill_level", flask.getTag().getInt("potionflasks:fill_level") - 1);
                    nonnulllist.set(i, flask);
                }
            }
        }

        return nonnulllist;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= 2 && pHeight >= 2;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(ItemRegistry.LINGERING_POTION_FLASK.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<TippedArrowFromFlaskRecipe> getSerializer() {

        return Serializer.INSTANCE;
    }
    public static class Type implements RecipeType<TippedArrowFromFlaskRecipe> {
        private Type() { }
        public static final TippedArrowFromFlaskRecipe.Type INSTANCE = new TippedArrowFromFlaskRecipe.Type();
        public static final String ID = "tipped_arrow_from_flask_recipe";
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    public static class Serializer implements RecipeSerializer<TippedArrowFromFlaskRecipe> {
        public static final TippedArrowFromFlaskRecipe.Serializer INSTANCE = new TippedArrowFromFlaskRecipe.Serializer();
        private static final ResourceLocation ID = new ResourceLocation(PotionFlasks.MOD_ID, "tipped_arrow_from_flask_recipe");
        public TippedArrowFromFlaskRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            return new TippedArrowFromFlaskRecipe(pRecipeId);
        }

        public TippedArrowFromFlaskRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new TippedArrowFromFlaskRecipe(pRecipeId);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, TippedArrowFromFlaskRecipe pRecipe) {

        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return TippedArrowFromFlaskRecipe.Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}
