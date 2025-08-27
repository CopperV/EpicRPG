package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class SetZiraelStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		switch(info.getSetCounts().getOrDefault("Zirael", 0)) {
			case 4:
			{
				Location sourceLoc = e.getRpgPlayer().getPlayer().getLocation().clone();
				double radius = 12;
				
				int amount = sourceLoc.getWorld().getNearbyEntities(sourceLoc, radius, radius, radius, entity -> {
					if(entity.getLocation().distanceSquared(sourceLoc) > radius * radius)
						return false;

					if(!MythicBukkit.inst().getMobManager().isMythicMob(entity))
						return false;
					
					ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
					if(aMob == null || aMob.isDead() || aMob.getType().getIsInvincible()
							|| (aMob.hasFaction() && (aMob.getFaction().equals("ALLY") || aMob.getFaction().equals("SUMMONS"))))
						return false;
					
					if(!aMob.getEntity().isDamageable())
						return false;
					
					return true;
				}).size();
				
				if(amount >= 8 && statType == StatTypes.ZRECZNOSC)
					e.increaceStatValue(50);
			}
			case 3:
				if(statType == StatTypes.ZRECZNOSC)
					e.increaceStatValue(30);
				break;
		}
	}
	
}
