package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class DropCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("drop"))
			return false;
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cTylko gracz moze uzyc tej komendy");
			return false;
		}
		Player p = (Player) sender;
		if(!PlayerManager.getInstance().playerExists(p))
			return false;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		if(info.isTutorial()) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz uzyc tej komendy w czasie samouczka!");
			return false;
		}
		sender.sendMessage("§aOd teraz"+(info.isDrop()?" nie ":" ")+"mozesz wyrzucac itemow z ekwipunku!");
		info.setDrop(!info.isDrop());
		return true;
	}

}
