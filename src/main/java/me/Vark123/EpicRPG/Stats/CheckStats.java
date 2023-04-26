package me.Vark123.EpicRPG.Stats;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class CheckStats {

	public static boolean check(RpgPlayer rpg, ItemStack item) {
		if(item == null ||
				item.getType().equals(Material.AIR) ||
				!item.hasItemMeta() ||
				!item.getItemMeta().hasLore())
			return true;
		if(isRune(item))
			return false;
		NBTItem nbtit = new NBTItem(item);
		if(nbtit.hasTag("RPGType")) return false;
		
		RpgPlayerInfo info = rpg.getInfo();
		RpgStats stats = rpg.getStats();
		MutableBoolean check = new MutableBoolean(true);
		item.getItemMeta().getLore().parallelStream().filter(s -> {
			if(s.contains("§4- §8Klasa: ") 
					|| s.contains("§4- §8Krag: §7"))
				return true;
			if(!s.contains(": §c"))
				return false;
			return true;
		}).takeWhile(s -> {
			return check.booleanValue();
		}).forEach(s -> {
//			Bukkit.broadcastMessage(s+" §r"+s.replace("§", "&"));
			if(s.contains("§4- §8Klasa: ")) {
				s = s.replace("§4- §8Klasa: ", "");
				String proffesion = ChatColor.stripColor(info.getProffesion());
				if(!proffesion.equalsIgnoreCase(ChatColor.stripColor(s))) {
					check.setValue(false);
				}
				return;
			} else if(s.contains("§4- §8Krag: §7")) {
				s = s.replace("§4- §8Krag: §7", "");
				if(stats.getKrag() < Integer.parseInt(ChatColor.stripColor(s))) {
					check.setValue(false);
				}
				return;
			} else if(s.contains("§4- §8Poziom: §c")) {
				s = s.replace("§4- §8Poziom: §c", "");
				if(info.getLevel() < Integer.parseInt(ChatColor.stripColor(s))) {
					check.setValue(false);
				}
				return;
			}
			s = s.replace("§4- §8", "");
			int toCheck = Integer.parseInt(ChatColor.stripColor(s.split(": ")[1]));
//			int toCheck = Integer.parseInt(s.replace(": ", ChatColor.stripColor(s)));
//			int toCheck = Integer.parseInt(s.replace(": §c", ""));
			int result;
			s = s.split(":")[0];
			s = Utils.convertToClassConvention(s);
			Class<?> _class = stats.getClass();
			String strMethod = "getFinal"+s;
			Method method;
			
			try {
				method = _class.getMethod(strMethod);
			} catch (NoSuchMethodException e) {
				return;
			} catch (SecurityException e) {
				return;
			}
			
			try {
				result = (int) method.invoke(stats);
			} catch (IllegalAccessException e) {
				return;
			} catch (IllegalArgumentException e) {
				return;
			} catch (InvocationTargetException e) {
				return;
			}
			
			if(result < toCheck) {
				check.setValue(false);
			}
		});;
		
		return check.booleanValue();
	}
	
	public static boolean isRune(ItemStack item) {
		if(item.getType().name().contains("RECORD")) 
			return true;
		else 
			return false;
	}
	
}
