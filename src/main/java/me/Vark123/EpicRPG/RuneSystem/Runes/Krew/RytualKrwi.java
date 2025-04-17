package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class RytualKrwi extends ACastableRune {

	private static final Random rand = new Random();
	private static final DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 0), 0.6f);

	private IRuneHitCondition hitCondition;
	
	public RytualKrwi(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.RYTUAL_KRWI,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.2f, 0.8f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 0.05, 0);

					_loc.getWorld().spawnParticle(Particle.DUST, _loc, 7,
							0.4, 0.9, 0.4, 0.25, dust);
				}));
	}
	
	public void castEffect() {
		Location startLoc = player.getLocation().clone().add(0, 0.5, 0);
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				0,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_MAGMA_CUBE_DEATH, 1.25f, 0.5f);
					
					double radius = rune.getObszar();
					double points = Math.pow(rune.getObszar(), 3) * 0.5;
					for(int i = 0; i < points; ++i) {
						double r = rand.nextDouble(radius);
						double angle1 = rand.nextDouble(Math.PI * 2);
						double angle2 = rand.nextDouble(Math.PI * 2);
						
						double x = r * Math.cos(angle1) * Math.sin(angle2);
						double y = r * Math.sin(angle1);
						double z = r * Math.cos(angle1) * Math.cos(angle2);
						Location pos = loc.clone().add(x, y, z);
						loc.getWorld().spawnParticle(Particle.DUST, pos, 1,
								0, 0, 0, rand.nextDouble(), new DustOptions(Color.fromRGB(192, 0, 0), rand.nextFloat(1f, 3.5f)));
					}
					for(int i = 0; i < points; ++i) {
						double r = rand.nextDouble(radius);
						double angle1 = rand.nextDouble(Math.PI * 2);
						double angle2 = rand.nextDouble(Math.PI * 2);
						
						double x = r * Math.cos(angle1) * Math.sin(angle2);
						double y = r * Math.sin(angle1);
						double z = r * Math.cos(angle1) * Math.cos(angle2);
						Location pos = loc.clone().add(x, y, z);
						loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, pos, 1,
								0, 0, 0, rand.nextDouble(0.2f, 0.8f), Color.fromRGB(192, 0, 0));
					}
				},
				hitCondition,
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune);
				});
	}

}
