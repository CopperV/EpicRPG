package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MultipleProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class WloczniaElysian extends ACastableRune {

	private static final BlockData particleBlockData = Material.QUARTZ_BLOCK.createBlockData();
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public WloczniaElysian(RpgPlayer rpgPlayer, EpicRune rune) {
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
		
		MultipleProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.5, 
				3,
				1,
				40, 
				5,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_TRIDENT_THROW, 1.2f, 0.2f);
				}, 
				loc -> {
					Location pos = loc.clone();
					Vector step = pos.getDirection().normalize().multiply(-0.1);
					
					for(int i = 0; i < 5; ++i) {
						pos.getWorld().spawnParticle(Particle.END_ROD, pos, 3, 
								0.08f, 0.08f, 0.08f, 0.03f);
						pos.getWorld().spawnParticle(Particle.FALLING_DUST, pos, 2, 
								0.08f, 0.08f, 0.08f, 0.03f, particleBlockData);
						pos.add(step);
					}
				}, 
				hitCondition, 
				(loc, e) -> {
					e.getWorld().spawnParticle(Particle.END_ROD, e.getLocation().clone().add(0,1,0), 10, 
							0.4f, 0.4f, 0.4f, 0.06f);
					e.getWorld().spawnParticle(Particle.FALLING_DUST, e.getLocation().clone().add(0,1,0), 10, 
							0.5f, 0.5f, 0.5f, 0.08f, particleBlockData);
					e.getWorld().playSound(loc, Sound.ITEM_TRIDENT_THUNDER, 0.7f, 0.7f);
					
					RuneUtils.damage(player, e, rune);
				}, 
				loc -> { });
	}

}
