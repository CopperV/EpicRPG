package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;

public class GrupoweLeczenie extends ACastableRune {

	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	public GrupoweLeczenie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		RpgStats stats = rpgPlayer.getStats();
		double value = 100 + 0.35 * stats.getFinalMana();
		double radius = rune.getObszar();
		
		Location startLoc = castLoc.clone().add(0, 0.05, 0);
		
		InstantRangeRuneTemplate.castEffect(
				this, 
				startLoc,
				rune.getObszar(),
				0,
				0, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_VILLAGER_YES, 1.5f, 1f);
					for(double i = 0.25; i < radius; i += 0.25) {
						double angleOffset = rand.nextDouble(Math.PI * 2);
						double angleStep = Math.PI * 2 / (i*4);
						for(int j = 0; j < i*4; ++j) {
							double angle = angleOffset + j * angleStep;
							
							double x = i * Math.sin(angle);
							double y = i * 0.05;
							double z = i * Math.cos(angle);
							
							Location pos = loc.clone().add(x,y,z);
							pos.getWorld().spawnParticle(Particle.HEART, pos, 1,
									0, 0, 0, 0.1);
						}
					}
				}, 
				hitCondition,
				(loc, entity) -> {
					if(!(entity instanceof Player target))
						return;
					
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(target);
					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, value);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						Location loc2 = entity.getLocation().clone().add(0, 0.1, 0);
						for(double i = 0.25; i < 2; i += 0.25) {
							double angleOffset = rand.nextDouble(Math.PI * 2);
							double angleStep = Math.PI * 2 / (i*6);
							for(int j = 0; j < i*6; ++j) {
								double angle = angleOffset + j * angleStep;
								
								double x = i * Math.sin(angle);
								double y = i * 0.75;
								double z = i * Math.cos(angle);
								
								Location pos = loc2.clone().add(x,y,z);
								pos.getWorld().spawnParticle(Particle.HEART, pos, 1,
										0, 0, 0, 0.1);
							}
						}
					}
				});
	}

}
