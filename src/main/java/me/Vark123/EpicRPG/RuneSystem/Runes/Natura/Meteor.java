package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.EmptyEntityRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Meteor extends ACastableRune {

	private Random rand = new Random();
	private IRuneHitCondition hitCondition;

	public Meteor(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		
		Block block = player.getTargetBlock(new HashSet<>(Arrays.asList(Material.AIR, Material.WATER, Material.LAVA)), (int) (rune.getObszar()*2));
		Location hit = block.getLocation().clone().add(0, 0.1, 0);
		
		double height = 30 + hit.distance(player.getLocation());
		Location origin = hit.clone().add(0, height, 0);
		
		ProjectileRuneTemplate.castProjectile(
				castableRune, 
				origin,
				new Vector(0, -1, 0),
				0.4,
				3,
				1,
				50,
				new BoundingBox(),
				loc -> {
					player.getWorld().playSound(player, Sound.ENTITY_GHAST_SHOOT, 1.5f, 0.7f);
				}, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, rand.nextFloat(0.4f, 0.8f), rand.nextFloat(0.7f, 1.3f));
					
					loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 8, 1, 1, 1, 0.4f);
					
					var loc2 = loc.clone().add(0,0.5,0);
					loc2.getWorld().spawnParticle(Particle.WHITE_SMOKE, loc2, 8, 0.1f, 0.6f, 0.1f, 0.04f);
				},
				new EmptyEntityRuneCondition(),
				(__, ___) -> { },
				loc -> {
					loc.add(0, 0.5, 0);
					
					WaveRuneTemplate.castWave(
							castableRune,
							loc,
							rune.getObszar(),
							6,
							0.33,
							1,
							1,
							_loc -> { },
							_loc -> {
								_loc.getWorld().playSound(_loc, Sound.ENTITY_GENERIC_EXPLODE, rand.nextFloat(0.3f, 0.7f), rand.nextFloat(0.8f, 1.5f));
								_loc.getWorld().spawnParticle(Particle.EXPLOSION, _loc, 2, 0.1f, 0.1f, 0.1f, 0.1f);
							}, 
							hitCondition,
							(_loc, entity) -> {
								RuneUtils.damage(player, entity, rune);
							}, 
							_loc -> { });
				});
		
	}

}
