package me.Vark123.EpicRPG.Placeholders;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.RpgStats;
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
				return stats.getFinalZdolnosci()+"";
			case "int":
				return stats.getFinalInteligencja()+"";
			case "mana":
				return stats.getFinalMana()+"";
			case "walka":
				return stats.getFinalWalka()+"";
			case "krag":
				return stats.getKrag()+"";
			default:
				return "Unmatched Placeholder";
		}
	}

}
