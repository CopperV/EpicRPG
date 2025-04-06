package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class UkazanieSmierci extends ACastableRune {

	private static Random rand = new Random();
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public UkazanieSmierci(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.8, -0.8, -0.8, 
				0.8, 0.8, 0.8);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.4, 
				3,
				1,
				40, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_CAST_SPELL, 1.2f, 0.7f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 6, 
							0.3f, 0.3f, 0.3f, 0.02f);
					loc.getWorld().spawnParticle(Particle.RAID_OMEN, loc, 9, 
							0.5f, 0.5f, 0.5f, 0.03f);
				}, 
				hitCondition, 
				(loc, e) -> {
					String name = e.getName().toLowerCase();
					if(name.contains("legendary") || name.contains("legendarny")) {
						if(RuneUtils.damage(player, e, rune)) {
							e.getWorld().playSound(loc, Sound.ENTITY_ALLAY_HURT, 2f, 0f);

							loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 19, 
									0.7f, 0.7f, 0.7f, 0.07f);
							loc.getWorld().spawnParticle(Particle.TRIAL_OMEN, loc, 30, 
									1f, 1f, 1f, 0.04f);
						}
						return;
					}
					
					double percent = e.getHealth() / e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
					if(percent > 0.6) {
						if(RuneUtils.damage(player, e, rune)) {
							e.getWorld().playSound(loc, Sound.ENTITY_ALLAY_HURT, 2f, 0f);

							loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 19, 
									0.7f, 0.7f, 0.7f, 0.07f);
							loc.getWorld().spawnParticle(Particle.TRIAL_OMEN, loc, 30, 
									1f, 1f, 1f, 0.04f);
						}
						return;
					}
					
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
					double chance = rpg.getInfo().getLevel() * 0.075 * 0.01;
					if(rand.nextDouble() > chance) {
						if(RuneUtils.damage(player, e, rune)) {
							e.getWorld().playSound(loc, Sound.ENTITY_ALLAY_HURT, 2f, 0f);

							loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 19, 
									0.7f, 0.7f, 0.7f, 0.07f);
							loc.getWorld().spawnParticle(Particle.TRIAL_OMEN, loc, 30, 
									1f, 1f, 1f, 0.04f);
						}
						return;
					}
					
					if(RuneUtils.damage(player, e, rune, e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 3)) {
						e.getWorld().playSound(loc, Sound.ENTITY_ALLAY_DEATH, 2f, 0f);

						loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 19, 
								0.7f, 0.7f, 0.7f, 0.07f);
						loc.getWorld().spawnParticle(Particle.RAID_OMEN, loc, 30, 
								1f, 1f, 1f, 0.04f);
					}
				}, 
				loc -> { });
	}

}
