package me.Vark123.EpicRPG.Core.CPS;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.NonNull;
import me.Vark123.EpicRPG.Config;
import me.Vark123.EpicRPG.Main;

public final class CPSManager {
	
	private static final CPSManager inst = new CPSManager();

	private final Map<Player, MutableInt> leftClicks = new ConcurrentHashMap<>();
	private final Map<Player, MutableInt> rightClicks = new ConcurrentHashMap<>();
	
	private CPSManager() { }
	
	public static final CPSManager get() {
		return inst;
	}
	
	public void addClick(Player p, @NonNull ClickType click) {
		Map<Player, MutableInt> map = click.equals(ClickType.LEFT) ? leftClicks : rightClicks;
		if(!map.containsKey(p)){
			startMeasure(p, click);
			map.put(p, new MutableInt());
		}
		map.get(p).increment();
	}
	
	private void startMeasure(Player p, @NonNull ClickType click) {
		int measureDuration = Config.get().getMeasureDuration();
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> stopMeasure(p, click), measureDuration*20);
	}
	
	private void stopMeasure(Player p, @NonNull ClickType click) {
		Map<Player, MutableInt> map = click.equals(ClickType.LEFT) ? leftClicks : rightClicks;
		if(!map.containsKey(p))
			return;
		
		int clicks = map.get(p).getValue();
		double cps = clicks / (double) Config.get().getMeasureDuration();
		double maxCps = Config.get().getMaxCPS();
		if(cps > maxCps) {
			Bukkit.broadcast("§7[§4§lOSTRZEZENIE§7]", "rpg.mod");
			Bukkit.broadcast("§7§o"+p.getName()+" §cosiagnal §7"+String.format("%.1f", cps)+" §cCPS! §7[§4"+click.name()+" CLICK§7]", "rpg.mod");
		}
		map.remove(p);
	}
	
	public static enum ClickType {
		LEFT,
		RIGHT;
	}
	
}
