package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BombRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.StunRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TrapRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Lowy_M extends ACastableRune {

	private static final DustOptions dust = new DustOptions(Color.fromRGB(0, 128, 0), 2);
	private static final Vector up = new Vector(0,1,0);
	
	private IRuneHitCondition hitCondition;
	private PotionEffect effect1;
	private PotionEffect effect2;
	private PotionEffect effect3;
	
	public Lowy_M(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		
		effect1 = new PotionEffect(PotionEffectType.SLOWNESS, 20*rune.getDurationTime(), 7);
		effect2 = new PotionEffect(PotionEffectType.WEAKNESS, 20*rune.getDurationTime(), 7);
		effect3 = new PotionEffect(PotionEffectType.BLINDNESS, 20*rune.getDurationTime(), 7);
	}

	@Override
	public void castSpell() {
		
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		Vector projDir = startLoc.getDirection().normalize();
		Vector projDir2 = up.clone().rotateAroundAxis(projDir.clone(), 180);
		
		castBomb(startLoc, projDir);
		castBomb(startLoc, projDir2);
		
		trapEffect(player.getLocation().clone().add(0, 0.1, 0));
	}
	
	private void castBomb(Location startLoc, Vector projDir) {
		BombRuneTemplate.castBomb(
				this, 
				startLoc, 
				projDir,
				0.6, 
				0.04,
				1,
				1,
				50, 
				null,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_CAST_SPELL, 1, 1.5f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 8, 
							0.15f, 0.15f, 0.15f, 0.06f, dust);
					loc.getWorld().spawnParticle(Particle.COMPOSTER, loc, 2, 
							0.05f, 0.05f, 0.05f, 0.01f);
				}, 
				hitCondition, 
				null, 
				loc -> {
					trapEffect(loc);
				});
	}
	
	private void trapEffect(Location loc) {
		TrapRuneTemplate.castTrap(
				this,
				loc,
				38*20,
				1,
				1.5,
				__ -> { },
				_loc -> {
					_loc.getWorld().spawnParticle(Particle.DUST, _loc, 6, 
							0.25f, 0.25f, 0.25f, 0.05f, dust);
					_loc.getWorld().spawnParticle(Particle.COMPOSTER, _loc, 1, 
							0.1f, 0.1f, 0.1f, 0.01f);
				},
				hitCondition, 
				(_loc, e) -> {
					_loc.getWorld().playSound(_loc, Sound.ENTITY_EVOKER_CAST_SPELL, 1, 0.5f);

					if(RuneUtils.damage(player, e, rune)) {
						StunRuneTemplate.castEffect(this, e);
						
						e.addPotionEffect(effect1);
						e.addPotionEffect(effect2);
						e.addPotionEffect(effect3);
						
						TimingEffectRuneTemplate.castEffect(
								this,
								e, 
								entity -> { },
								entity -> { },
								new TimingRuneEffect(
										4,
										entity -> {
											Location loc2 = entity.getLocation().clone().add(0,1,0);

											loc2.getWorld().spawnParticle(Particle.DUST, loc2, 8, 
													0.6f, 0.6f, 0.6f, 0.04f, dust);
											loc2.getWorld().spawnParticle(Particle.COMPOSTER, loc2, 2, 
													0.6f, 0.6f, 0.6f, 0.02f);
										}
										));
					}
				}, 
				__ -> { });
		
	}

}
