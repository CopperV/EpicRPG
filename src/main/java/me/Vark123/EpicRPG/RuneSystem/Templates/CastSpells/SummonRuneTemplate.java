package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import io.lumine.mythic.api.mobs.entities.SpawnReason;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import io.lumine.mythic.core.skills.variables.Variable;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonDefinition;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonLevelCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonManager;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonRegistry;
import me.Vark123.EpicRPG.Utils.Utils;

public class SummonRuneTemplate {

	private static final Random rand = new Random();
	private static final MobExecutor mobManager = MythicBukkit.inst().getMobManager();
	
	private SummonRuneTemplate() { }
	
	public static void castSummon(
			ACastableRune castableRune,
			Location summonLoc,
			double spawnRadius,
			IRuneLocationEffect onControlableSpawnEffect,
			IRuneLocationEffect onWildSpawnEffect) {
		castSummon(castableRune, summonLoc, spawnRadius, onControlableSpawnEffect, onWildSpawnEffect, new HashMap<>());
	}
	
	public static void castSummon(
			ACastableRune castableRune,
			Location summonLoc,
			double spawnRadius,
			IRuneLocationEffect onControlableSpawnEffect,
			IRuneLocationEffect onWildSpawnEffect,
			Map<String, Variable> variables) {
		
		int finalInt = castableRune.getRpgPlayer().getStats().getFinalInteligencja();
		
		SummonDefinition def = SummonRegistry.getDefinition(castableRune.getRune().getMythicType());
		if(def == null) {
			throw new IllegalArgumentException(castableRune.getRune().getMythicType()+" is not summon-type rune");
		}
		
		boolean canControl = castableRune.getRune().getMinIntToControl() <= finalInt;
		String mobType = canControl ? def.getVariantForInt(finalInt).getMobType() : def.getWildMobType();
		
		mobManager.getMythicMob(mobType).ifPresent(mythicMob -> {
			double angle1 = rand.nextDouble(Math.PI*2);
			
			Location randomLoc = summonLoc.clone().add(Utils.transferSphericalToVector(spawnRadius, angle1, 0));
			
			ActiveMob aMob = mobManager.spawnMob(mobType, BukkitAdapter.adapt(randomLoc), SpawnReason.SUMMON, 1);
			aMob.getVariables().putInt("mana_cost", castableRune.getRune().getPriceOverTime());
			
			aMob.getVariables().putAll(variables);
			
			aMob.getEntity().getBukkitEntity().setVelocity(new Vector(0, 0.3, 0));
			
			Player p = castableRune.getPlayer();
			SummonManager.get().addPlayerSummonInfo(p, aMob, castableRune);
			
			if(canControl) {
				SummonLevelCalcEvent event = new SummonLevelCalcEvent(p, aMob, castableRune);
				Bukkit.getPluginManager().callEvent(event);
				
				aMob.setLevel(event.getLevel());
				
				aMob.setOwnerUUID(castableRune.getPlayer().getUniqueId());
				
				onControlableSpawnEffect.playEffect(BukkitAdapter.adapt(aMob.getLocation()));
			} else {
				aMob.setTarget(BukkitAdapter.adapt(p));
				
				onWildSpawnEffect.playEffect(BukkitAdapter.adapt(aMob.getLocation()));
			}
		});
	}
	
}
