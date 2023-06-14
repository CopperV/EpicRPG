package me.Vark123.EpicRPG.Scrolls;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class ScrollManager {

	private static final ScrollManager instance = new ScrollManager();
	
	@Getter(value = AccessLevel.NONE)
	public final int SCROLL_COOLDOWN;
	
	private final Map<Player, Date> clickCooldowns;
	
	private ScrollManager() {
		SCROLL_COOLDOWN = 500;
		clickCooldowns = new ConcurrentHashMap<>();
	}
	
	public static final ScrollManager getInstance() {
		return instance;
	}
	
	public boolean canPlayerClickScroll(Player p) {
		if(!clickCooldowns.containsKey(p))
			return false;
		
		long current = new Date().getTime();
		long old = clickCooldowns.get(p).getTime();
		if((current - old) < SCROLL_COOLDOWN)
			return false;
		return true;
	}
	
	public void setPlayerCooldown(Player p) {
		clickCooldowns.put(p, new Date());
	}
	
}
