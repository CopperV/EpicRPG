package me.Vark123.EpicRPG.RuneSystem.Runes.Chaos;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.StunRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Spetanie extends ACastableRune {
	
	private static final BlockData particleBlockData = Material.OBSIDIAN.createBlockData();
	
	private IRuneHitCondition hitCondition;
	private PotionEffect potion;

	public Spetanie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		
		potion = new PotionEffect(PotionEffectType.SLOWNESS, 20*rune.getDurationTime(), 1);
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		Location startLoc = player.getLocation().clone().add(0, 0.7, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				7, 
				0.24, 
				1,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.2f, 0.2f);
					player.addPotionEffect(potion);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 3,
							0.15, 0.7, 0.15, 0.02);
					loc.getWorld().spawnParticle(Particle.SMOKE, loc, 6,
							0.15, 0.7, 0.15, 0.02);
					loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 4,
							0.1, 0.7, 0.1, 0.05, particleBlockData);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						StunRuneTemplate.castEffect(this, entity);
						
						loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);

						double angle = Math.PI * 0.25;
						double radius = 0.75;
						double step = Math.PI * 0.0625;
						TimingEffectRuneTemplate.castEffect(
								castableRune,
								entity,
								__ -> { },
								__ -> { },
								new TimingRuneEffect(
										5,
										e -> {
											Location pos = e.getLocation().clone().add(0, 0.75, 0);
											for(double theta = 0; theta <= (Math.PI*2); theta = theta + step) {
												double x = radius * Math.cos(theta)*Math.cos(angle);
												double z = radius * Math.cos(theta)*Math.sin(angle);
												double y = radius * Math.sin(theta);
												pos.getWorld().spawnParticle(Particle.SMOKE, pos.clone().add(x, y, z), 1,
														0,0,0,0);
											}
											for(double theta = 0; theta <= (Math.PI*2); theta = theta + step) {
												double x = radius * Math.cos(theta)*Math.cos(-angle);
												double z = radius * Math.cos(theta)*Math.sin(-angle);
												double y = radius * Math.sin(theta);
												pos.getWorld().spawnParticle(Particle.SMOKE, pos.clone().add(x, y, z), 1,
														0,0,0,0);
											}
										})
								);
					}
				},
				loc -> { });
	}

}
