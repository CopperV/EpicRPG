package me.Vark123.EpicRPG.RuneSystem.Runes.Chaos;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class SzeptPrzedwiecznych extends ACastableRune {

	private static final DustOptions dust = new DustOptions(Color.PURPLE, 2.5f);
	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	public SzeptPrzedwiecznych(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Map<UUID, Integer> affected = new LinkedHashMap<>();
		
		Location startLoc = player.getLocation().clone().add(0, 0.1, 0);
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				rune.getObszar(),
				4,
				20,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1.5f, 0.7f);
				},
				loc -> {
					double radius = rune.getObszar();
					double angleStep = Math.PI / (rune.getObszar()*4);
					
					for(double theta = 0; theta < Math.PI * 2; theta += angleStep) {
						double x = radius * Math.sin(theta);
						double z = radius * Math.cos(theta);
						
						Location pos = loc.clone().add(x,0,z);
						Vector dir = new Vector(
								loc.getX() - pos.getX(),
								0,
								loc.getZ() - pos.getZ()
								).normalize().setY(0.5).normalize();
						loc.getWorld().spawnParticle(Particle.DUST, pos, 2,
								0.15, 0.05, 0.15, 0.04, dust);
						loc.getWorld().spawnParticle(Particle.DRAGON_BREATH, pos, 0,
								dir.getX(), dir.getY(), dir.getZ(), rand.nextDouble(0.04, 0.12));
					}
				},
				hitCondition,
				(loc, entity) -> {
					UUID uid = entity.getUniqueId();
					if(!affected.containsKey(uid))
						affected.put(uid, rand.nextInt(5));
					
					skillEffect(entity, startLoc, affected.get(uid));
				},
				loc -> { });
	}
	
	private void skillEffect(LivingEntity entity, Location source, int effect) {
		switch(effect) {
			case 0:
				RuneUtils.damage(player, entity, rune, rune.getDamage()*3);
				break;
			case 1:
				if(RuneUtils.damage(player, entity, rune)) {
					entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15, 1));
				}
				break;
			case 2:
				if(RuneUtils.damage(player, entity, rune)) {
					entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20*4, 2));
				}
				break;
			case 3:
				InstantRangeRuneTemplate.castEffect(
						this,
						source,
						2,
						0,
						0,
						loc -> {
							loc.getWorld().playSound(loc, Sound.ENTITY_GUARDIAN_DEATH, 1, 0.6f);
							
							DustOptions dust = new DustOptions(Color.PURPLE, 1);
							loc.getWorld().spawnParticle(Particle.DUST, loc, 20,
									1.5, 1.5, 1.5, 0.1, dust);
						}, 
						hitCondition,
						(loc, e) -> {
							RuneUtils.damage(player, e, rune, rune.getDamage()*0.25);
						});
				break;
			case 4:
				if(RuneUtils.damage(player, entity, rune)) {
					Location loc = entity.getLocation().clone();
					Vector dir = new Vector(
							loc.getX() - source.getX(),
							0,
							loc.getZ() - source.getZ()
							).normalize().setY(4).normalize().multiply(3);
					

					loc.getWorld().playSound(loc, Sound.ENTITY_GUARDIAN_HURT, 1, 0.6f);
					
					loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc.clone().add(0,1,0), 12,
							0.6, 0.6, 0.6, 0.06);
					entity.setVelocity(dir);
				}
				break;
		}
	}

}
