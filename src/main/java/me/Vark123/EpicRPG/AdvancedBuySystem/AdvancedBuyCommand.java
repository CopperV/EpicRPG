package me.Vark123.EpicRPG.AdvancedBuySystem;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.StringUtils;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.CoinsCost;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.MoneyCost;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.ReputationCost;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.RudaCost;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.StygiaCost;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.UndefinedCost;

public class AdvancedBuyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("advancedbuy"))
			return false;
		if(!sender.hasPermission("epicrpg.gm")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie masz permisji do uzycia tej komendy");
			return false;
		}
		if(args.length < 4) {
			sendCorrectUsage(sender);
			return false;
		}
		
		if(Bukkit.getPlayerExact(args[0])==null) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[0]+" §cjest offline");
			return false;
		}
		if(!StringUtils.isNumeric(args[2])){
			sender.sendMessage(Main.getInstance().getPrefix()+args[2]+" §cnie jest liczba");
			return false;
		}

		Player p = Bukkit.getPlayer(args[0]);
		int amount = Integer.parseInt(args[2]);
		
		String MMItem = args[1];
		ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(MMItem);
		
		if(it == null) {
			sender.sendMessage(Main.getInstance().getPrefix()+args[1]+" §cnie istnieje");
			return false;
		}
		
		List<AdvancedBuyCost> list = new LinkedList<>();
		for(int i = 3; i < args.length; ++i) {
			list.add(getCost(args[i]));
		}
		
		for(AdvancedBuyCost cost : list) {
			if(cost.check(p))
				continue;
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie mozna tego zakupic!");
			p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz tego zakupic!");
			return false;
		}
		
		for(AdvancedBuyCost cost : list) {
			cost.spend(p);
		}
		
		it.setAmount(amount);
		int slot = p.getInventory().firstEmpty();
		if(slot >= 0 && slot < 36) {
			p.getInventory().setItem(slot, it);
		} else {
			p.getWorld().dropItem(p.getLocation().add(0, 0.5, 0), it);
		}
		
		p.sendMessage(Main.getInstance().getPrefix()+" §aZakupiles §7"+amount+" §r"+it.getItemMeta().getDisplayName());
		
		return true;
	}
	
	private void sendCorrectUsage(CommandSender sender) {
		sender.sendMessage(Main.getInstance().getPrefix()+" §cPoprawne uzycie komendy: §c§o/abs <nick> <MMItem> <ilosc> [wymagania/koszta]");
	}

	private AdvancedBuyCost getCost(String line) {
		String[] tab = line.split(":");
		String costType = tab[0].toLowerCase();
		switch(costType) {
			case "money":
				double money = Double.parseDouble(tab[1]);
				return new MoneyCost(money);
			case "coins":
				int coins = Integer.parseInt(tab[1]);
				return new CoinsCost(coins);
			case "ruda":
				int ruda = Integer.parseInt(tab[1]);
				return new RudaCost(ruda);
			case "stygia":
				int stygia = Integer.parseInt(tab[1]);
				return new StygiaCost(stygia);
			case "reputation":
				String fraction = tab[1].toLowerCase();
				int level = Integer.parseInt(tab[2]);
				return new ReputationCost(fraction, level);
			default:
				return new UndefinedCost();
		}
	}
	
}
