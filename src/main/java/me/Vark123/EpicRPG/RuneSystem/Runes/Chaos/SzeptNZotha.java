package me.Vark123.EpicRPG.RuneSystem.Runes.Chaos;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class SzeptNZotha extends ACastableRune {
	
	private static final DustOptions prepareDust = new DustOptions(Color.fromRGB(48, 25, 52), 1.2f);
	private static final DustOptions projectileDust = new DustOptions(Color.fromRGB(48, 25, 52), 0.25f);
	private static final Random random = new Random();

	private BoundingBox boundingBox;
	private IRuneHitCondition hitCondition;
	
	public SzeptNZotha(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.5, -0.5, -0.5, 
				0.5, 0.5, 0.5);
	}

	@Override
	public void castSpell() {
		Location effectLoc = player.getLocation().clone().add(0, 1, 0);
		Location startLoc = player.getLocation().clone().add(0, 0.1, 0);
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				3,
				rune.getObszar(),
				4,
				5,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 2.5f, 0.6f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 5, .3, .3, .3, .15, prepareDust);
					loc.getWorld().spawnParticle(Particle.PORTAL, loc, 15, .7, .7, .7, .5);
				},
				hitCondition,
				(loc, entity) -> {
					Location eLoc = entity.getLocation();
					Vector vec = new Vector(
							startLoc.getX() - eLoc.getX(),
							startLoc.getY() - eLoc.getY(),
							startLoc.getZ() - eLoc.getZ())
							.normalize()
							.multiply(0.1);
					entity.setVelocity(vec);
				},
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_HURT, 2.5f, 1.2f);
					for(int i = 0; i < 20; ++i) {
						double x = random.nextDouble(2) - 1;
						double y = random.nextDouble(.4) - .2;
						double z = random.nextDouble(2) - 1;
						Vector dir = new Vector(x,y,z).normalize();
						Vector projDir = dir.clone().multiply(0.2);
						
						ProjectileRuneTemplate.castProjectile(
								this, 
								effectLoc, 
								dir,
								0.4, 
								3,
								1,
								rune.getObszar(), 
								boundingBox,
								loc2 -> { }, 
								loc2 -> {
									Location tmp = loc2.clone();
									for(int j = 0; j < 4; ++j) {
										tmp.getWorld().spawnParticle(Particle.DUST, tmp, 3, .15, .15, .15, .03, projectileDust);
										tmp.add(projDir);
									}
								}, 
								hitCondition, 
								(loc2, e) -> {
									if(RuneUtils.damage(player, e, rune)) {
										e.getWorld().spawnParticle(Particle.REVERSE_PORTAL, e.getLocation().clone().add(0,1,0), 6,
												0.3f, 0.3f, 0.3f, 1.5f);
										e.getWorld().playSound(loc, Sound.ENTITY_GUARDIAN_DEATH, 0.9f, 1.2f);
									}
								}, 
								loc2 -> { });
					}
				});
	}

}
