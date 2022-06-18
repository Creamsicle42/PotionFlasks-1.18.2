package com.annabelle.potionflasks.potionfilling;

import com.annabelle.potionflasks.ItemRegistry;
import com.annabelle.potionflasks.PotionFlasks;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class PotionFillingRecipe implements CraftingRecipe {

    private final ResourceLocation id;

    public PotionFillingRecipe(ResourceLocation id){
        this.id = id;
    }


    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        // Search through container for 1 potion_flask and 1 empty bottle
        boolean bottleFlag = false;
        boolean flaskFlag = false;

        for(int i = 0; i < pContainer.getContainerSize(); i++){
            ItemStack item = pContainer.getItem(i);
            if(item.isEmpty()){continue;}
            if(item.getItem() == Items.GLASS_BOTTLE){
                if(bottleFlag){return false;}
                bottleFlag = true;
            }
            if(item.getItem() == ItemRegistry.POTION_FLASK.get()){
                if(flaskFlag){return false;}
                flaskFlag = true;
            }
        }

       return bottleFlag && flaskFlag;
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer) {
        ItemStack out = new ItemStack(Items.POTION);
        out.setTag(new CompoundTag());
        for(int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack item = pContainer.getItem(i);
            if(item.getItem() == ItemRegistry.POTION_FLASK.get()){
                out.getTag().putString("Potion",
                        item.getTag().getString("Potion"));
            }
        }
        return out;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer pContainer) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(pContainer.getContainerSize(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack item = pContainer.getItem(i);
            if (item.getItem() == ItemRegistry.POTION_FLASK.get()) {
                if(item.getTag().getInt("potionflasks:fill_level") == 1) {
                    nonnulllist.set(i, new ItemStack(ItemRegistry.EMPTY_POTION_FLASK.get()));
                }else{
                    ItemStack flask = new ItemStack(ItemRegistry.POTION_FLASK.get());
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
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(ItemRegistry.POTION_FLASK.get());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<PotionFillingRecipe> getSerializer() {
        return new PotionFillingRecipe.Serializer();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PotionFillingRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation(PotionFlasks.MOD_ID, "fill_from_potion_flask");
        public PotionFillingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            return new PotionFillingRecipe(pRecipeId);
        }

        public PotionFillingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new PotionFillingRecipe(pRecipeId);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, PotionFillingRecipe pRecipe) {

        }
    }
}
