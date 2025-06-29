package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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

public class SzalBeliara extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	private DustOptions dust1 = new DustOptions(Color.fromRGB(224, 32, 32), 0.8f);
	private DustOptions dust2 = new DustOptions(Color.fromRGB(224, 32, 32), 0.5f);
		
	public SzalBeliara(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.71, -0.71, -0.71, 
				0.71, 0.71, 0.71);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		MutableDouble damage = new MutableDouble(rune.getDamage());
		
		MultipleProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.55, 
				2,
				1,
				30, 
				0,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_SPIDER_DEATH, 1.2f, 0.5f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 4, 
							0.1f, 0.1f, 0.1f, 0.33f, dust1);
					loc.getWorld().spawnParticle(Particle.DUST, loc, 4, 
							0f, 0f, 0f, 0.15f, dust2);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune, damage.doubleValue())) {
						e.getWorld().playSound(loc, Sound.ENTITY_SKELETON_HORSE_HURT, 0.9f, 0.8f);

						loc.getWorld().spawnParticle(Particle.DUST, loc, 15, 
								0.4f, 0.4f, 0.4f, 0.18f, dust1);
						
						damage.setValue(damage.doubleValue() * 0.8);
					}
				}, 
				loc -> { });
	}

}
