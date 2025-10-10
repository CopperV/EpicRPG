package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MultipleProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class KoszmarBeliara extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
		
	public KoszmarBeliara(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.75, -0.75, -0.75, 
				0.75, 0.75, 0.75);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		MutableDouble damage = new MutableDouble(rune.getDamage());
		
		MultipleProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.625, 
				2,
				1,
				35, 
				0,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SHOOT, 1.25f, 0.7f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.WITCH, loc, 4, 
							0.1f, 0.1f, 0.1f, 0.08f);
					loc.getWorld().spawnParticle(Particle.SMOKE, loc, 4, 
							0.1f, 0.1f, 0.1f, 0.04f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune, damage.doubleValue())) {
						e.getWorld().playSound(loc, Sound.ENTITY_WITHER_HURT, 0.8f, 0.7f);

						loc.getWorld().spawnParticle(Particle.WITCH, loc, 15, 
								0.4f, 0.4f, 0.4f, 0.15f);
						
						damage.setValue(damage.doubleValue() * 0.85);
					}
				}, 
				loc -> { });
	}

}
