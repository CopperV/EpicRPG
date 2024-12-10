package me.Vark123.EpicRPG.Placeholders;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import me.Vark123.EpicRPG.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class MiscPlaceholders extends PlaceholderExpansion {

	@Override
	public @NotNull String getAuthor() {
		return "Vark123";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "epicmisc";
	}

	@Override
	public @NotNull String getVersion() {
		return Main.getInstance().getDescription().getVersion();
	}

	@Override
	public String onRequest(OfflinePlayer p, @NotNull String identifier) {
		if(identifier.startsWith("players_in_world_")) {
			String world = identifier.replace("players_in_world_", "");
			World w = Bukkit.getWorld(world);
			if(w == null)
				return 0 + "";
			return w.getPlayers().stream()
					.filter(_p -> _p.getGameMode().equals(GameMode.SURVIVAL) || _p.getGameMode().equals(GameMode.ADVENTURE))
					.collect(Collectors.toList()).size()+" ";
		}
		return "";
	}

}
