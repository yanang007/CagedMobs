package com.corgam.cagedmobs.addons.jei;

import com.corgam.cagedmobs.serializers.mob.MobData;
import com.corgam.cagedmobs.setup.CagedItems;
import com.corgam.cagedmobs.setup.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EntityCategory implements IRecipeCategory<MobData> {

    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "entity");

    private static final int BG_PADDING = 5, BG_WIDTH = 166, BG_HEIGHT = 111;
    private final IDrawable icon;
    private final IDrawableStatic slotDrawable;
    private final IDrawable background;
    // Specific to MobData
    private final List<ItemStack> samplers = NonNullList.create();

    public EntityCategory(IGuiHelper gui){
        this.icon = gui.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CagedItems.MOB_CAGE.get()));
        this.slotDrawable = gui.getSlotDrawable();
        ResourceLocation backgroundRL = new ResourceLocation(Constants.MOD_ID, "textures/gui/entity.png");
        this.background = gui.createDrawable(backgroundRL,0,0,BG_WIDTH+BG_PADDING*2,BG_HEIGHT+BG_PADDING*2);
        ////// Specific do MobData
        // Add DNA samplers to the recipe based on the tier of recipe
        if(entity.getSamplerTier() >= 3){
            this.samplers.add(new ItemStack(CagedItems.DNA_SAMPLER_NETHERITE.get()));
        }else if(entity.getSamplerTier() == 2){
            this.samplers.add(new ItemStack(CagedItems.DNA_SAMPLER_NETHERITE.get()));
            this.samplers.add(new ItemStack(CagedItems.DNA_SAMPLER_DIAMOND.get()));
        }else{
            this.samplers.add(new ItemStack(CagedItems.DNA_SAMPLER_NETHERITE.get()));
            this.samplers.add(new ItemStack(CagedItems.DNA_SAMPLER_DIAMOND.get()));
            this.samplers.add(new ItemStack(CagedItems.DNA_SAMPLER.get()));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends MobData> getRecipeClass() {
        return MobData.class;
    }

    @Override
    public RecipeType<MobData> getRecipeType() {
        return CagedMobsPlugin.MOB_CAGE;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.category.cagedmobs.entity");
    }

    @Override
    public IDrawable getBackground() {

        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MobData wrapper, IFocusGroup focuses){
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 62+19)
                //.addItemStacks(wrapper.getSamplers());
                .addItemStack(new ItemStack(CagedItems.DNA_SAMPLER_DIAMOND.get()));
    }

//    @Override
//    public void setIngredients(EntityWrapper entityWrapper, IIngredients iIngredients) {
//        entityWrapper.setIngredients(iIngredients);
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout iRecipeLayout, EntityWrapper entityWrapper, IIngredients iIngredients) {
//        final IGuiItemStackGroup stacks = iRecipeLayout.getItemStacks();
//
//        // DNA Samplers
//        stacks.init(0, true, 19, 62+19);
//        stacks.set(0, entityWrapper.getSamplers());
//
//        // Soil Inputs
//        stacks.init(1, true, 19 + 20, 62 + 19);
//        stacks.set(1, entityWrapper.getEnvsItems());
//
//        int nextSlotId = 2;
//        List<Item> blacklistedItems = RecipesHelper.getItemsFromConfigList();
//        for (final LootData entry : entityWrapper.getDrops()) {
//            // If items not blacklisted draw them
//            if(!CagedMobs.SERVER_CONFIG.isItemsListInWhitelistMode()){
//                if(!blacklistedItems.contains(entry.getItem().getItem())){
//                    int relativeSlotId = nextSlotId - 2;
//                    stacks.init(nextSlotId, false, 100 + 19 * (relativeSlotId % 4), 5 + 19 * (relativeSlotId / 4));
//                    if(entry.isCooking() && entityWrapper.getCookedIDs().contains(relativeSlotId)){
//                        stacks.set(nextSlotId, entry.getCookedItem());
//                    }else{
//                        stacks.set(nextSlotId, entry.getItem());
//                    }
//                    nextSlotId++;
//                }
//            }else{
//                if(blacklistedItems.contains(entry.getItem().getItem())){
//                    int relativeSlotId = nextSlotId - 2;
//                    stacks.init(nextSlotId, false, 100 + 19 * (relativeSlotId % 4), 5 + 19 * (relativeSlotId / 4));
//                    if(entry.isCooking() && entityWrapper.getCookedIDs().contains(relativeSlotId)){
//                        stacks.set(nextSlotId, entry.getCookedItem());
//                    }else{
//                        stacks.set(nextSlotId, entry.getItem());
//                    }
//                    nextSlotId++;
//                }
//            }
//        }
//        stacks.addTooltipCallback(entityWrapper::getTooltip);
//    }

    @Override
    public void draw (MobData recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrix, double mouseX, double mouseY) {
        // Draw Seed & Soil
        this.slotDrawable.draw(matrix, 19, 62+19);
        this.slotDrawable.draw(matrix, 19+20, 62 + 19);
        this.slotDrawable.draw(matrix, 19+20, 62 + 19);
        // Draw Drops
        for (int nextSlotId = 2; nextSlotId < 22; nextSlotId++) {

            final int relativeSlotId = nextSlotId - 2;
            this.slotDrawable.draw(matrix, 100 + 19 * (relativeSlotId % 4), 5 + 19 * (relativeSlotId / 4));
        }
//        // Draw entity
//        recipe.drawInfo(getBackground().getWidth(), getBackground().getHeight(), matrix, mouseX, mouseY);
//        // Draw entity name
//        matrix.pushPose();
//        matrix.translate(5, 2, 0);
//        if(recipe.getEntity() != null && recipe.getEntity().getEntityType() != null) {
//            Minecraft.getInstance().font.draw(matrix, recipe.getEntity().getEntityType().getDescription(), 0, 0, 8);
//        }
//        // Draw required ticks
//        matrix.translate(5, 100, 0);
//        Minecraft.getInstance().font.draw(matrix, new TranslatableComponent("jei.tooltip.cagedmobs.entity.ticks",recipe.getSeconds()).getString(), 0, 0, 8);
//        // Draw if requires water
//        if(recipe.ifRequiresWater()){
//            matrix.translate(-5, 10, 0);
//            Minecraft.getInstance().font.draw(matrix, new TranslatableComponent("jei.tooltip.cagedmobs.entity.requiresWater").withStyle(ChatFormatting.BLUE), 0, 0, 8);
//        }
//        matrix.popPose();
    }
}
