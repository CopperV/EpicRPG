package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class EndTutorialCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("endtut"))
			return false;
		if(sender instanceof Player) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cKomenda tylko dla graczy");
			return false;
		}
		if(args.length < 1) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNalezy podac gracza!");
			return false;
		}
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(Bukkit.getPlayer(args[0]));
		RpgPlayerInfo info = rpg.getInfo();
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				info.setDrop(true);
				info.setTutorial(false);
				rpg.getPlayer().teleport(Bukkit.getWorld("F_RPG").getSpawnLocation());
				rpg.getPlayer().getInventory().clear();
				me.Vark123.EpicRPGRespawn.Main.getPlayers().put(rpg.getPlayer().getUniqueId().toString(), 
						me.Vark123.EpicRPGRespawn.Main.getRegiony().get("spawn1").getLoc());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give "+args[0]+" ItemStart1");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give "+args[0]+" ItemStart2");
			}
		}.runTaskLater(Main.getInstance(), 20*3);
		return true;
	}

}
