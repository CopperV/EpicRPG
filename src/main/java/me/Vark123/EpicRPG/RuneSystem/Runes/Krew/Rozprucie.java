package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Rozprucie extends ACastableRune {
	
	private static final Random random = new Random();
	
	private IRuneHitCondition hitCondition;

	public Rozprucie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.5, 0);
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				0,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_DEATH, 1.33f, 0.6f);
					
					double radius = rune.getObszar();
					double points = Math.pow(rune.getObszar(), 3);
					for(int i = 0; i < points; ++i) {
						double r = random.nextDouble(radius);
						double angle1 = random.nextDouble(Math.PI * 2);
						double angle2 = random.nextDouble(Math.PI * 2);
						
						double x = r * Math.cos(angle1) * Math.sin(angle2);
						double y = r * Math.sin(angle1);
						double z = r * Math.cos(angle1) * Math.cos(angle2);
						Location pos = loc.clone().add(x, y, z);
						loc.getWorld().spawnParticle(Particle.DUST, pos, 1,
								0, 0, 0, random.nextDouble(), new DustOptions(Color.fromRGB(154, 3, 3), random.nextFloat(3f, 5f)));
					}
				},
				hitCondition,
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune);
				});
	}

}
