package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
import me.Vark123.EpicRPG.Utils.Utils;

public class KasaCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("kasa")) return false;
		Player p;
		if(args.length > 0) {
			if(Bukkit.getPlayerExact(args[0]) == null) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(Main.getInstance().getPrefix()+" §cTylko gracze moga uzyc tej komendy!");
					return false;
				}
				p = (Player) sender;
			} else {
				p = Bukkit.getPlayer(args[0]);
			}
		} else {
			if(!(sender instanceof Player)) {
				sender.sendMessage(Main.getInstance().getPrefix()+" §cTylko gracze moga uzyc tej komendy!");
				return false;
			}
			p = (Player) sender;
		}
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgVault vault = rpg.getVault();
		RpgPlayerInfo info = rpg.getInfo();
		
		p.sendMessage("§a§l=========="+Main.getInstance().getPrefix()+"§a§l==========");
		p.sendMessage("        §6§lSKARBIEC §e§l"+p.getName());
		p.sendMessage("§a§oStan konta: §e§o"+Utils.formatCurrencyGrouped(vault.getMoney())+"◎");
		if(info.getLevel()>=60)
			p.sendMessage("§a§oStygia: §3§o"+Utils.formatCurrencyGrouped(vault.getStygia()));
		if(info.getLevel()>=50)
			p.sendMessage("§a§oSmocze monety: §c§o"+Utils.formatCurrencyGrouped(vault.getDragonCoins()));
		p.sendMessage("§a§oBrylki rudy: §9§o"+Utils.formatCurrencyGrouped(vault.getBrylkiRudy()));
//		p.sendMessage("§a§oSloneczka: §e§o"+vault.getEventCurrency());
//		p.sendMessage("§a§oPalemki: §e§o"+vault.getEventCurrency2());
		p.sendMessage("§a§l=========="+Main.getInstance().getPrefix()+"§a§l==========");
		
		return true;
	}

}
