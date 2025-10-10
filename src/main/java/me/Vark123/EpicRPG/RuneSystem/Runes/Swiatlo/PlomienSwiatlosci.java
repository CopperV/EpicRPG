package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MultipleProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class PlomienSwiatlosci extends ACastableRune {
	
	private static Vector worldUp = new Vector(0, 1, 0);
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private int points;
	
	public PlomienSwiatlosci(RpgPlayer rpgPlayer, EpicRune rune, int points) {
		super(rpgPlayer, rune);
		
		this.points = points;
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.65, -0.65, -0.65, 
				0.65, 0.65, 0.65);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		Vector forward = startLoc.getDirection().normalize();
		Vector right = forward.clone().crossProduct(worldUp).normalize();
		Vector normal = right.clone().crossProduct(forward).normalize();
		
		double angle = Math.PI * 2 / (double) points;
		for(int i = 0; i < points; ++i) {
			double _angle = i * angle;
			
			Vector dir = forward.clone().rotateAroundAxis(normal, _angle);
			spellEffect(startLoc, dir);
		}
	}
	
	private void spellEffect(Location startLoc, Vector direction) {
		MutableDouble damage = new MutableDouble(rune.getDamage());
		
		MultipleProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				direction,
				0.44, 
				1,
				1,
				32, 
				0,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.8f, 1.2f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 4, 
							0.18f, 0.18f, 0.18f, 0.01f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune, damage.doubleValue())) {
						e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 0.8f, 1.44f);

						loc.getWorld().spawnParticle(Particle.SOUL, loc.clone().add(0,1,0), 12, 
								0.35f, 0.75f, 0.35f, 0f);
						
						damage.setValue(damage.doubleValue() * 0.85);
					}
				}, 
				loc -> { });
	}

}
