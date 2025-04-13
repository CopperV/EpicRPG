package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;

public class ZdrojZycia extends ACastableRune {
	
	private static final Random random = new Random();
	private IRuneHitCondition hitCondition;
	
	public ZdrojZycia(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone();
		double radius = rune.getObszar();
		double points = radius * 12;
		double angleStep = Math.PI * 2 / points;
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				radius,
				2,
				20,
				loc -> {							
					loc.getWorld().playSound(loc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
				},
				loc -> {
					double angleOffset = random.nextDouble(Math.PI * 2);
					for(int i = 0; i < points; ++i) {
						double angle = angleOffset + i*angleStep;
						double x = radius * Math.sin(angle);
						double z = radius * Math.cos(angle);
						
						Location pos = loc.clone().add(x, 0.1, z);
						pos.getWorld().spawnParticle(Particle.HEART, pos, 1,
								0.1, 0.05, 0.1, 0.02);
					}
					
					loc.getWorld().spawnParticle(Particle.FALLING_LAVA, loc.clone().add(0,2,0), 13,
							0.3, 2, 0.3, 0.15);
					for(int i = 0; i < 20; ++i) {
						double angle = random.nextDouble(Math.PI*2);
						double r = random.nextDouble(0.15);
						
						double x = r * Math.sin(angle);
						double y = random.nextDouble(1.5);
						double z = r * Math.cos(angle);
						
						Location pos = loc.clone().add(x,y,z);
						double force = random.nextDouble(0.01, 0.05);
						Vector dir = new Vector(
								pos.getX() - loc.getX(),
								pos.getY() - loc.getY(),
								pos.getZ() - loc.getZ()).normalize();
						pos.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, pos, 0,
								dir.getX(), dir.getY(), dir.getZ(), force);
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(!(entity instanceof Player target))
						return;
					
					RpgStats stats = rpgPlayer.getStats();
					double value = 25 + stats.getFinalMana()*0.02 + stats.getFinalInteligencja()*0.035;
					
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(target);
					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, value);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						target.getWorld().playSound(target.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1f, 0.9f);
						target.getWorld().spawnParticle(Particle.HEART, target.getLocation().clone().add(0,1,0), 14,
								0.4, 0.6, 0.4, 0.03);
					}
				},
				loc -> { });
	}

}
