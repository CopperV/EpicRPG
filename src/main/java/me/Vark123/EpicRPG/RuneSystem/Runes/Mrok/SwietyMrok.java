package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Stats.ChangeStats;

public class SwietyMrok extends ACastableRune {
	
	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;

	public SwietyMrok(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		ChangeStats.change(rpgPlayer);
		
		RpgStats stats = rpgPlayer.getStats();
		double damage = 4*stats.getFinalSila() + 2.75*stats.getFinalWytrzymalosc();
		
		double r = rune.getObszar();
		Location startLoc = player.getLocation().clone().add(0, 0.5, 0);
		
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				r,
				0,
				0,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 1.4f, 0.65F);

					for(int i = 0; i < 100; ++i) {
						double radius = rand.nextDouble(r);
						double angle = rand.nextDouble(Math.PI * 2);
						
						double x = Math.sin(angle) * radius;
						double y = rand.nextDouble(3.5) - 1;
						double z = Math.cos(angle) * radius;
						
						Location target = loc.clone().add(x,y,z);
						
						Vector vec1 = new Vector(rand.nextDouble(2)-1, rand.nextDouble(2)-1, rand.nextDouble(2)-1).normalize();
						Vector vec2 = new Vector(rand.nextDouble(2)-1, rand.nextDouble(2)-1, rand.nextDouble(2)-1).normalize();
						loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, target, 0, vec1.getX(), vec1.getY(), vec1.getZ(), rand.nextDouble(0.2)+0.1);
						loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, target, 0, vec2.getX(), vec2.getY(), vec2.getZ(), rand.nextDouble(0.2)+0.1);
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune, damage));
					
					loc.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.8f, 0.5F);
				});
	}

}
