package me.Vark123.EpicRPG.RuneSystem;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class ItemStackRune {
	
	private String name;
	private int damage;
	private Date date = new Date();
	private long regenTime;
	private int durationTime;
	private int krag;
	private int price;
	private boolean hpInsteadMana;
	private boolean reqKlasa;
	private String klasa;
	private int obszar;
	private double wplyw;
	private int pvp;
	private String magicType;
	
	public ItemStackRune(ItemStack it) {
		name = it.getItemMeta().getDisplayName();
		it.getItemMeta().getLore().parallelStream().filter(s -> {
			if(!s.contains(": "))
				return false;
			if(s.contains("%"))
				return false;
			return true;
		}).forEach(s -> {
			if(s.contains("Obrazenia")) {
				damage = Integer.parseInt(ChatColor.stripColor(s).split(": ")[1]);
				return;
			}
			if(s.contains("Krag: ")) {
				krag = Integer.parseInt(ChatColor.stripColor(s).split(": ")[1]);
			}
			if(s.contains("Klasa: ")) {
				klasa = ChatColor.stripColor(s).split(": ")[1];
				reqKlasa = true;
			}
			if(s.contains("Koszt ")) {
				if(s.contains("zycia"))
					hpInsteadMana = true;
				price = Integer.parseInt(ChatColor.stripColor(s).split(": ")[1]);
			}
			if(s.contains("Obszar: ")) {
				obszar = Integer.parseInt(ChatColor.stripColor(s).split(": ")[1]);
			}
			if(s.contains("Czas trwania: ")) {
				durationTime = Integer.parseInt(ChatColor.stripColor(s).split(": ")[1].split(" ")[0]);
			}
			if(s.contains("Czas regeneracji: ")) {
				int seconds = Integer.parseInt(ChatColor.stripColor(s).split(": ")[1].split(" ")[0]);
				regenTime = 1000 * seconds;
			}
			if(s.contains("Wplyw umyslu: ")) {
				wplyw = Double.parseDouble(ChatColor.stripColor(s).split(": ")[1]);
			}
			if(s.contains("Typ magii")) {
				magicType = ChatColor.stripColor(s).split(": ")[1];
			}
			if(s.contains("PVP")) {
				String tmp = ChatColor.stripColor(s).split(": ")[1].toLowerCase();
				switch(tmp) {
					case "tak":
						pvp = 1;
						break;
					case "nie":
						pvp = 2;
						break;
				}
			}
		});
	}
	
	public String getName() {
		return name;
	}

	public int getDamage() {
		return damage;
	}

	public Date getDate() {
		return date;
	}

	public long getRegenTime() {
		return regenTime;
	}

	public int getDurationTime() {
		return durationTime;
	}

	public int getKrag() {
		return krag;
	}

	public int getPrice() {
		return price;
	}

	public boolean isHpInsteadMana() {
		return hpInsteadMana;
	}

	public boolean isReqKlasa() {
		return reqKlasa;
	}

	public String getKlasa() {
		return klasa;
	}

	public int getObszar() {
		return obszar;
	}

	public double getWplyw() {
		return wplyw;
	}

	public int getPvp() {
		return pvp;
	}

	public String getMagicType() {
		return magicType;
	}

	public void modifyRegenTime(RpgStats stats) {
		double percent = (stats.getFinalMana() / (70. * 100.));
		if(percent > 0.5)
			percent = 0.5;
		regenTime *= 1. - percent;
	}

}
