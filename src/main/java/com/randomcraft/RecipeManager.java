package com.randomcraft;

import net.minecraft.world.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.*;

public class RecipeManager {
    private List<RandomRecipe> randomRecipeList = new ArrayList<>();
    private List<ItemStack> results = new ArrayList<>();
    public RecipeManager() {
        if (RandomCraft.getInstance().getData().get("recipes") == null) {
            return;
        }
        List<ShapedRecipe> list = new ArrayList<>();
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe old = (ShapedRecipe) recipe;
                list.add(old);
            }
        });

        randomRecipeList.forEach(recipe -> {
            results.add(recipe.shapedRecipe.getResult());
        });


        Bukkit.clearRecipes();
        list.forEach(oldRecipe -> {
            Material material = Material.getMaterial(RandomCraft.getInstance().getData().getString("recipes." + oldRecipe.getKey().getKey()));
            ItemStack oldstack = oldRecipe.getResult();
            oldstack.setType(material);
            ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(RandomCraft.getInstance(), oldRecipe.getKey().getKey()), oldstack);
            shapedRecipe.shape(oldRecipe.getShape());
            oldRecipe.getIngredientMap().forEach((character, itemStack) -> {
                if (itemStack == null) return;
                shapedRecipe.setIngredient(character, itemStack.getType());
            });
            Bukkit.addRecipe(shapedRecipe);
            this.randomRecipeList.add(new RandomRecipe(shapedRecipe));
        });
    }

    public void generateRecipes() {
        List<ShapedRecipe> list = new ArrayList<>();
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe old = (ShapedRecipe) recipe;
                list.add(old);
            }
        });

        randomRecipeList.forEach(recipe -> {
            results.add(recipe.shapedRecipe.getResult());
        });

        Bukkit.clearRecipes();
        list.forEach(oldRecipe -> {
            ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(RandomCraft.getInstance(), oldRecipe.getKey().getKey()), getItem());
            shapedRecipe.shape(oldRecipe.getShape());
            oldRecipe.getIngredientMap().forEach((character, itemStack) -> {
                if (itemStack == null) return;
                shapedRecipe.setIngredient(character, itemStack.getType());
            });
            Bukkit.addRecipe(shapedRecipe);
            this.randomRecipeList.add(new RandomRecipe(shapedRecipe));
        });
    }

    public class RandomRecipe {
        public final ShapedRecipe shapedRecipe;

        public RandomRecipe(ShapedRecipe shapedRecipe) {
            this.shapedRecipe = shapedRecipe;
        }
    }

    private ItemStack getItem() {
        return results.get(new Random().nextInt(results.size()));
    }

    public List<RandomRecipe> getRandomRecipeList() {
        return randomRecipeList;
    }
}
