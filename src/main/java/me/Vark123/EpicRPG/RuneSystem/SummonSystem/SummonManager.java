package me.Vark123.EpicRPG.RuneSystem.SummonSystem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobDespawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;

@Getter
public final class SummonManager implements Listener {

	private static final SummonManager inst = new SummonManager();
	
	private final Map<Player, Collection<SummonInfo>> playerSummons = new ConcurrentHashMap<>();
	
	private SummonManager() {
		
	}
	
	public static final SummonManager get() {
		return inst;
	}
	
	public void addPlayerSummonInfo(Player player, ActiveMob summon, ACastableRune rune) {
		SummonInfo info = new SummonInfo(player, summon, rune);
		
		if(!playerSummons.containsKey(player)) {
			Collection<SummonInfo> list = new HashSet<>();
			playerSummons.put(player, list);
		}
		
		playerSummons.get(player).add(info);
	}
	
	public void removePlayerSummonInfo(Player player, ActiveMob summon) {
		if(!playerSummons.containsKey(player))
			return;
		
		playerSummons.get(player).stream()
			.filter(info -> info.getSummon().equals(summon))
			.findAny()
			.ifPresent(playerSummons.get(player)::remove);
	}
	
	public int getSummonPoints(Player player) {
		if(!playerSummons.containsKey(player))
			return 0;
		
		MutableInt summonPoints = new MutableInt(0);
		playerSummons.get(player).stream()
			.map(info -> info.getCastableRune().getRune().getSummonPoints())
			.forEach(summonPoints::add);
		return summonPoints.intValue();
	}
	
	private void removeAllSummons(Player player) {
		if(!playerSummons.containsKey(player))
			return;
		
		playerSummons.get(player).stream()
			.collect(Collectors.toSet())
			.forEach(info -> {
				ActiveMob summon = info.getSummon();
				playerSummons.get(player).remove(info);
				
				summon.despawn();
			});
	}
	
	private void removeAllSummonInfo(ActiveMob aMob) {
		String mType = aMob.getMobType();
		if(!(mType.endsWith("_Controlable") || mType.endsWith("_Wild")))
			return;

		playerSummons.values().stream()
			.flatMap(list -> list.stream())
			.filter(info -> info.getSummon().equals(aMob))
			.collect(Collectors.toSet())
			.forEach(info -> playerSummons.get(info.getOwner()).remove(info));
	}
	
	@EventHandler
	private void onQuit(PlayerQuitEvent e) {
		removeAllSummons(e.getPlayer());
	}
	
	@EventHandler
	private void onKick(PlayerKickEvent e) {
		removeAllSummons(e.getPlayer());
	}
	
	@EventHandler
	private void onWorldChange(PlayerChangedWorldEvent e) {
		removeAllSummons(e.getPlayer());
	}
	
	@EventHandler
	private void onTeleport(PlayerTeleportEvent e) {
		if(!e.getFrom().getWorld().getUID().equals(e.getTo().getWorld().getUID()))
			return;
		if(e.getFrom().distanceSquared(e.getTo()) < 32*32)
			return;
		
		removeAllSummons(e.getPlayer());
	}
	
	@EventHandler
	private void onDespawn(MythicMobDespawnEvent e) {
		removeAllSummonInfo(e.getMob());
	}
	
	@EventHandler
	private void onDeath(MythicMobDeathEvent e) {
		removeAllSummonInfo(e.getMob());
	}
	
	@AllArgsConstructor
	@Getter
	private class SummonInfo {
		private Player owner;
		private ActiveMob summon;
		private ACastableRune castableRune;
	}
}
