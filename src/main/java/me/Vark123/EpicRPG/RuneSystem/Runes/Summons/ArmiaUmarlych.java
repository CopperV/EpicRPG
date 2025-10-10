package me.Vark123.EpicRPG.RuneSystem.Runes.Summons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import io.lumine.mythic.api.mobs.entities.SpawnReason;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.variables.Variable;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonDefinition;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonLevelCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonRegistry;
import me.Vark123.EpicRPG.Utils.Utils;

public class ArmiaUmarlych extends ACastableRune {
	
	private static final List<String> POSSIBLE_RUNES = new ArrayList<>(List.of(
			"PrzyzwanieZombie",
			"PrzyzwanieSzkieleta",
			"PrzyzwanieSzkieletaStrzelca",
			"PrzyzwanieSzkieletaWojownika",
			"PrzyzwanieSzkieletaMaga"));
	
	private static final Map<String, EpicRune> SUMMON_COSTS = new HashMap<>();
	
	static {
		POSSIBLE_RUNES.forEach(rune -> {
			EpicRune summonRune = getSummonRune(rune);
			SUMMON_COSTS.put(rune, summonRune);
		});
	}
	
	private static EpicRune getSummonRune(String runeName) {
		ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(runeName);
		if(!Utils.isRune(it))
			return null;
		
		return new EpicRune(it);
	}

	private Map<String, Variable> variables = new HashMap<>();
	private final Random rand = new Random();
	private IRuneLocationEffect spawnEffect = loc -> {
		new BukkitRunnable() {
			int timer = 3 * 4;
			double radius = 1;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0) {
					cancel();
					return;
				}
				--timer;
				
				for(int i = 0; i < 4*radius; i++) {
					double angle = rand.nextDouble(Math.PI*2);
					
					double x = radius * Math.sin(angle);
					double z = radius * Math.cos(angle);
					
					Location point = loc.clone().add(x,0,z);
					point.getWorld().spawnParticle(Particle.LARGE_SMOKE, point, 0, 0, 1, 0, rand.nextFloat(0.04f, 0.16f));
				}
				for(int i = 0; i < 12*radius*Math.log(radius); i++) {
					double r = rand.nextDouble(radius);
					double angle = rand.nextDouble(Math.PI*2);
					
					double x = r * Math.sin(angle);
					double z = r * Math.cos(angle);
					
					Location point = loc.clone().add(x,0,z);
					point.getWorld().spawnParticle(Particle.WITCH, point, 1, 0, 0, 0, 0.2f);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 4);
	};
	
	public ArmiaUmarlych(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		int summonPoints = rpgPlayer.getStats().getCurrentSummonPoints();
		if(summonPoints < 1)
			return;
		
		int tries = 100;
		while(summonPoints > 0 && tries > 100) {
			String chosen = POSSIBLE_RUNES.get(rand.nextInt(POSSIBLE_RUNES.size()));
			EpicRune summonRune = SUMMON_COSTS.getOrDefault(chosen, null);
			
			if(summonRune == null || summonRune.getSummonPoints() < 1 || summonRune.getSummonPoints() > summonPoints) {
				--tries;
				continue;
			}
			
			summonPoints -= summonRune.getSummonPoints();
			summon(chosen);
		}
	}
	
	private void summon(String summonRune) {
		Location startLoc = castLoc.clone().add(0, 0.1, 0);
		
		double maxRadius = 5;
		
		double angle = rand.nextDouble(Math.PI*2);
		double radius = rand.nextDouble(maxRadius);
		
		double x = radius * Math.sin(angle);
		double z = radius * Math.sin(angle);
		
		Location summonLoc = startLoc.clone().add(x,0,z);
		
		summon(summonRune, summonLoc);
	}
	
	private void summon(String summonRune, Location summonLoc) {
		int finalInt = rpgPlayer.getStats().getFinalInteligencja();
		
		SummonDefinition def = SummonRegistry.getDefinition(summonRune);
		if(def == null) {
			throw new IllegalArgumentException(summonRune+" is not summon-type rune");
		}
		
		EpicRune rune = SUMMON_COSTS.get(summonRune);
		
		boolean canControl = rune.getMinIntToControl() <= finalInt;
		String mobType = canControl ? def.getVariantForInt(finalInt).getMobType() : def.getWildMobType();
	
		MythicBukkit.inst().getMobManager().getMythicMob(mobType).ifPresent(mythicMob -> {
			ActiveMob aMob = MythicBukkit.inst().getMobManager()
					.spawnMob(mobType, BukkitAdapter.adapt(summonLoc), SpawnReason.SUMMON, 1);

			aMob.getVariables().putInt("mana_cost", rune.getPriceOverTime());
			
			aMob.getVariables().putAll(variables);
			
			aMob.getEntity().getBukkitEntity().setVelocity(new Vector(0, 0.3, 0));
			
			if(canControl) {
				SummonLevelCalcEvent event = new SummonLevelCalcEvent(player, aMob, this, 25);
				Bukkit.getPluginManager().callEvent(event);
				
				aMob.setLevel(event.getLevel());
				
				aMob.setOwnerUUID(player.getUniqueId());
				
				spawnEffect.playEffect(BukkitAdapter.adapt(aMob.getLocation()));
			} else {
				aMob.setTarget(BukkitAdapter.adapt(player));
			}
		});
	}

}
