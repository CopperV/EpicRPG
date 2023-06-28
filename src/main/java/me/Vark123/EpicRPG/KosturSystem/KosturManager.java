package me.Vark123.EpicRPG.KosturSystem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class KosturManager {
	
	private static final KosturManager instance = new KosturManager();
	
	@Getter(value = AccessLevel.NONE)
	public final long CLICK_COOLDOWN;
	@Getter(value = AccessLevel.NONE)
	public final long CAST_TIME;
	
	private final Map<Player, KosturClickInfo> combos;
	
	private KosturManager() {
		CLICK_COOLDOWN = 100;
		CAST_TIME = 3000;
		
		combos = new ConcurrentHashMap<>();
	}
	
	public static final KosturManager getInstance() {
		return instance;
	}
	
	public boolean isUsingCombo(Player p, ItemStack kostur) {
		return combos.containsKey(p) && combos.get(p).getKostur().equals(kostur);
	}
	
	public boolean isComboExpired(Player p, ItemStack kostur) {
		if(!isUsingCombo(p, kostur))
			return true;
		KosturClickInfo info = combos.get(p);
		if((System.currentTimeMillis() - info.getLastUse()) > CAST_TIME)
			return true;
		return false;
	}
	
	public boolean isCooldownPass(Player p, ItemStack kostur) {
		if(!isUsingCombo(p, kostur))
			return false;
		KosturClickInfo info = combos.get(p);
		if((System.currentTimeMillis() - info.getLastUse()) < CLICK_COOLDOWN)
			return false;
		return true;
	}
	
	public KosturClickInfo getPlayerCombo(Player p) {
		return combos.getOrDefault(p, null);
	}
	
	public void createCombo(Player p, ItemStack kostur) {
		KosturClickInfo info = new KosturClickInfo(p, kostur);
		combos.put(p, info);
		sendTitle(p, info);
	}
	
	public void updateCombo(Player p, ItemStack kostur, boolean isRight) {
		if(!isUsingCombo(p, kostur))
			return;
		KosturClickInfo info = combos.get(p);
		info.updateClicks();
		switch(info.getClickAmount()) {
			case 2:
				info.setR2(isRight);
				break;
			case 3:
				info.setR3(isRight);
				break;
		}
		sendTitle(p, info);
	}
	
	public boolean isComboFinished(Player p, ItemStack kostur) {
		if(!isUsingCombo(p, kostur))
			return false;
		return combos.get(p).getClickAmount() == 3;
	}
	
	public String finishCombo(Player p, ItemStack kostur) {
		if(!isUsingCombo(p, kostur))
			return "LLL";
		
		KosturClickInfo info = combos.get(p);
		StringBuilder builder = new StringBuilder();
		builder.append(info.isR1() ? "P" : "L");
		builder.append(info.isR2() ? "P" : "L");
		builder.append(info.isR3() ? "P" : "L");
		
		combos.remove(p);
		return builder.toString();
	}
	
	private void sendTitle(Player p, KosturClickInfo info) {
		StringBuilder builder = new StringBuilder("*-*-*");
		switch(info.getClickAmount()) {
			case 3:
				builder.setCharAt(4, info.isR3() ? 'P':'L');
			case 2:
				builder.setCharAt(2, info.isR2() ? 'P':'L');
			case 1:
				builder.setCharAt(0, info.isR1() ? 'P':'L');
				break;
		}
		String title = builder.toString();
		float pitch = 1 - 0.2f * (info.getClickAmount() - 1);
		
		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, pitch);
		p.sendTitle("§a§l"+title, null, 10, 40, 10);
	}

}
