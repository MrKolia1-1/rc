package com.randomcraft;

import com.randomcraft.command.RandomCraftCommand;
import com.randomcraft.listener.MainListener;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomCraft extends JavaPlugin {
    private RecipeManager recipeManager;
    private WorldManager worldManager;
    private UpdateTask updateTask;
    private FileConfiguration data;
    private File dataFile;

    @Override
    public void onDisable() {
        this.getConfig().set("reset.seconds-left", this.worldManager.getTimeLeft());
        this.recipeManager.getRandomRecipeList().forEach(recipe -> {
            this.data.set("recipes." + recipe.shapedRecipe.getKey().getKey(), recipe.shapedRecipe.getResult().getType().name());
        });

        this.saveConfig();
        try {
            this.data.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        this.registerListener();
        this.registerConfig();
        this.registerCommand();
        this.worldManager = new WorldManager();
        this.updateTask = new UpdateTask();
        this.recipeManager = new RecipeManager();

        if (this.recipeManager.getRandomRecipeList().size() == 0) {
            RandomCraft.getInstance().getRecipeManager().generateRecipes();
        }
    }

    private void registerCommand() {
        this.getCommand("randomcraft").setExecutor(new RandomCraftCommand());
    }

    private void registerListener() {
        this.getServer().getPluginManager().registerEvents(new MainListener(), this);
    }

    private void registerConfig() {
        this.saveDefaultConfig();

        this.dataFile = new File(getDataFolder(), "data.yml");
        if (!this.dataFile.exists()) {
            this.dataFile.getParentFile().mkdirs();
            this.saveResource("data.yml", false);
        }
        this.data = new YamlConfiguration();
        try {
            this.data.load(dataFile);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public static RandomCraft getInstance() {
        return getPlugin(RandomCraft.class);
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }


    public FileConfiguration getData() {
        return data;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }
}
