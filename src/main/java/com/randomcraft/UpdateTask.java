package com.randomcraft;

import org.bukkit.Bukkit;

public class UpdateTask implements Runnable {
    private int tick = 0;
    public UpdateTask() {
        Bukkit.getServer().getScheduler().runTaskTimer(RandomCraft.getInstance(), this, 1L, 1L);
    }

    @Override
    public void run() {
        if (this.tick % 20 == 0) {
            if (RandomCraft.getInstance().getWorldManager().getTimeLeft() <= 0) {
                RandomCraft.getInstance().getWorldManager().setSeconds(
                        RandomCraft.getInstance().getConfig().getInt("reset.seconds-lifetime"));
                RandomCraft.getInstance().getWorldManager().reset();
            }
            else {
                RandomCraft.getInstance().getWorldManager().setSeconds(
                        RandomCraft.getInstance().getWorldManager().getTimeLeft() - 1);
            }
        }
        this.tick++;
    }
}
