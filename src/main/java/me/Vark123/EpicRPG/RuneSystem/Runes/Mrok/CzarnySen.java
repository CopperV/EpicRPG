package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.StunRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class CzarnySen extends ACastableRune {
	
	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;

	public CzarnySen(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.1, 0);
		int points = (int) (4 * rune.getObszar());
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				rune.getObszar(),
				4,
				rune.getDurationTime(),
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_AMBIENT, 1.75f, 0.6f);
				},
				loc -> {
					for(int i = 0; i < points; i++) {
						double angle = rand.nextDouble(0, Math.PI*2);
						double x = rune.getObszar() * Math.sin(angle);
						double z = rune.getObszar() * Math.cos(angle);
						
						Location point = loc.clone().add(x,0,z);
						point.getWorld().spawnParticle(Particle.LARGE_SMOKE, point, 1, 0, 0, 0, 0);
						point.getWorld().spawnParticle(Particle.SMOKE, point, 0, 0, 1, 0, rand.nextFloat(0.1f, 0.2f));
					}
				}, 
				(_p, _v) -> { return false; },
				(_l, _v) -> { },
				source -> {
					InstantRangeRuneTemplate.castEffect(
							this, 
							source,
							rune.getObszar(),
							0,
							0,
							loc -> {
								loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, 1f);
								loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .9f);
								loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .8f);
								loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .7f);
								loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .6f);
								loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .5f);
								
								WaveRuneTemplate.castWave(
										this,
										loc,
										rune.getObszar(),
										4.,
										1.,
										0,
										1,
										__ -> { },
										_point -> {
											_point.getWorld().spawnParticle(Particle.LARGE_SMOKE, _point, 3, 0.2f, 0.8f, 0.2f, 0.04f);
										},
										(_p, _v) -> { return false; },
										(_l, _v) -> { },
										__ -> { });
							}, 
							hitCondition, 
							(loc, entity) -> {
								if(RuneUtils.damage(player, entity, rune)) {
									StunRuneTemplate.castEffect(this, entity);
									
									AbstractEntity aEntity = BukkitAdapter.adapt(entity);
									boolean flag = aEntity.hasAI();
									
									TimingEffectRuneTemplate.castEffect(
											this,
											entity,
											victim -> {
												if(flag)
													aEntity.setAI(false);
											},
											victim -> {
												if(flag)
													aEntity.setAI(true);
											},
											new TimingRuneEffect(
													4,
													victim -> {
														Location vLoc = BukkitAdapter.adapt(aEntity.getEyeLocation());
														vLoc.getWorld().spawnParticle(Particle.SMOKE, vLoc, 4, 0.2f, 0.4f, 0.2f, 0.03f);
													})
											);
								}
							}
					);
				});
	}

}
