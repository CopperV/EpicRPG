package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageBurnEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BombRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.RainRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.EmptyEntityRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class Kataklizm extends ACastableRune {
	
	private Random rand = new Random();
	private IRuneHitCondition hitCondition;

	public Kataklizm(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		switch(rand.nextInt(6)) {
			case 0:
				effect1();
				break;
			case 1:
				effect2();
				break;
			case 2:
				effect3();
				break;
			case 3:
				effect4();
				break;
			case 4:
				effect5();
				break;
			case 5:
				effect6();
				break;
		}
	}
	
	//DESZCZ METEORYTOW
	private void effect1() {
		ACastableRune castableRune = this;
		double radius = rune.getObszar();
		double hitRadius = rune.getObszar() * 0.4;
		double time = rune.getDurationTime() * 0.5;
		double damage = rune.getDamage() * 1.5;
		IRunePostDamageEffect hitEffect = new DamageBurnEffect(7, damage * 0.12, this);
		
		Block block = player.getTargetBlock(new HashSet<>(Arrays.asList(Material.AIR, Material.WATER, Material.LAVA)), (int) (rune.getObszar()*2));
		Location hit = block.getLocation().clone().add(0, 0.1, 0);
		
		double height = 20+hit.distance(castLoc);
		Location origin = castLoc.clone().add(0, height, 0);
		Vector vec = new Vector(
				hit.getX() - origin.getX(),
				hit.getY() - origin.getY(),
				hit.getZ() - origin.getZ())
				.normalize();
		
		RainRuneTemplate.castRain(
				castableRune,
				castLoc,
				radius,
				time,
				0.5,
				1, 
				5, 
				1,
				1,
				(loc1, loc2) -> vec,
				height,
				height + 15,
				new BoundingBox(),
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_PORTAL_TRAVEL, 2, 0.8f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 8, 0.4f, 0.4f, 0.4f, 0.1f);
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 14, 0.5f, 0.5f, 0.5f, 0.1f);
				}, 
				new EmptyEntityRuneCondition(),
				(__, ___) -> { },
				loc -> { },
				loc -> { },
				loc -> {
					WaveRuneTemplate.castWave(
							castableRune,
							loc,
							hitRadius,
							4,
							0.33,
							1,
							1,
							__ -> { },
							_loc -> {
								_loc.getWorld().playSound(_loc, Sound.ENTITY_GENERIC_EXPLODE, rand.nextFloat(0.3f, 0.75f), rand.nextFloat(0.75f, 1.25f));
								_loc.getWorld().spawnParticle(Particle.EXPLOSION, _loc, 1, 0.1f, 0.1f, 0.1f, 0.1f);
							},
							hitCondition,
							(_loc, entity) -> {
								if(RuneUtils.damage(player, entity, rune, damage, hitEffect))
									_loc.getWorld().playSound(_loc, Sound.BLOCK_FIRE_EXTINGUISH, 0.6f, 1);
							},
							__ -> { });
				});
	}
	
	//CZARNA DZIURA
	private void effect2() {
		ACastableRune castableRune = this;
		DustOptions dust = new DustOptions(Color.BLACK, 1.7f);
		
		double radius = rune.getObszar() * 0.6;
		double pullRadius = rune.getObszar() * 1.5;
		int time = (int) (rune.getDurationTime() * 1.5);
		double damage = rune.getDamage() * 0.15;
		
		MutableDouble theta = new MutableDouble(0);
		int sinAmount = 6;
		double sinStep = (Math.PI * 2) / (double) sinAmount;
		double thetaStep = (Math.PI * 2) / 256;
		
		TotemRuneTemplate.castTotem(
				castableRune,
				castLoc.clone().add(0, 0.1, 0),
				time,
				radius,
				2,
				20, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ALLAY_DEATH, 1.2f, 0.4f);
				}, 
				loc -> {
					for(int i = 0; i < sinAmount; ++i) {
						double thetaAngle = theta.doubleValue() + sinStep*i;
						
						for(double t = 0; t <= radius; t+=0.5) {
							double percent = t/radius * Math.PI;
							double x = t * Math.sin(thetaAngle - percent);
							double z = t * Math.cos(thetaAngle - percent);
							
							Location tmp = loc.clone().add(x,0,z);
							tmp.getWorld().spawnParticle(Particle.DUST, tmp, 1, 0.1f, 0.1f, 0.1f, 0, dust);
						}
					}
					theta.add(thetaStep);
					
					InstantRangeRuneTemplate.castEffect(
							castableRune,
							loc, 
							pullRadius,
							0,
							0,
							__ -> { },
							hitCondition,
							(__, entity) -> {
								Location eLoc = entity.getLocation();
								Vector vec = new Vector(
										loc.getX() - eLoc.getX(),
										-1,
										loc.getZ() - eLoc.getZ())
										.normalize()
										.multiply(0.8 / eLoc.distance(loc));
								entity.setVelocity(vec);
							});
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune, damage)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_PHANTOM_HURT, rand.nextFloat(0.4f, 0.6f), rand.nextFloat(0.55f, 0.85f));
					}
				},
				loc -> { });
		
	}
	
	//BURZA Z PIORUNAMI
	private void effect3() {
		double radius = rune.getObszar() * 1.25;
		double time = rune.getDurationTime() * 1.75;
		double damage = rune.getDamage() * 0.3;
		
		new BukkitRunnable() {
			int timer = (int) time;
			@Override
			public void run() {
				if(timer < 0 || !casterInCastWorld()) {
					cancel();
					return;
				}
				--timer;

				castLoc.getWorld().getNearbyEntities(castLoc, radius, radius, radius, entity -> {
					if(entity.getLocation().distanceSquared(castLoc) > radius * radius)
						return false;
					
					if(!(entity instanceof LivingEntity))
						return false;
					
					LivingEntity le = (LivingEntity) entity;
					if(hitCondition != null)
						return hitCondition.check(player, le);
					return true;
				}).stream().collect(Collectors.collectingAndThen(
						Collectors.toList(),
						list -> {
							Collections.shuffle(list);
							return list.stream().limit(2).collect(Collectors.toList());
						})
				).forEach(entity -> {
					if(RuneUtils.damage(player, (LivingEntity) entity, rune, damage)) {
						entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, rand.nextFloat(1, 1.5f), rand.nextFloat(0.7f, 1.1f));
						
						Location start = entity.getLocation();
						Location current = start.clone();
						double targetY = start.getY() + 30;
						
						while (current.getY() < targetY) {
						    double offsetX = (rand.nextDouble() - 0.5) * 3;
						    double offsetZ = (rand.nextDouble() - 0.5) * 3;
						    
						    // ensure we don’t overshoot the target
						    double remainingY = targetY - current.getY();
						    double offsetY = Math.min(1.5 + rand.nextDouble() * 1.5, remainingY);

						    Location next = current.clone().add(offsetX, offsetY, offsetZ);

						    Utils.drawLine(
						        Particle.ELECTRIC_SPARK,
						        current,
						        next,
						        0.1,    // spacing
						        2,      // count
						        0.05f,  // x offset
						        0.05f,  // y offset
						        0.05f,  // z offset
						        0.03f   // speed
						    );

						    current = next;
						}
					}
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		TotemRuneTemplate.castTotem(
				this, 
				castLoc,
				(int) time,
				radius,
				1,
				4,
				loc -> {
					loc.getWorld().playSound(loc, Sound.WEATHER_RAIN_ABOVE, 3f, 0.85f);
					new BukkitRunnable() {
						int timer = (int) time;
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(timer <= 0 || !casterInCastWorld()) {
								this.cancel();
								return;
							}
							--timer;

							float vol = 1.5f + rand.nextFloat(1.5f);
							float pitch = 0.6f + rand.nextFloat(0.3f);
							loc.getWorld().playSound(loc, Sound.WEATHER_RAIN_ABOVE, vol, pitch);
						}
					}.runTaskTimer(Main.getInstance(), 0, 20);
				},
				loc -> {
					Location effectLoc = loc.clone().add(0,30,0);
					
					for(int i = 0; i < radius * 6; ++i) {
						double r = rand.nextDouble(radius);
						double angle = rand.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double y = rand.nextDouble(-0.1, 0.1);
						double z = r *  Math.cos(angle);
						Location pos = effectLoc.clone().add(x, y, z);
						
						loc.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, pos, 1,
								0, 0, 0, rand.nextDouble(0, 0.05));
					}
					for(int i = 0; i < radius * 6; ++i) {
						double r = rand.nextDouble(radius);
						double angle = rand.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double y = rand.nextDouble(-0.1, 0.1);
						double z = r *  Math.cos(angle);
						Location pos = effectLoc.clone().add(x, y, z);
						
						loc.getWorld().spawnParticle(Particle.WHITE_SMOKE, pos, 1,
								0, 0, 0, rand.nextDouble(0, 0.06));
					}
					for(int i = 0; i < radius * 6; ++i) {
						double r = rand.nextDouble(radius);
						double angle = rand.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double y = rand.nextDouble(-0.1, 0.1);
						double z = r *  Math.cos(angle);
						Location pos = effectLoc.clone().add(x, y, z);
						
						loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, pos, 1,
								0, 0, 0, rand.nextDouble(0, 0.02));
					}
					for(int i = 0; i < radius * 9; ++i) {
						double r = rand.nextDouble(radius);
						double angle = rand.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double y = rand.nextDouble(-0.1, 0.1);
						double z = r *  Math.cos(angle);
						Location pos = effectLoc.clone().add(x, y, z);
						
						loc.getWorld().spawnParticle(Particle.FALLING_WATER, pos, 1,
								0, 0, 0, rand.nextDouble(0, 0.05));
					}
				}, 
				new EmptyEntityRuneCondition(),
				(__, ___) -> { },
				loc -> { });
	}
	
	//FALA PLOMIENI
	private void effect4() {
		double radius = rune.getObszar() * 2;
		double damage = rune.getDamage() * 0.7;
		IRunePostDamageEffect hitEffect = new DamageBurnEffect(14, damage * 0.12, this);
		
		Location startLoc = castLoc.clone().add(0,1,0);
		WaveRuneTemplate.castWave(
				this,
				startLoc,
				radius,
				6,
				0.22,
				1,
				1,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1.2f, 0.9f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 4,
							0.1, 1, 0.1, 0.03);
					loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc, 2,
							0.1, 1, 0.1, 0.02);
					loc.getWorld().spawnParticle(Particle.LAVA, loc, 1,
							0.1, 1, 0.1, 0.01);
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune, damage, hitEffect)) {
						loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 0.6f, 0.6f);
					}
				}, 
				loc -> { });
	}
	
	//TORNADA
	private void effect5() {
		ACastableRune castableRune = this;
		
		double radius = rune.getObszar() * 0.6;
		double time = rune.getDurationTime() * 1.8;
		double damage = rune.getDamage() * 0.8;
		int amount = 4;
		
		MutableDouble theta = new MutableDouble(rand.nextDouble(0, Math.PI * 2));
		double thetaStep = (2*Math.PI) / 256.;
		double angleStep = (2*Math.PI) / (double) amount;
		
		Location startLoc = castLoc.clone().add(0, 0.05, 0);
		
		new BukkitRunnable() {
			Collection<UUID> cooldowns = new HashSet<>();
			int timer = (int) (time * 20);
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0 || !casterInCastWorld()) {
					cancel();
					return;
				}
				--timer;
				
				for(int i = 0; i < amount; ++i) {
					double angle = theta.doubleValue() + i*angleStep;
					double x = radius * Math.sin(angle);
					double z = radius * Math.cos(angle);
					
					Location _loc = startLoc.clone().add(x,0,z);
					
					_loc.getWorld().playSound(_loc, Sound.ENTITY_BREEZE_CHARGE, rand.nextFloat(0.3f, 0.75f), rand.nextFloat(0.35f, 0.65f));
					for(double y = 0; y <= 3; y += 0.5) {
						double r = y + 0.25;
						double step = (Math.PI*2) / (r * 2);
						for(double alfa = 0; alfa < (Math.PI*2); alfa += step) {
							double _x = r * Math.sin(angle + alfa);
							double _z = r * Math.cos(angle + alfa);
							Location tmp = _loc.clone().add(_x,y,_z);
							tmp.getWorld().spawnParticle(Particle.WHITE_SMOKE, tmp, 1, 0.05f, 0.05f, 0.05f, 0.03f);
						}
					}
					
					InstantRangeRuneTemplate.castEffect(
							castableRune,
							_loc,
							1.5,
							0,
							0,
							__ -> { },
							hitCondition,
							(__, entity) -> {
								UUID uid = entity.getUniqueId();
								if(cooldowns.contains(uid))
									return;
								if(RuneUtils.damage(player, entity, rune, damage)) {
									Location eLoc = entity.getLocation();
									eLoc.getWorld().playSound(eLoc, Sound.ENTITY_ENDER_DRAGON_FLAP, 0.4f, rand.nextFloat(1.1f, 1.3f));
									
									Vector vec = new Vector(
											_loc.getX() - eLoc.getX(),
											0,
											_loc.getZ() - eLoc.getZ())
											.normalize()
											.setY(0.3)
											.multiply(3);
									entity.setVelocity(vec);
									
									cooldowns.add(uid);
									new BukkitRunnable() {
										@Override
										public void run() {
											cooldowns.remove(uid);
										}
									}.runTaskLater(Main.getInstance(), 10);
								}
							});
				}
				theta.add(thetaStep);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	//ERUPCJA
	private void effect6() {
		ACastableRune castableRune = this;
		Location startLoc = castLoc.clone().add(0, 0.15, 0);
		BoundingBox boundingBox = new BoundingBox(
				-0.4, -0.4, -0.4, 
				0.4, 0.4, 0.4);
		
		int amount = 5;
		double radius = rune.getObszar() * 0.5;
		double time = rune.getDurationTime() * 1.4;
		double damage = rune.getDamage() * 0.6;
		IRunePostDamageEffect hitEffect = new DamageBurnEffect(9, damage * 0.12, this);
		
		for(int i = 0; i < amount; ++i) {
			double angle = rand.nextDouble(Math.PI * 2);
			double r = rand.nextDouble(radius);
			double x = r * Math.sin(angle);
			double z = r * Math.cos(angle);
			
			Location castLoc = startLoc.clone().add(x, 0, z);
			while(castLoc.getBlock().getType().isSolid())
				castLoc.add(0,1,0);
			
			TotemRuneTemplate.castTotem(
					castableRune, 
					castLoc,
					(int) time,
					0,
					4,
					1,
					loc -> {
						loc.getWorld().playSound(loc, Sound.BLOCK_LAVA_POP, 1.5f, rand.nextFloat(1, 1.4f));
					},
					loc -> {
						loc.getWorld().spawnParticle(Particle.LAVA, loc, 3, 0.1f, 0.1f, 0.1f, 0.04f);
						loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc, 2, 0.12f, 0.12f, 0.12f, 0.06f);
					},
					new EmptyEntityRuneCondition(),
					(__, ___) -> { },
					loc -> { });
			
			TotemRuneTemplate.castTotem(
					castableRune, 
					castLoc,
					(int) time,
					0,
					20,
					1,
					loc -> { },
					loc -> {
						double angle1 = rand.nextDouble(Math.PI * 2);
						double angle2 = Math.toRadians(rand.nextDouble(30, 60));
					
						double _x = Math.sin(angle1);
						double _z = Math.cos(angle1);
						Vector horizontal = new Vector(_x, 0, _z).normalize().multiply(Math.cos(angle2));
						double vertical = Math.sin(angle2);
						
						Vector finalVector = new Vector(horizontal.getX(), vertical, horizontal.getY()).normalize();
						
						BombRuneTemplate.castBomb(
								castableRune, 
								loc, 
								finalVector,
								rand.nextDouble(0.5, 0.7),
								rand.nextDouble(0.04, 0.08),
								1,
								1,
								50,
								boundingBox,
								__ -> { },
								_loc -> {
									_loc.getWorld().spawnParticle(Particle.LAVA, _loc, 2, 0.06f, 0.06f, 0.06f, 0.02f);
									_loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, _loc, 3, 0.06f, 0.06f, 0.06f, 0.03f);
								},
								hitCondition,
								(_loc, entity) -> {
									if(RuneUtils.damage(player, entity, rune, damage, hitEffect)) {
										_loc.getWorld().playSound(_loc, Sound.BLOCK_FIRE_EXTINGUISH, 0.6f, 0.75f);
									}
								},
								__ -> { });
					},
					new EmptyEntityRuneCondition(),
					(__, ___) -> { },
					loc -> { });
		}
	}

}
