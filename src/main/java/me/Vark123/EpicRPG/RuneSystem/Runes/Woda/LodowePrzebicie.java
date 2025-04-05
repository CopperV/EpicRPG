package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.AcceleratedProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class LodowePrzebicie extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private BlockData particleBlockData;
	
	public LodowePrzebicie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				0.85, 0.6, 0.85, 
				0.85, 0.6, 0.85);
		particleBlockData = Material.BLUE_ICE.createBlockData();
	}

	@Override
	public void castSpell() {
		InstantRangeRuneTemplate.castEffect(
				this, 
				player.getLocation(),
				rune.getObszar(),
				0,
				0,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1, 0.8f);
					loc.getWorld().spawnParticle(Particle.BLOCK, loc.clone().add(0,1,0), 15, 
							0.4, 0.8, 0.4, 0.03, particleBlockData);
				}, 
				hitCondition, 
				(loc, e) -> {
					spellEffect(e);
				});
	}
	
	private void spellEffect(Entity target) {
		Location startLoc = target.getLocation().clone().add(0, 25, 0);
		
		AcceleratedProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				new Vector(0, -1, 0),
				0.4, 
				1,
				1,
				40, 
				1.05,
				boundingBox,
				loc -> { }, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.BLOCK, loc, 6, 
							0.08f, 0.25f, 0.08f, 0.06f, particleBlockData);
				}, 
				hitCondition, 
				(loc, e) -> {
					e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 0.65f);
					
					RuneUtils.damage(player, e, rune);
				}, 
				loc -> { });
	}

}
