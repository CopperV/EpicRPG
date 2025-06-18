package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageBurnEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class OgnistaFala extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private IRunePostDamageEffect hitEffect;

	public OgnistaFala(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		
		hitEffect = new DamageBurnEffect(10, rune.getDamage() * 0.12, this);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				6, 
				0.24, 
				1,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1.2f, 0.9f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 4,
							0.1, 1, 0.1, 0.03);
					loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc, 2,
							0.1, 1, 0.1, 0.02);
					loc.getWorld().spawnParticle(Particle.LAVA, loc, 1,
							0.1, 1, 0.1, 0.01);
				}, 
				hitCondition, 
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune, hitEffect);

					loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 0.6f, 1);
				},
				loc -> { });
	}

}
