package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

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
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MultipleProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class WloczniaCiemnosci extends ACastableRune {

	private DustOptions dust = new DustOptions(Color.PURPLE, 2f);
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public WloczniaCiemnosci(RpgPlayer rpgPlayer, EpicRune rune) {
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
		
		MultipleProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.55, 
				2,
				1,
				30, 
				7,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_STRAY_HURT, 1.2f, 0.4f);
				}, 
				loc -> {
					Location pos = loc.clone();
					Vector step = pos.getDirection().normalize().multiply(-0.1);
					
					for(int i = 0; i < 5; ++i) {
						pos.getWorld().spawnParticle(Particle.LARGE_SMOKE, pos, 2, 
								0.03f, 0.03f, 0.03f, 0.03f);
						pos.getWorld().spawnParticle(Particle.DUST, pos, 5, 
								0.3f, 0.3f, 0.3f, 0.1f, dust);
						pos.add(step);
					}
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_SKELETON_HORSE_DEATH, 0.9f, 1.4f);

						loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 8, 
								0.1f, 0.9f, 0.1f, 0.08f);
						loc.getWorld().spawnParticle(Particle.DUST, loc, 18, 
								0.7f, 0.7f, 0.7f, 0.5f, dust);
					}
				}, 
				loc -> { });
	}

}
