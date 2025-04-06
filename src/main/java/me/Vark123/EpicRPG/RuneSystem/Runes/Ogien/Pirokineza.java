package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageBurnEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.StunRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Pirokineza extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;
	public Pirokineza(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.7, -0.7, -0.7, 
				0.7, 0.7, 0.7);
		
		hitEffect = new DamageBurnEffect(rune.getDurationTime(), rune.getDamage(), this);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.3, 
				2,
				1,
				30, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 0.95f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc, 6, 
							0.2f, 0.2f, 0.2f, 0.03f);
				}, 
				hitCondition, 
				(loc, e) -> {
					e.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1.1f);
					
					RuneUtils.damage(player, e, rune, hitEffect);
					StunRuneTemplate.castEffect(this, e);
				}, 
				loc -> { });
	}

}
