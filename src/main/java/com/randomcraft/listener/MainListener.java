package com.randomcraft.listener;

import com.randomcraft.RandomCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Objects;

public class MainListener implements Listener {
    @EventHandler
    private void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(RandomCraft.getInstance().getWorldManager().getWorld().getSpawnLocation());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "recipe take " + player.getName() + " *");
    }

    @EventHandler
    private void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        String name = event.getName();
        if (RandomCraft.getInstance().getData().get("players." + name) == null) {
            RandomCraft.getInstance().getData().set("players." + name + ".lives", 1);
        }
        if (RandomCraft.getInstance().getData().getInt("players." + name + ".lives") <= 0) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You have 0 lives buy donate :)");
        }
    }
    @EventHandler
    private void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
    }

    @EventHandler
    private void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        RandomCraft.getInstance().getData().set("players." + player.getName() + ".lives", RandomCraft.getInstance().getData().getInt("players." + player.getName() + ".lives") - 1);
        if (RandomCraft.getInstance().getData().getInt("players." + player.getName() + ".lives") <= 0) {
            player.kickPlayer("you death you have 0 lives)");
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    private void onPlayerPortalEvent(PlayerPortalEvent event) {
        Entity entity = event.getPlayer();
        World fromWorld = event.getFrom().getWorld();
        World toWorld = event.getFrom().getWorld();

        if (RandomCraft.getInstance().getWorldManager().getWorld().equals(fromWorld)) {
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                Location location = event.getTo();
                Objects.requireNonNull(location).setWorld(RandomCraft.getInstance().getWorldManager().getNether());
                event.setTo(location);
            }
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
                Location location = event.getTo();
                Objects.requireNonNull(location).setWorld(RandomCraft.getInstance().getWorldManager().getEnd());
                event.setTo(location);
            }
        }

        if (RandomCraft.getInstance().getWorldManager().getNether().equals(fromWorld)) {
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                Location location = event.getTo();
                Objects.requireNonNull(location).setWorld(RandomCraft.getInstance().getWorldManager().getWorld());
                event.setTo(location);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    private void onPlayerPortalEvent2(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        World fromWorld = event.getFrom().getWorld();
        World toWorld = event.getFrom().getWorld();

        if (RandomCraft.getInstance().getWorldManager().getWorld().equals(fromWorld)) {
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                Location location = event.getTo();
                Objects.requireNonNull(location).setWorld(RandomCraft.getInstance().getWorldManager().getNether());
                event.setTo(location);
            }
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
                Location location = event.getTo();
                Objects.requireNonNull(location).setWorld(RandomCraft.getInstance().getWorldManager().getEnd());
                event.setTo(location);
            }
        }

        if (RandomCraft.getInstance().getWorldManager().getNether().equals(fromWorld)) {
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                Location location = event.getTo();
                Objects.requireNonNull(location).setWorld(RandomCraft.getInstance().getWorldManager().getWorld());
                event.setTo(location);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    private void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World fromWorld = event.getFrom();
        if (RandomCraft.getInstance().getWorldManager().getEnd().equals(fromWorld)) {
            if (RandomCraft.getInstance().getWorldManager().getWorld().equals(player.getBedSpawnLocation().getWorld())) {
                player.teleport(player.getBedSpawnLocation());
            }
            else {
                player.teleport(RandomCraft.getInstance().getWorldManager().getWorld().getSpawnLocation());
            }
        }
    }
}
