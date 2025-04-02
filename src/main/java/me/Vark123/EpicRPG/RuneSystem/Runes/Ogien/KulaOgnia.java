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
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class KulaOgnia extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;
	
	public KulaOgnia(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				0.75, 0.75, 0.75, 
				0.75, 0.75, 0.75);
		hitEffect = new DamageBurnEffect(6, rune.getDamage() * 0.1, this);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.35, 
				3,
				1,
				30, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 0.95f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 8, 
							0.25f, 0.25f, 0.25f, 0.01f);
				}, 
				hitCondition, 
				(loc, e) -> {
					RuneUtils.damage(player, e, rune, hitEffect);
					
					e.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1f);
				}, 
				loc -> { });
	}

}
