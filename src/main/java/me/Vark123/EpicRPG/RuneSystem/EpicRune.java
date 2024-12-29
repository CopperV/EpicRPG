package me.Vark123.EpicRPG.RuneSystem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import lombok.Getter;

@Getter
public class EpicRune {

	private String mythicType;
	private String name;
	private String magicType;
	private double damage;
	private double obszar;
	private double wplyw;
	
	private long regenTime;
	private int durationTime;
	
	private int krag;
	
	private int price;
	private boolean hpInsteadMana;
	
	private String klasa;
	
	private int pvp;

	public EpicRune(ItemStack it) {
		ReadableNBT nbt = NBT.readNbt(it);
		nbt = nbt.getCompound("PublicBukkitValues");
		this.mythicType = nbt.getString("mythicmobs:type");
		
		ItemMeta im = it.getItemMeta();
		this.name = im.getDisplayName();
		
		im.getLore().stream()
			.filter(s -> s.contains(": "))
			.filter(s -> !s.contains("%"))
			.forEach(s -> {
				if(s.contains("Obrazenia")) {
					this.damage = Double.parseDouble(
							ChatColor.stripColor(s).split(": ")[1]);
					return;
				}
				if(s.contains("Krag: ")) {
					this.krag = Integer.parseInt(ChatColor.stripColor(s).split(": ")[1]);
				}
				if(s.contains("Klasa: ")) {
					this.klasa = ChatColor.stripColor(s).split(": ")[1];
				}
				if(s.contains("Koszt ")) {
					if(s.contains("zycia"))
						this.hpInsteadMana = true;
					this.price = Integer.parseInt(
							ChatColor.stripColor(s).split(": ")[1]);
				}
				if(s.contains("Obszar: ")) {
					obszar = Double.parseDouble(
							ChatColor.stripColor(s).split(": ")[1]);
				}
				if(s.contains("Czas trwania: ")) {
					this.durationTime = Integer.parseInt(
							ChatColor.stripColor(s).split(": ")[1].split(" ")[0]);
				}
				if(s.contains("Czas regeneracji: ")) {
					this.regenTime = Integer.parseInt(
							ChatColor.stripColor(s).split(": ")[1].split(" ")[0]);
				}
				if(s.contains("Wplyw umyslu: ")) {
					this.wplyw = Double.parseDouble(
							ChatColor.stripColor(s).split(": ")[1]);
				}
				if(s.contains("Typ magii")) {
					this.magicType = ChatColor.stripColor(s).split(": ")[1];
				}
				if(s.contains("PVP")) {
					String tmp = ChatColor.stripColor(s).split(": ")[1].toLowerCase();
					switch(tmp) {
						case "tak":
							this.pvp = 1;
							break;
						case "nie":
							this.pvp = 2;
							break;
					}
				}
			});
	}
	
	public boolean isClassRequired() {
		return klasa != null && !klasa.isEmpty();
	}
	
	public boolean canUseInPlayerLocation(Player p) {
		if(pvp == 0) {
			p.sendMessage("§7[§bEpicRPG§7] §cMasz przestarzala rune! Nie mozesz jej uzyc");
			return false;
		}
		if(pvp == 1) {
			return true;
		}
		if(pvp == 2) {
			RegionQuery query = WorldGuard.getInstance()
					.getPlatform()
					.getRegionContainer()
					.createQuery();
			ApplicableRegionSet set = query.getApplicableRegions(
					BukkitAdapter.adapt(p.getLocation()));
			State flag = set.queryValue(null, Flags.PVP);
			if(flag == null || flag.equals(State.DENY) 
					|| p.getWorld().getName().toLowerCase().contains("dungeon")
					|| p.getWorld().getName().toLowerCase().contains("raid")) 
				return true;

			p.sendMessage("§7[§bEpicRPG§7] §cTej runy nie mozesz uzyc na PvP!");
			return false;
		}
		return false;
	}
	
}
