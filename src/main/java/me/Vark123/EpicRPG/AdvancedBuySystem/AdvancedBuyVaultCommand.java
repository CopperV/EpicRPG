package me.Vark123.EpicRPG.AdvancedBuySystem;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.StringUtils;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Core.CoinsSystem;
import me.Vark123.EpicRPG.Core.MoneySystem;
import me.Vark123.EpicRPG.Core.RudaSystem;
import me.Vark123.EpicRPG.Core.StygiaSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class AdvancedBuyVaultCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("advancedbuyvault"))
			return false;
		if(!sender.hasPermission("epicrpg.gm")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie masz permisji do uzycia tej komendy");
			return false;
		}
		if(args.length < 3) {
			sendCorrectUsage(sender);
			return false;
		}
		
		if(Bukkit.getPlayerExact(args[2])==null) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[2]+" §cjest offline");
			return false;
		}
		if(!StringUtils.isNumeric(args[1])){
			sender.sendMessage(Main.getInstance().getPrefix()+args[1]+" §cnie jest liczba");
			return false;
		}

		Player p = Bukkit.getPlayer(args[2]);
		int amount = Integer.parseInt(args[1]);
		
		Map<String, String> conditions = new LinkedHashMap<>();
		for(int i = 3; i < args.length; ++i) {
			String[] arr = args[i].split(":", 2);
			if(arr.length < 2)
				continue;
			conditions.put(arr[0], arr[1]);
		}
		
		AdvancedBuyEvent event = new AdvancedBuyEvent(p, conditions);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie mozna tego zakupic!");
			p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz tego zakupic!");
			return false;
		}
		
		event.getActions().forEach(action -> action.action(p));
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		switch(args[0].toLowerCase()) {
			case "money":
				MoneySystem.getInstance().addMoney(rpg, amount, "cmd");
				break;
			case "coins":
				CoinsSystem.getInstance().addCoins(rpg, amount, "cmd");
				break;
			case "stygia":
				StygiaSystem.getInstance().addStygia(rpg, amount, "cmd");
				break;
			case "ruda":
				RudaSystem.getInstance().addRuda(rpg, amount, "cmd");
				break;
		}
		
		return true;
	}
	
	private void sendCorrectUsage(CommandSender sender) {
		sender.sendMessage(Main.getInstance().getPrefix()+" §cPoprawne uzycie komendy: §c§o/absv <vault> <ilosc> <nick> [wymagania/koszta]");
	}

//	private AdvancedBuyCost getCost(String line) {
//		String[] tab = line.split(":");
//		String costType = tab[0].toLowerCase();
//		switch(costType) {
//			case "money":
//				double money = Double.parseDouble(tab[1]);
//				return new MoneyCost(money);
//			case "coins":
//				int coins = Integer.parseInt(tab[1]);
//				return new CoinsCost(coins);
//			case "ruda":
//				int ruda = Integer.parseInt(tab[1]);
//				return new RudaCost(ruda);
//			case "stygia":
//				int stygia = Integer.parseInt(tab[1]);
//				return new StygiaCost(stygia);
//			case "reputation":
//				String fraction = tab[1].toLowerCase();
//				int level = Integer.parseInt(tab[2]);
//				return new ReputationCost(fraction, level);
//			default:
//				return new UndefinedCost();
//		}
//	}
	
}
