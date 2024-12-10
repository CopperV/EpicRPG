package me.Vark123.EpicRPG.Core.Listeners;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.nikl.calendarevents.CalendarEvent;

public class HalloweenBossSpawnListener implements Listener {

	private static final Random rand = new Random();
	
	@EventHandler
	public void onDate(CalendarEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getLabels().contains("halloween_event"))
			return;
		RpgPlayer rpg = Bukkit.getOnlinePlayers().stream()
			.filter(p -> p.getGameMode().equals(GameMode.ADVENTURE) || p.getGameMode().equals(GameMode.SURVIVAL))
			.filter(p -> !p.getWorld().getName().equalsIgnoreCase("plots"))
			.filter(PlayerManager.getInstance()::playerExists)
			.map(PlayerManager.getInstance()::getRpgPlayer)
			.filter(_rpg -> _rpg.getInfo().getLevel() >= 25)
			.collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
				Collections.shuffle(list);
				return list.size() > 0 ? list.get(0) : null;
			}));
		if(rpg == null)
			return;
		Player p = rpg.getPlayer();
		Location loc = p.getLocation();
		int lvl = rpg.getInfo().getLevel();
		
		Bukkit.broadcastMessage(Main.getInstance().getPrefix()+" §7"+p.getName()+" §azostal zaatakowany przez §6§lJezdzca Bez Glowy§a!");
		Bukkit.broadcastMessage(Main.getInstance().getPrefix()+" §7"+p.getName()+" §azostal zaatakowany przez §6§lJezdzca Bez Glowy§a!");
		Bukkit.broadcastMessage(Main.getInstance().getPrefix()+" §7"+p.getName()+" §azostal zaatakowany przez §6§lJezdzca Bez Glowy§a!");
		
		double angle = rand.nextDouble(Math.PI*2);
		double x = Math.sin(angle) * 7;
		double z = Math.sin(angle) * 7;
		Location spawnLoc = loc.clone().add(x,0,z);
		while(spawnLoc.getBlock().getType().isSolid())
			spawnLoc.add(0,1,0);
		
		MythicBukkit.inst().getMobManager().getMythicMob("HalloweenSpecialBoss1").ifPresent(mMob -> {
			mMob.spawn(BukkitAdapter.adapt(spawnLoc), lvl);
		});
	}
	
}
