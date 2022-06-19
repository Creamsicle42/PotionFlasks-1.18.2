package com.annabelle.potionflasks.potionflask;

import com.annabelle.potionflasks.ItemRegistry;
import com.annabelle.potionflasks.PotionFlasks;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class PotionFlaskRecipe implements CraftingRecipe {

    private final ResourceLocation id;

    public PotionFlaskRecipe(ResourceLocation id){
        this.id = id;
    }


    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {

        System.out.println("Testing potion flask recipe");

        boolean hasPotionFlask = false;
        boolean splashFlask = false;
        int potionsFound = 0;
        int maxPotions = PotionFlaskItem.getMaxFillLevel();
        String potionID = "";

        boolean foundPot = false;

        for(int i = 0; i < pContainer.getContainerSize(); i++){
            ItemStack item = pContainer.getItem(i);
            if(item.isEmpty()){
                continue;
            }
            if(item.getItem() == ItemRegistry.EMPTY_POTION_FLASK.get() ||
                    item.getItem() == ItemRegistry.EMPTY_SPLASH_POTION_FLASK.get() ||
                    item.getItem() == ItemRegistry.EMPTY_LINGERING_POTION_FLASK.get()){
                if(hasPotionFlask){
                    System.out.println("Too many flasks");
                    return false;
                }else{
                    System.out.println("Empty flask found");
                    hasPotionFlask = true;
                    continue;
                }
            }
            if(item.getItem() == Items.POTION){
                foundPot = true;
                // Set tag to first potion found
                System.out.println("Found potion");
                if(potionID == ""){
                    potionID = item.getTag().getString("Potion");

                }
                else{
                    if(!potionID.equals(item.getTag().getString("Potion"))){
                        System.out.println("Alternate potion found");
                        return false;
                    }
                }
                potionsFound += 1;
                continue;
            }
            if(item.getItem() == ItemRegistry.POTION_FLASK.get() ||
                    item.getItem() == ItemRegistry.SPLASH_POTION_FLASK.get() ||
                    item.getItem() == ItemRegistry.LINGERING_POTION_FLASK.get()){
                if(hasPotionFlask){return false;}
                if(potionID == ""){
                    potionID = item.getTag().getString("Potion");

                }else{
                    if(!potionID.equals(item.getTag().getString("Potion"))){
                        return false;
                    }
                }
                if(item.getTag().getInt("potionflasks:fill_level") == 0){return false;}
                maxPotions = maxPotions - item.getTag().getInt("potionflasks:fill_level");
                hasPotionFlask = true;
                continue;
            }

            return false;
        }
        return hasPotionFlask && (potionsFound <= maxPotions) && foundPot;
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer) {

        boolean splashFlask = false;
        boolean lingeringFlask = false;
        int baseFillLevel = 0;
        int fillAdd = 0;
        String potion = "";

        for(int i = 0; i < pContainer.getContainerSize(); i++){
            if(pContainer.getItem(i).isEmpty()){continue;}
            if(pContainer.getItem(i).getItem() == Items.POTION){
                fillAdd++;
                potion = pContainer.getItem(i).getTag().getString("Potion");
            }
            if(pContainer.getItem(i).getItem() == ItemRegistry.POTION_FLASK.get()){
                baseFillLevel = pContainer.getItem(i).getTag().getInt("potionflasks:fill_level");
            }
            if(pContainer.getItem(i).getItem() == ItemRegistry.SPLASH_POTION_FLASK.get()){
                baseFillLevel = pContainer.getItem(i).getTag().getInt("potionflasks:fill_level");
                splashFlask = true;
            }
            if(pContainer.getItem(i).getItem() == ItemRegistry.EMPTY_SPLASH_POTION_FLASK.get()){
                splashFlask = true;
            }
            if(pContainer.getItem(i).getItem() == ItemRegistry.LINGERING_POTION_FLASK.get()){
                baseFillLevel = pContainer.getItem(i).getTag().getInt("potionflasks:fill_level");
                lingeringFlask = true;
            }
            if(pContainer.getItem(i).getItem() == ItemRegistry.EMPTY_LINGERING_POTION_FLASK.get()){
                lingeringFlask = true;
            }
        }

        ItemStack flask;

        if(splashFlask){
            flask = new ItemStack(ItemRegistry.SPLASH_POTION_FLASK.get());
        } else if (lingeringFlask) {
            flask = new ItemStack(ItemRegistry.LINGERING_POTION_FLASK.get());
        } else{
            flask = new ItemStack(ItemRegistry.POTION_FLASK.get());
        }

        flask.setTag(new CompoundTag());

        flask.getTag().putString("Potion",
                potion);
        flask.getTag().putInt("potionflasks:fill_level", baseFillLevel + fillAdd);
        return flask;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer pContainer) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(pContainer.getContainerSize(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack item = pContainer.getItem(i);
            if (item.getItem() == Items.POTION) {
                nonnulllist.set(i, new ItemStack(Items.GLASS_BOTTLE));
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
    public RecipeSerializer<PotionFlaskRecipe> getSerializer() {
        return new PotionFlaskRecipe.Serializer();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PotionFlaskRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation(PotionFlasks.MOD_ID, "fill_potion_flask");
        public PotionFlaskRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            return new PotionFlaskRecipe(pRecipeId);
        }

        public PotionFlaskRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new PotionFlaskRecipe(pRecipeId);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, PotionFlaskRecipe pRecipe) {

        }
    }
}
