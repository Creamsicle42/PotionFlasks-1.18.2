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
        int potionsNeeded = 8;
        String potionID = "";


        for(int i = 0; i < pContainer.getContainerSize(); i++){
            ItemStack item = pContainer.getItem(i);
            if(item.isEmpty()){continue;}
            if(item.getItem() == ItemRegistry.EMPTY_POTION_FLASK.get()){
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
                // Set tag to first potion found
                System.out.println("Found potion");
                if(potionsNeeded == 8){
                    potionID = item.getTag().getString("Potion");
                    System.out.println("Potion type set to " + potionID);
                }
                else{
                    if(!potionID.equals(item.getTag().getString("Potion"))){
                        System.out.println("Alternate potion found");
                        return false;
                    }
                }
                potionsNeeded -= 1;
            }
        }
        System.out.println("Potion flasks needed: " + potionsNeeded);
        return hasPotionFlask && (potionsNeeded == 0);
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer) {
        ItemStack flask = new ItemStack(ItemRegistry.POTION_FLASK.get());
        flask.setTag(new CompoundTag());
        for(int i = 0; i < pContainer.getContainerSize(); i++){
            if(pContainer.getItem(i).isEmpty()){continue;}
            if(pContainer.getItem(i).getItem() == Items.POTION){
                flask.getTag().putString("Potion",
                        pContainer.getItem(i).getTag().getString("Potion"));
            }
        }

        return flask;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(ItemRegistry.POTION_FLASK.get());
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    public static class Serializer implements RecipeSerializer<PotionFlaskRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(PotionFlasks.MOD_ID,"fill_potion_flask");

        @Override
        public PotionFlaskRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new PotionFlaskRecipe(id);
        }

        @Override
        public PotionFlaskRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

            return new PotionFlaskRecipe(id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PotionFlaskRecipe recipe) {

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
            return Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}
