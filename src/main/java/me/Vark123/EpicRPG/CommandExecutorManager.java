package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPG.Chat.ChatClearCommand;
import me.Vark123.EpicRPG.Chat.ChatToggleCommand;
import me.Vark123.EpicRPG.Core.Commands.DragonCoinsBuyCommand;
import me.Vark123.EpicRPG.Core.Commands.DragonCoinsChangeCommand;
import me.Vark123.EpicRPG.Core.Commands.DragonCoinsCmdRunCommand;
import me.Vark123.EpicRPG.Core.Commands.EventCurrencyChangeCommand;
import me.Vark123.EpicRPG.Core.Commands.RudaBuyCommand;
import me.Vark123.EpicRPG.Core.Commands.RudaChangeCommand;
import me.Vark123.EpicRPG.Core.Commands.SoulbindItemCommand;
import me.Vark123.EpicRPG.Core.Commands.StygiaBuyCommand;
import me.Vark123.EpicRPG.Core.Commands.StygiaChangeCommand;
import me.Vark123.EpicRPG.Core.Commands.UniquetemCommand;
import me.Vark123.EpicRPG.Jewelry.JewelryCommand;
import me.Vark123.EpicRPG.MenuSystem.MenuCommand;
import me.Vark123.EpicRPG.Reputation.ReputationCommand;
import me.Vark123.EpicRPG.Reputation.ReputationModCommand;
import me.Vark123.EpicRPG.Stats.StatsCommand;

public class CommandExecutorManager {

	public static void setExecutors() {
		Bukkit.getPluginCommand("bizuteria").setExecutor(new JewelryCommand());
		Bukkit.getPluginCommand("reputation").setExecutor(new ReputationCommand());
		Bukkit.getPluginCommand("reputationmod").setExecutor(new ReputationModCommand());
		Bukkit.getPluginCommand("staty").setExecutor(new StatsCommand());
		Bukkit.getPluginCommand("chat").setExecutor(new ChatToggleCommand());
		Bukkit.getPluginCommand("chatclear").setExecutor(new ChatClearCommand());
		Bukkit.getPluginCommand("rpgmenu").setExecutor(new MenuCommand());
		Bukkit.getPluginCommand("uniquegive").setExecutor(new UniquetemCommand());
		Bukkit.getPluginCommand("soulbindgive").setExecutor(new SoulbindItemCommand());

		Bukkit.getPluginCommand("stygiabuy").setExecutor(new StygiaBuyCommand());
		Bukkit.getPluginCommand("stygiamod").setExecutor(new StygiaChangeCommand());
		Bukkit.getPluginCommand("coinsbuy").setExecutor(new DragonCoinsBuyCommand());
		Bukkit.getPluginCommand("coinscmd").setExecutor(new DragonCoinsCmdRunCommand());
		Bukkit.getPluginCommand("dragoncoins").setExecutor(new DragonCoinsChangeCommand());
		Bukkit.getPluginCommand("brbuy").setExecutor(new RudaBuyCommand());
		Bukkit.getPluginCommand("ruda").setExecutor(new RudaChangeCommand());
		Bukkit.getPluginCommand("event").setExecutor(new EventCurrencyChangeCommand());
	}
	
}
