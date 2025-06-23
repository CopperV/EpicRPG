package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class UderzenieChi extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	private DustOptions dust = new DustOptions(Color.AQUA, 1f);
	
	public UderzenieChi(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.7, -0.7, -0.7, 
				0.7, 0.7, 0.7);
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		new BukkitRunnable() {
			int amount = 2;
			@Override
			public void run() {
				if(amount <= 0 || !casterInCastWorld()) {
					cancel();
					return;
				}
				--amount;
				
				Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
				
				ProjectileRuneTemplate.castProjectile(
						castableRune, 
						startLoc, 
						startLoc.getDirection().normalize(),
						0.8, 
						2,
						1,
						36, 
						boundingBox,
						loc -> {
							loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_CAST_SPELL, 1, 0.65f);
						}, 
						loc -> {
							loc.getWorld().spawnParticle(Particle.DUST, loc, 16, 
									0.3f, 0.3f, 0.3f, 0.06f, dust);
							loc.getWorld().spawnParticle(Particle.WHITE_SMOKE, loc, 7, 
									0.09f, 0.09f, 0.09f, 0.02f);
						}, 
						hitCondition, 
						(loc, e) -> {
							if(RuneUtils.damage(player, e, rune))
								loc.getWorld().playSound(loc, Sound.ENTITY_MAGMA_CUBE_JUMP, 1, 0.7f);
						}, 
						loc -> { });
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}

}
