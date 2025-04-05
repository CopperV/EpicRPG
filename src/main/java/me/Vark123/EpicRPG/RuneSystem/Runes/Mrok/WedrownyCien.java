package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.ActiveMob.ThreatTable;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;

public class WedrownyCien extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private Random rand = new Random();

	public WedrownyCien(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.5, 0);
		
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.WEDROWNY_CIEN,
				player,
				target -> {
					target.getWorld().playSound(target, Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 0.8f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.5f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.02, 0.08);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.03);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 4, 0.4f, 0.8f, 0.4f, force);
				}));
		
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				0,
				loc -> { },
				hitCondition,
				(loc, entity) -> {
					Creature creature = (Creature) entity;
					if(creature.getTarget() == null || !creature.getTarget().equals(player))
						return;
					creature.setTarget(null);
					
					ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
					if(aMob != null) {
						if(aMob.hasThreatTable()) {
							ThreatTable threatTable = aMob.getThreatTable();
							if(threatTable.getTopThreatHolder().getBukkitEntity().equals(player)) {
								threatTable.clearTarget();
								threatTable.targetHighestThreat();
							} else {
								threatTable.threatSet(BukkitAdapter.adapt(player), 0);
							}
						}
						aMob.resetTarget();
					}
					
				});
	}

}
