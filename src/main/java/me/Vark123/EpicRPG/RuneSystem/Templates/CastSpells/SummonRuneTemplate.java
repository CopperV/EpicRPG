package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.mobs.entities.SpawnReason;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonManager;
import me.Vark123.EpicRPG.Utils.Utils;

public class SummonRuneTemplate {

	private static final Random rand = new Random();
	private static final String CONTROLABLE_SUFFIX = "_Controlable";
	private static final String WILD_SUFFIX = "_Wild";
	
	private SummonRuneTemplate() { }
	
	public static void castSummon(
			ACastableRune castableRune,
			String mobType,
			Location summonLoc,
			double spawnRadius,
			IRuneLocationEffect onControlableSpawnEffect,
			IRuneLocationEffect onWildSpawnEffect) {
		
		int finalInt = castableRune.getRpgPlayer().getStats().getFinalInteligencja();
		boolean canControl = castableRune.getRune().getMinIntToControl() <= finalInt;
		
		String finalMobType = mobType + (canControl ? CONTROLABLE_SUFFIX : WILD_SUFFIX);
		MythicBukkit.inst().getMobManager().getMythicMob(finalMobType).ifPresent(mythicMob -> {
			double angle1 = rand.nextDouble(Math.PI*2);
			double angle2 = rand.nextDouble(Math.PI*2);
			
			Location randomLoc = summonLoc.clone().add(Utils.transferSphericalToVector(spawnRadius, angle1, angle2));
			
			ActiveMob aMob = MythicBukkit.inst().getMobManager().spawnMob(finalMobType, BukkitAdapter.adapt(randomLoc), SpawnReason.SUMMON, 1);
			aMob.getVariables().putInt("mana_cost", castableRune.getRune().getPriceOverTime());
			
			Player p = castableRune.getPlayer();
			SummonManager.get().addPlayerSummonInfo(p, aMob, castableRune);
			
			if(canControl) {
				aMob.setOwner(castableRune.getPlayer().getUniqueId());
				
				onControlableSpawnEffect.playEffect(BukkitAdapter.adapt(aMob.getLocation()));
			} else {
				aMob.setTarget(BukkitAdapter.adapt(p));
				
				onWildSpawnEffect.playEffect(BukkitAdapter.adapt(aMob.getLocation()));
			}
		});
	}
	
}
