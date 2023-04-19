package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPG.Jewelry.JewerlyCommand;
import me.Vark123.EpicRPG.Reputation.ReputationCommand;
import me.Vark123.EpicRPG.Reputation.ReputationModCommand;

public class CommandExecutorManager {

	public static void setExecutors() {
		Bukkit.getPluginCommand("bizuteria").setExecutor(new JewerlyCommand());
		Bukkit.getPluginCommand("reputation").setExecutor(new ReputationCommand());
		Bukkit.getPluginCommand("reputationmod").setExecutor(new ReputationModCommand());
	}
	
}
