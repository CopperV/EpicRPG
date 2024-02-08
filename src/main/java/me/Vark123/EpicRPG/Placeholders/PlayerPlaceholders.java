package me.Vark123.EpicRPG.Placeholders;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlayerPlaceholders extends PlaceholderExpansion {

	@Override
	public @NotNull String getAuthor() {
		return "Vark123";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "epicrpg";
	}

	@Override
	public @NotNull String getVersion() {
		return Main.getInstance().getDescription().getVersion();
	}

	@Override
	public String onRequest(OfflinePlayer p, @NotNull String identifier) {
		if(!p.isOnline())
			return "Offline Player";
		if(!PlayerManager.getInstance().playerExists((Player) p))
			return "Offline Player";
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) p);
		RpgPlayerInfo info = rpg.getInfo();
		RpgStats stats = rpg.getStats();
		RpgVault vault = rpg.getVault();
		switch(identifier) {
			case "level":
				return info.getLevel()+"";
			case "exp":
				return info.getExp()+"";
			case "nextlevel":
				return info.getNextLevel()+"";
			case "klasa":
				return info.getProffesion()+"";
			case "str":
				return stats.getFinalSila()+"";
			case "wytrz":
				return stats.getFinalWytrzymalosc()+"";
			case "zr":
				return stats.getFinalZrecznosc()+"";
			case "zd":
				return stats.getFinalZdolnosciMysliwskie()+"";
			case "int":
				return stats.getFinalInteligencja()+"";
			case "mana":
				return stats.getFinalMana()+"";
			case "percent_mana":
				return (double) stats.getPresentMana() / (double) stats.getFinalMana()+"";
			case "walka":
				return stats.getFinalWalka()+"";
			case "krag":
				return stats.getKrag()+"";
			case "kasa":
				return (int)vault.getMoney()+"";
			case "stygia":
				return vault.getStygia()+"";
			case "coins":
				return vault.getDragonCoins()+"";
			case "ruda":
				return vault.getBrylkiRudy()+"";
			case "damage":
				return stats.getFinalObrazenia()+"";
			case "defense":
				return stats.getFinalOchrona()+"";
			case "crit_pve":
			{
				int kryt = stats.getFinalWalka();
				int bonusKryt = info.getShortProf().equalsIgnoreCase("mys") ? 50 : 0;
				if(rpg.getSkills().hasCiosKrytyczny())
					bonusKryt += 25;
				kryt += bonusKryt;
				
				double percent = (double) kryt / 500. * 100.;
				return String.format("%.1f",percent);
			}
			case "crit_pvp":
			{
				int kryt = stats.getFinalWalka();
				int bonusKryt = info.getShortProf().equalsIgnoreCase("mys") ? 50 : 0;
				if(rpg.getSkills().hasCiosKrytyczny())
					bonusKryt += 25;
				kryt += bonusKryt;
				
				double percent = (double) kryt / 2500. * 100.;
				return String.format("%.1f",percent);
			}
			case "pn":
				return info.getPn()+"";
			case "exp_percent":
				return String.format("%.2f", (((double)(info.getExp() - ExpSystem.getInstance().getNextLevelExp(info.getLevel() - 1)))
						/ ((double)(info.getNextLevel() - ExpSystem.getInstance().getNextLevelExp(info.getLevel() - 1)))
						* 100.0));
			case "raw_klasa":
				return ChatColor.stripColor(info.getProffesion());
			default:
				return "Unmatched Placeholder";
		}
	}

}
