package me.Vark123.EpicRPG.Players.Components.Scoreboard;

import java.awt.Color;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;

public class ScoreboardPlaceholders extends PlaceholderExpansion {

	@Override
	public @NotNull String getAuthor() {
		return "Vark123";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "epicscoreboard";
	}

	@Override
	public @NotNull String getVersion() {
		return Main.getInstance().getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, @NotNull String identifier) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		switch(identifier.toLowerCase()) {
			case "info":
				return ChatColor.translateAlternateColorCodes('&', "&3》 "+ChatColor.of(new Color(66, 104, 124)).toString() + "" + ChatColor.BOLD + "INFO &3《");
			case "resources":
				return ChatColor.translateAlternateColorCodes('&', "&3》 "+ChatColor.of(new Color(66, 104, 124)).toString() + "" + ChatColor.BOLD + "ZASOBY &3《");
			case "stats":
				return ChatColor.translateAlternateColorCodes('&', "&3》 "+ChatColor.of(new Color(66, 104, 124)).toString() + "" + ChatColor.BOLD + "STATY &3《");
			case "nick":
			{
				String klasa = "  &bGracz: ";
				switch(ChatColor.stripColor(rpg.getInfo().getProffesion().toLowerCase())) {
					case "wojownik":
						klasa += "&c&o";
						break;
					case "mag":
						klasa += "&5&o";
						break;
					case "mysliwy":
						klasa += "&2&o";
						break;
					default:
						klasa += "&e&o";
						break;
				}
				klasa += player.getName();
				return ChatColor.translateAlternateColorCodes('&', klasa);
			}
			case "klasa":
				return ChatColor.translateAlternateColorCodes('&', "  &bKlasa: "+rpg.getInfo().getProffesion());
			case "level":
			{
				String poziom = "  &bPoziom: &e";
				if(rpg.getInfo().getLevel() == ExpSystem.getInstance().MAX_LEVEL)
					poziom += "MAX";
				else
					poziom += rpg.getInfo().getLevel();
				return ChatColor.translateAlternateColorCodes('&', poziom);
			}
			case "exp":
			{
				String exp = "  &bExp: &e";
				if(rpg.getInfo().getLevel() == ExpSystem.getInstance().MAX_LEVEL)
					exp += "MAX";
				else 
					exp += (rpg.getInfo().getExp()+"&a/&e"+rpg.getInfo().getNextLevel());
				return ChatColor.translateAlternateColorCodes('&', exp);
			}
			case "percent_exp":
			{
				String exp = "  &bExp: &e";
				if(rpg.getInfo().getLevel() == ExpSystem.getInstance().MAX_LEVEL)
					exp += "MAX";
				else {
					RpgPlayerInfo info = rpg.getInfo();
					exp += String.format("%.2f", (((double)(info.getExp() - ExpSystem.getInstance().getNextLevelExp(info.getLevel() - 1)))
							/ ((double)(info.getNextLevel() - ExpSystem.getInstance().getNextLevelExp(info.getLevel() - 1)))
							* 100.0))+"%";
				}
				return ChatColor.translateAlternateColorCodes('&', exp);
			}
			case "present_exp":
			{
				String exp = "  &bExp: &e";
				if(rpg.getInfo().getLevel() == ExpSystem.getInstance().MAX_LEVEL)
					exp += "MAX";
				else 
					exp += rpg.getInfo().getExp();
				return ChatColor.translateAlternateColorCodes('&', exp);
			}
			case "nextlevel_exp":
			{
				String exp = "  &bExp: &e";
				if(rpg.getInfo().getLevel() == ExpSystem.getInstance().MAX_LEVEL)
					exp += "MAX";
				else 
					exp += rpg.getInfo().getNextLevel();
				return ChatColor.translateAlternateColorCodes('&', exp);
			}
			case "pn":
				return ChatColor.translateAlternateColorCodes('&', "  &bPunkty nauki: &e"+rpg.getInfo().getPn());
			case "money":
				return ChatColor.translateAlternateColorCodes('&', "  &bSaldo: &e"+((int)rpg.getVault().getMoney())+"$");
			case "stygia":
				return ChatColor.translateAlternateColorCodes('&', "  &bStygia: &3"+rpg.getVault().getStygia());
			case "coins":
				return ChatColor.translateAlternateColorCodes('&', "  &bSmocze monety: &4"+rpg.getVault().getDragonCoins());
			case "ruda":
				return ChatColor.translateAlternateColorCodes('&', "  &bBrylki rudy: &9"+rpg.getVault().getBrylkiRudy());
			case "str":
				return ChatColor.translateAlternateColorCodes('&', "  &bSila: &e"+rpg.getStats().getFinalSila());
			case "wytrz":
				return ChatColor.translateAlternateColorCodes('&', "  &bWytrzymalosc: &e"+rpg.getStats().getFinalWytrzymalosc());
			case "zr":
				return ChatColor.translateAlternateColorCodes('&', "  &bZrecznosc: &e"+rpg.getStats().getFinalZrecznosc());
			case "zd":
				return ChatColor.translateAlternateColorCodes('&', "  &bZdolnosci mysliwskie: &e"+rpg.getStats().getFinalZdolnosci());
			case "walka":
				return ChatColor.translateAlternateColorCodes('&', "  &bWalka: &e"+rpg.getStats().getFinalWalka());
			case "krag":
				return ChatColor.translateAlternateColorCodes('&', "  &bKrag: &e"+rpg.getStats().getKrag());
			case "int":
				return ChatColor.translateAlternateColorCodes('&', "  &bInteligencja: &e"+rpg.getStats().getFinalInteligencja());
			case "mana":
				return ChatColor.translateAlternateColorCodes('&', "  &bMana: &9"+rpg.getStats().getPresentMana()+"&b/&9"+rpg.getStats().getFinalMana());
			case "present_mana":
				return ChatColor.translateAlternateColorCodes('&', "  &bObecna mana: &9"+rpg.getStats().getPresentMana());
			case "max_mana":
				return ChatColor.translateAlternateColorCodes('&', "  &bMax mana: &9"+rpg.getStats().getFinalMana());
			case "hp":
				return ChatColor.translateAlternateColorCodes('&', "  &bHP: &c"+((int)player.getHealth())+"&b/&c"+((int)player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
			case "percent_hp":
				return ChatColor.translateAlternateColorCodes('&', "  &bHP: &c"+String.format("%.2f", player.getHealth()/player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*100.)+"%");
		}
		return " ";
	}
	
}
