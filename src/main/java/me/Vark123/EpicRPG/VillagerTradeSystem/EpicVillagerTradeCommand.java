package me.Vark123.EpicRPG.VillagerTradeSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;

public class EpicVillagerTradeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("epic-trader"))
			return false;

		if(!sender.hasPermission("epicrpg.trades"))
			return false;
		if(args.length < 2) {
			sendCorrectUsage(sender);
			return false;
		}
		
		Player p = Bukkit.getPlayerExact(args[0]);
		if(p == null) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §7§o"+args[0]+" §cjest offline!");
			return false;
		}
		
		if(EpicVillagerManager.get().getVillager(args[1]).isEmpty()) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §7§oVillager o id "+args[1]+" nie istnieje");
			return false;
		}
		
		EpicVillagerManager.get().openTrader(p, args[1]);
		return true;
	}
	
	private void sendCorrectUsage(CommandSender sender) {
		sender.sendMessage(Main.getInstance().getPrefix()+" §cPoprawne uzycie komendy: §7§o/epic-trader <nick> <villager-id>");
	}

}
