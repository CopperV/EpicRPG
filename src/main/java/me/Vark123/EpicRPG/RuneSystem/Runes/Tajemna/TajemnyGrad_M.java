package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import java.util.Arrays;
import java.util.HashSet;

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
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.RainRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class TajemnyGrad_M extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	public TajemnyGrad_M(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.5, -0.5, -0.5, 
				0.5, 0.5, 0.5);
	}

	@Override
	public void castSpell() {
		Block block = player.getTargetBlock(new HashSet<>(Arrays.asList(Material.AIR, Material.WATER, Material.LAVA)), (int) (rune.getObszar()*2));
		Location hit = block.getLocation().clone().add(0, 0.1, 0);
		
		double height = 20+hit.distance(player.getLocation());
		Location origin = player.getLocation().clone().add(0, height, 0);
		Vector vec = new Vector(
				hit.getX() - origin.getX(),
				hit.getY() - origin.getY(),
				hit.getZ() - origin.getZ())
				.normalize();
		
		
		RainRuneTemplate.castRain(
				this,
				castLoc,
				rune.getObszar(),
				rune.getDurationTime(),
				0.47, 
				2,
				1, 
				3,
				1,
				(loc1, loc2) -> {
					return vec;
				}, 
				height, 
				height + 15,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.2f, 1.2f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.WITCH, loc, 8, 
							0.15f, 0.15f, 0.15f, 0.02f);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 0.9f, 1);
						loc.getWorld().spawnParticle(Particle.REVERSE_PORTAL, loc, 14, 
								0.4f, 0.4f, 0.4f, 1.5f);
					}
				}, 
				loc -> { },
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_FALL, 2.2f, 0.4f);
				},
				loc -> { });
	}

}
