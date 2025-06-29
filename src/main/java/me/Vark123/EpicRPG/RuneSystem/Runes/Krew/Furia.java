package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
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

public class Furia extends ACastableRune {

	private static final Random rand = new Random();
	private static final double red = 138./255.;
	private static final double green = 3./255.;
	private static final double blue = 3./255.;
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public Furia(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.45, -0.45, -0.45, 
				0.45, 0.45, 0.45);
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		new BukkitRunnable() {
			int amount = 3;
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
						0.25, 
						3,
						1,
						30, 
						boundingBox,
						loc -> {
							loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 1, 1f);
						}, 
						loc -> {
							for(int i = 0; i < 12; ++i) {
								double x = rand.nextDouble(0.8) - 0.4;
								double y = rand.nextDouble(0.8) - 0.4;
								double z = rand.nextDouble(0.8) - 0.4;
								Location tmp = loc.clone().add(x,y,z);
								tmp.getWorld().spawnParticle(Particle.ENTITY_EFFECT, tmp, 0, red, green, blue, 1);
							}
						}, 
						hitCondition, 
						(loc, e) -> {
							if(RuneUtils.damage(player, e, rune)) {
								e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 0.66f);
								for(int i = 0; i < 12; ++i) {
									double x = rand.nextDouble(1.2) - 0.6;
									double y = rand.nextDouble(1.2) - 0.6;
									double z = rand.nextDouble(1.2) - 0.6;
									Location tmp = loc.clone().add(x,y,z);
									tmp.getWorld().spawnParticle(Particle.ENTITY_EFFECT, tmp, 0, red, green, blue, 1);
								}
							}
						}, 
						loc -> { });
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}

}
