package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Sound;
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

public class KrwawyPocisk extends ACastableRune {

	private static final DustOptions dust = new DustOptions(Color.fromRGB(138, 3, 3), 1.25f);
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public KrwawyPocisk(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.52, -0.52, -0.52, 
				0.52, 0.52, 0.52);
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
						0.28, 
						3,
						1,
						40, 
						boundingBox,
						loc -> {
							loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1.1f);
						}, 
						loc -> {
							loc.getWorld().spawnParticle(Particle.DUST, loc, 4, 
									0.08f, 0.08f, 0.08f, 0.15f, dust);
						}, 
						hitCondition, 
						(loc, e) -> {
							if(RuneUtils.damage(player, e, rune)) {
								e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 0.8f);
								e.getWorld().spawnParticle(Particle.DUST, e.getLocation().clone().add(0,1,0), 12, 
										0.4f, 0.5f, 0.4f, 0.4f, dust);
							}
						}, 
						loc -> { });
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}

}
