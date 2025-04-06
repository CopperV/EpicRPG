package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class MagicznaIskra extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	private DustOptions dust = new DustOptions(Color.PURPLE, 0.4f);
	private BlockData particleBlockData = Material.AMETHYST_BLOCK.createBlockData();
	
	public MagicznaIskra(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.6, -0.6, -0.6, 
				0.6, 0.6, 0.6);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.5, 
				3,
				1,
				30, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 1.2f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 8, 
							0.15f, 0.15f, 0.15f, 0.04f, dust);
					loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 8, 
							0.15f, 0.15f, 0.15f, 0.04f, particleBlockData);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune))
						loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1.3f);
				}, 
				loc -> { });
	}

}
