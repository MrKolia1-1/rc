package com.randomcraft;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class WorldManager {
    private World world;
    private World nether;
    private World end;
    private int seconds;

    public WorldManager() {
        if (Bukkit.getWorld("randomcraft_world") == null) {
            this.world = new WorldCreator("randomcraft_world").environment(World.Environment.NORMAL).createWorld();
            Objects.requireNonNull(this.world).setSpawnLocation(0, this.world.getSpawnLocation().getBlockY(), 0);
        }
        else {
            this.world = Bukkit.getWorld("randomcraft_world");
        }
        if (Bukkit.getWorld("randomcraft_nether") == null) {
            this.nether = new WorldCreator("randomcraft_nether").environment(World.Environment.NETHER).createWorld();
        }
        else {
            this.nether = Bukkit.getWorld("randomcraft_nether");
        }
        if (Bukkit.getWorld("randomcraft_end") == null) {
            this.end = new WorldCreator("randomcraft_end").environment(World.Environment.THE_END).createWorld();
        }
        else {
            this.end = Bukkit.getWorld("randomcraft_end");
        }

        this.seconds = RandomCraft.getInstance().getConfig().getInt("reset.seconds-left");
    }

    public void reset() {
        RandomCraft.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            player.undiscoverRecipes(player.getDiscoveredRecipes());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "recipe take " + player.getName() + " *");
        });
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.kickPlayer("restart");
        });

        Bukkit.unloadWorld(this.world, false);
        Bukkit.unloadWorld(this.nether, false);
        Bukkit.unloadWorld(this.end, false);

        this.deleteWorld(this.world.getWorldFolder());
        this.deleteWorld(this.nether.getWorldFolder());
        this.deleteWorld(this.end.getWorldFolder());


        RandomCraft.getInstance().getData().set("recipes", null);
        RandomCraft.getInstance().getData().set("players", null);


        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RandomCraft.getInstance(), () -> {
            this.world = new WorldCreator("randomcraft_world").environment(World.Environment.NORMAL).createWorld();
            Objects.requireNonNull(this.world).setSpawnLocation(0, this.world.getSpawnLocation().getBlockY(), 0);
        },100);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RandomCraft.getInstance(), () -> {
            this.nether = new WorldCreator("randomcraft_nether").environment(World.Environment.NETHER).createWorld();
        },500);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RandomCraft.getInstance(), () -> {
            this.end = new WorldCreator("randomcraft_end").environment(World.Environment.THE_END).createWorld();
        },1000);

        Bukkit.clearRecipes();
        RandomCraft.getInstance().getRecipeManager().getRandomRecipeList().clear();
        RandomCraft.getInstance().getRecipeManager().generateRecipes();
    }

    private boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

    public World getWorld() {
        return world;
    }

    public World getNether() {
        return nether;
    }

    public World getEnd() {
        return end;
    }

    public int getTimeLeft() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
