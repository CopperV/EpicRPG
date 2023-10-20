package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPG.AdvancedBuySystem.AdvancedBuyCommand;
import me.Vark123.EpicRPG.BlackrockSystem.BlackrockCommand;
import me.Vark123.EpicRPG.BoosterSystem.Commands.BaseBoostCommand;
import me.Vark123.EpicRPG.BoosterSystem.Commands.BoostCommandManager;
import me.Vark123.EpicRPG.BoosterSystem.Commands.BoosterMenuCommand;
import me.Vark123.EpicRPG.BoosterSystem.Commands.Impl.CoinsBoosterCommand;
import me.Vark123.EpicRPG.BoosterSystem.Commands.Impl.ExpBoosterCommand;
import me.Vark123.EpicRPG.BoosterSystem.Commands.Impl.MoneyBoosterCommand;
import me.Vark123.EpicRPG.BoosterSystem.Commands.Impl.RudaBoosterCommand;
import me.Vark123.EpicRPG.BoosterSystem.Commands.Impl.StygiaBoosterCommand;
import me.Vark123.EpicRPG.Chat.ChatClearCommand;
import me.Vark123.EpicRPG.Chat.ChatToggleCommand;
import me.Vark123.EpicRPG.Core.Commands.DropCommand;
import me.Vark123.EpicRPG.Core.Commands.EndTutorialCommand;
import me.Vark123.EpicRPG.Core.Commands.EpicRPGCommand;
import me.Vark123.EpicRPG.Core.Commands.KasaCommand;
import me.Vark123.EpicRPG.Core.Commands.KoszCommand;
import me.Vark123.EpicRPG.Core.Commands.LevelCommand;
import me.Vark123.EpicRPG.Core.Commands.ResetStatsCommand;
import me.Vark123.EpicRPG.Core.Commands.RotacjaCommand;
import me.Vark123.EpicRPG.Core.Commands.RpgMMCommand;
import me.Vark123.EpicRPG.Core.Commands.SklepCommand;
import me.Vark123.EpicRPG.Core.Commands.SoulbindItemCommand;
import me.Vark123.EpicRPG.Core.Commands.SprzedajCommand;
import me.Vark123.EpicRPG.Core.Commands.UniquetemCommand;
import me.Vark123.EpicRPG.Core.Commands.SystemCmds.DragonCoinsBuyCommand;
import me.Vark123.EpicRPG.Core.Commands.SystemCmds.DragonCoinsChangeCommand;
import me.Vark123.EpicRPG.Core.Commands.SystemCmds.DragonCoinsCmdRunCommand;
import me.Vark123.EpicRPG.Core.Commands.SystemCmds.EventCurrencyChangeCommand;
import me.Vark123.EpicRPG.Core.Commands.SystemCmds.RudaBuyCommand;
import me.Vark123.EpicRPG.Core.Commands.SystemCmds.RudaChangeCommand;
import me.Vark123.EpicRPG.Core.Commands.SystemCmds.StygiaBuyCommand;
import me.Vark123.EpicRPG.Core.Commands.SystemCmds.StygiaChangeCommand;
import me.Vark123.EpicRPG.HorseSystem.HorseSummonCommand;
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
		Bukkit.getPluginCommand("level").setExecutor(new LevelCommand());
		Bukkit.getPluginCommand("chat").setExecutor(new ChatToggleCommand());
		Bukkit.getPluginCommand("chatclear").setExecutor(new ChatClearCommand());
		Bukkit.getPluginCommand("rpgmenu").setExecutor(new MenuCommand());
		Bukkit.getPluginCommand("uniquegive").setExecutor(new UniquetemCommand());
		Bukkit.getPluginCommand("soulbindgive").setExecutor(new SoulbindItemCommand());
		Bukkit.getPluginCommand("rpgmm").setExecutor(new RpgMMCommand());

		Bukkit.getPluginCommand("kasa").setExecutor(new KasaCommand());
		Bukkit.getPluginCommand("stygiabuy").setExecutor(new StygiaBuyCommand());
		Bukkit.getPluginCommand("stygiamod").setExecutor(new StygiaChangeCommand());
		Bukkit.getPluginCommand("coinsbuy").setExecutor(new DragonCoinsBuyCommand());
		Bukkit.getPluginCommand("coinscmd").setExecutor(new DragonCoinsCmdRunCommand());
		Bukkit.getPluginCommand("dragoncoins").setExecutor(new DragonCoinsChangeCommand());
		Bukkit.getPluginCommand("brbuy").setExecutor(new RudaBuyCommand());
		Bukkit.getPluginCommand("ruda").setExecutor(new RudaChangeCommand());
		Bukkit.getPluginCommand("event").setExecutor(new EventCurrencyChangeCommand());

		Bukkit.getPluginCommand("kon").setExecutor(new HorseSummonCommand());
		Bukkit.getPluginCommand("rotacja").setExecutor(new RotacjaCommand());
		Bukkit.getPluginCommand("kosz").setExecutor(new KoszCommand());
		Bukkit.getPluginCommand("advancedbuy").setExecutor(new AdvancedBuyCommand());
		Bukkit.getPluginCommand("blackrock").setExecutor(new BlackrockCommand());
		Bukkit.getPluginCommand("drop").setExecutor(new DropCommand());

		Bukkit.getPluginCommand("endtut").setExecutor(new EndTutorialCommand());
		Bukkit.getPluginCommand("sprzedaj").setExecutor(new SprzedajCommand());
		Bukkit.getPluginCommand("sklep").setExecutor(new SklepCommand());
		Bukkit.getPluginCommand("reset").setExecutor(new ResetStatsCommand());

		Bukkit.getPluginCommand("epicrpg").setExecutor(new EpicRPGCommand());
		Bukkit.getPluginCommand("epicboost").setExecutor(new BaseBoostCommand());
		Bukkit.getPluginCommand("modyfikatory").setExecutor(new BoosterMenuCommand());
		
		BoostCommandManager.get().registerSubcommand(new CoinsBoosterCommand());
		BoostCommandManager.get().registerSubcommand(new ExpBoosterCommand());
		BoostCommandManager.get().registerSubcommand(new MoneyBoosterCommand());
		BoostCommandManager.get().registerSubcommand(new RudaBoosterCommand());
		BoostCommandManager.get().registerSubcommand(new StygiaBoosterCommand());
	}
	
}
