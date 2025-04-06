package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import java.util.Random;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class MagicznePociski extends ACastableRune {

	private static final Random rand = new Random();

	private static final Vector UP = new Vector(0, 1, 0);
	private static final Vector RIGHT = new Vector(0, 0, 1);

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	private DustOptions dust1 = new DustOptions(Color.fromRGB(128, 0, 128), 1);
	private DustOptions dust2 = new DustOptions(Color.fromRGB(75, 0, 130), 1);
	private DustOptions dust3 = new DustOptions(Color.fromRGB(255, 0, 255), 1);
	
	public MagicznePociski(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.65, -0.65, -0.65, 
				0.65, 0.65, 0.65);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		Vector projectileDirection = startLoc.getDirection().clone().normalize();
		
		MutableDouble theta = new MutableDouble(rand.nextDouble(Math.PI*2));
		double thetaStep = Math.PI * 2 / 24;
		double amplitude = 1.5;
		
		Vector right = projectileDirection.clone().normalize().crossProduct(UP).normalize();
		Vector up = projectileDirection.clone().normalize().crossProduct(RIGHT).normalize();
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				projectileDirection,
				0.5, 
				2,
				1,
				30, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 1f);
					loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 0.75f);
					loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 0.5f);
				}, 
				loc -> {
					double offset1 = amplitude * Math.sin(theta.doubleValue());
					double offset2 = -amplitude * Math.sin(theta.doubleValue());
					
					Location pos1 = loc.clone().add(right.clone().multiply(offset1));
					Location pos2 = loc.clone().add(right.clone().multiply(offset2));
					Location pos3 = loc.clone().add(up.clone().multiply(offset1));
					
					loc.getWorld().spawnParticle(Particle.DUST, pos1, 8, 
							0.15f, 0.15f, 0.15f, 0.04f, dust1);
					loc.getWorld().spawnParticle(Particle.DUST, pos2, 8, 
							0.15f, 0.15f, 0.15f, 0.04f, dust2);
					loc.getWorld().spawnParticle(Particle.DUST, pos3, 8, 
							0.15f, 0.15f, 0.15f, 0.04f, dust3);
					
					theta.add(thetaStep);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1f);
						loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1.25f);
						loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1.5f);
					}
				}, 
				loc -> { });
	}

}
