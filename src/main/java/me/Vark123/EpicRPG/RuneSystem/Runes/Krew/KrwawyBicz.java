package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class KrwawyBicz extends ACastableRune {

	private static final DustOptions lineDust = new DustOptions(Color.fromRGB(138, 3, 3), 0.5f);
	private static final DustOptions cloudDust = new DustOptions(Color.fromRGB(138, 3, 3), 2.25f);
	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	
	public KrwawyBicz(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		TimingEffectRuneTemplate.castEffect(
				this, 
				player, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_BREEZE_CHARGE, 1f, 0.7f);
				}, 
				target -> { 
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location loc = target.getLocation().clone().add(0,3,0);
					for(int i = 0; i < 6; ++i) {
						double x = rand.nextDouble(1.5) - 0.75;
						double y = rand.nextDouble(0.8) - 0.4;
						double z = rand.nextDouble(1.5) - 0.75;
						float speed = rand.nextFloat(0.25f) + 0.1f;
						Location tmp = loc.clone().add(x,y,z);
						loc.getWorld().spawnParticle(Particle.DUST, tmp, 0, 0, 1, 0, speed, cloudDust);
					}
				}),
				new TimingRuneEffect(10, target -> {
					Location loc = target.getLocation().clone();
					double radius = rune.getObszar();
					loc.getWorld().getNearbyEntities(loc, radius, radius, radius, entity -> {
						if(entity.getLocation().distanceSquared(loc) > radius * radius)
							return false;
						
						if (!(entity instanceof LivingEntity))
							return false;

						LivingEntity le = (LivingEntity) entity;
						return hitCondition.check(player, le);
					}).stream().min((e1, e2) -> {
						double dist1 = e1.getLocation().distanceSquared(loc);
						double dist2 = e2.getLocation().distanceSquared(loc);
						if (dist1 == dist2)
							return 0;
						return dist1 < dist2 ? -1 : 1;
					}).map(e -> (LivingEntity) e).ifPresent(entity -> {
						if (RuneUtils.damage(player, entity, rune, rune.getDamage() * 0.5)) {
							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BREEZE_SHOOT, 1, 0.6f);
							Utils.drawLine(Particle.DUST, loc.clone().add(0,3,0), entity.getEyeLocation(), 
									0.1, 2, .1f, .1f, .1f, rand.nextFloat(0.1f, 0.35f), lineDust);
							
							Location loc1 = entity.getLocation();
							Vector vec = new Vector(
									loc1.getX() - loc.getX(),
									loc1.getY() - loc.getY(),
									loc1.getZ() - loc.getZ()
									)
									.normalize()
									.setY(0.2)
									.normalize()
									.multiply(0.8);
							entity.setVelocity(vec);
						}
					});
				}));
	}

}
