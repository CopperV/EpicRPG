package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;

public class TajemnyBlask_M extends ACastableRune {

	private static final Collection<UUID> debuff = new HashSet<>();
	private static final DustOptions dust = new DustOptions(Color.RED, 1.25f);
	
	private Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	public TajemnyBlask_M(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		Location startLoc = castLoc.clone().add(0, 0.1, 0);
		
		InstantRangeRuneTemplate.castEffect(
				this, 
				startLoc,
				rune.getObszar(),
				0,
				0, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 2.5f, 0.8f);
				}, 
				hitCondition,
				(loc, entity) -> {
					if(!(entity instanceof Player))
						return;
					if(debuff.contains(entity.getUniqueId())) {
						player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 1.5f, 0.8f);
						player.getWorld().spawnParticle(Particle.SMOKE, player.getLocation(), 10, 0.4f, 0.8f, 0.4f, 0.04f);
						return;
					}
					
					UUID uid = entity.getUniqueId();
					debuff.add(uid);
					new BukkitRunnable() {
						@Override
						public void run() {
							Player player = Bukkit.getPlayer(uid);
							debuff.remove(uid);
							if(player == null || !player.isOnline())
								return;

							player.sendMessage(Main.getInstance().getPrefix()+" §aDebuff runy "+rune.getName()+" skonczyl sie");
							player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 1.5f, 1.5f);
							player.getWorld().spawnParticle(Particle.DUST, player.getLocation(), 10, 0.4f, 0.8f, 0.4f, 0.1f, dust);
						}
					}.runTaskLater(Main.getInstance(), 20*60*15);
					
					BufferRuneTemplate.castEffect(
							castableRune, 
							rune.getName(),
							EpicModifierTypes.TAJEMNY_BLASK_M,
							entity,
							target -> {
								target.getWorld().spawnParticle(Particle.FLASH, target.getLocation().clone().add(0,1,0), 1,
										0,0,0,0);
							},
							target -> {
								target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0.8f);

								Location _loc = target.getLocation().clone().add(0, 1, 0);

								double force = rand.nextDouble(0.01, 0.05);
								loc.getWorld().spawnParticle(Particle.SMOKE, _loc, 6, 0.4f, 0.8f, 0.4f, force);
							},
							new TimingRuneEffect(4, target -> {
								Location _loc = target.getLocation().clone().add(0, 1, 0);

								double force = rand.nextDouble(0, 0.03);
								loc.getWorld().spawnParticle(Particle.END_ROD, _loc, 4, 0.4f, 0.9f, 0.4f, force);
							}));
				});
	}

}
