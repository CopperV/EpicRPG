package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;

public class Wtopienie_H extends ACastableRune {
	private Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	public Wtopienie_H(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		Location startLoc = castLoc.clone().add(0, 0.1, 0);
		
		InstantRangeRuneTemplate.castEffect(
				this, 
				startLoc,
				rune.getObszar(),
				0,
				0, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 2f, .8f);
				}, 
				hitCondition,
				(loc, entity) -> {
					BufferRuneTemplate.castEffect(
							castableRune, 
							rune.getName(),
							EpicModifierTypes.WTOPIENIE_H,
							entity,
							target -> {
								target.getWorld().playSound(target, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.75f, 0.65f);
								target.getWorld().spawnParticle(Particle.FLAME, target.getLocation().clone().add(0,1,0), 1);
							},
							target -> {
								target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0.8f);

								Location _loc = target.getLocation().clone().add(0, 1, 0);

								double force = rand.nextDouble(0.01, 0.05);
								loc.getWorld().spawnParticle(Particle.SMOKE, _loc, 6, 0.4f, 0.8f, 0.4f, force);
							},
							new TimingRuneEffect(3, target -> {
								Block block = target.getLocation().clone().subtract(0,1,0).getBlock();
								Material material = block == null || block.getType().equals(Material.AIR) ? Material.DIRT : block.getType();
								
								Location _loc = target.getLocation().clone().add(0, 1, 0);

								double force = rand.nextDouble(0.01, 0.1);
								loc.getWorld().spawnParticle(Particle.BLOCK, _loc, 8, 0.4f, 0.8f, 0.4f, force, Bukkit.createBlockData(material));
							}));
				});
	}

}
