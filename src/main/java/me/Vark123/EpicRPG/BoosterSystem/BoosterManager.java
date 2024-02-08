package me.Vark123.EpicRPG.BoosterSystem;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import me.Vark123.EpicRPG.Main;

@Getter
public class BoosterManager {

	private static final BoosterManager inst = new BoosterManager();
	
	private final Map<Booster, Date> boosters;
	
	private final BukkitTask boosterController;
	
	private BoosterManager() {
		boosters = new ConcurrentHashMap<>();
		
		boosterController = new BukkitRunnable() {
			@Override
			public void run() {
				Date now = new Date();
				boosters.entrySet().stream()
					.filter(entry -> entry.getValue().before(now))
					.map(entry -> entry.getKey())
					.forEach(booster -> {
						Bukkit.broadcastMessage(Main.getInstance().getPrefix()+" §eEfekt ksiegi wiedzy §7[§6"+booster.getDisplay().toUpperCase()+" §e§o+"+String.format("%2d", (int)(booster.getModifier()*100))+"%§7] "
								+ "§eskonczyl sie!");
						boosters.remove(booster);
					});
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 20*1);
	}
	
	public static final BoosterManager get() {
		return inst;
	}
	
	public void initBooster(String name, String display, double modifier, long time) {
		Booster template = Booster.builder()
				.name(name.toUpperCase())
				.display(display)
				.modifier(modifier)
				.build();
		Date date = new Date(boosters.getOrDefault(template, new Date()).getTime() + time);
		boosters.put(template, date);
	}
	
	public void registerBooster(String player, String name, String display, double modifier, long time) {
//		display = ChatColor.translateAlternateColorCodes('&', display);
		initBooster(name.toUpperCase(), display, modifier, time);
		
		long minutes = (time/(1000*60)) % 60;
		long hours = (time/(1000*60*60));
		Bukkit.broadcastMessage(Main.getInstance().getPrefix()+" §a"+player+" §euzyl ksiegi wiedzy §7[§6"+display.toUpperCase()+" §e§o+"+String.format("%2d", (int)(modifier*100))+"%§7] "
				+ "§ena czas §7"+String.format("%02d", hours)+":"+String.format("%02d", minutes)+"§e!");
		Bukkit.getOnlinePlayers().parallelStream()
			.forEach(p -> {
				p.playSound(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
				p.sendTitle("§e§lUZYTO KSIEGI WIEDZY", "§7[§6"+display.toUpperCase()+" §e§o+"+String.format("%2d", (int)(modifier*100))+"%§7] §aprzez §o"+player, 5, 10, 15);
			});
		
		updateBoosters(name.toUpperCase(), modifier, time);
	}
	
	private void updateBoosters(String name, double modifier, long time) {
		boosters.entrySet().stream()
			.filter(entry -> entry.getKey().getName().equals(name))
			.filter(entry -> entry.getKey().getModifier() < modifier)
			.filter(entry -> entry.getValue().after(new Date()))
			.forEach(entry -> {
				Date newDate = new Date(entry.getValue().getTime() + time);
				Booster booster = entry.getKey();
				boosters.put(booster, newDate);
			});
	}
	
	public Entry<Booster, Date> getTopBooster(String name) {
		Optional<Entry<Booster,Date>> result = boosters.entrySet()
				.stream()
				.filter(entry -> entry.getKey().getName().equalsIgnoreCase(name))
				.filter(entry -> entry.getValue().after(new Date()))
				.max((e1, e2) -> Double.compare(e1.getKey().getModifier(), e2.getKey().getModifier()));
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
}
