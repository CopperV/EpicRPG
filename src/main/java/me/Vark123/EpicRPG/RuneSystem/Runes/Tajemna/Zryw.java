package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.lib.lang3.mutable.MutableBoolean;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneEffectType;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.RuneEffectRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;

public class Zryw extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private Random rand = new Random();

	public Zryw(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		Location startLoc = player.getLocation().clone().add(0, 0.5, 0);
		
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.ZRYW,
				player,
				target -> {
					target.getWorld().playSound(target, Sound.BLOCK_ANVIL_USE, 1.5f, 0.8f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.5f, 0.8f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.02, 0.08);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0, 0.02);
					_loc.getWorld().spawnParticle(Particle.ENCHANTED_HIT, _loc, 6, 0.5f, 1f, 0.5f, force);
				}));
		
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				0,
				loc -> { },
				hitCondition,
				(loc, entity) -> {
					AbstractEntity ae = BukkitAdapter.adapt(entity);
					ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
					if(!aMob.hasThreatTable()) {
						MutableBoolean canTargeting = new MutableBoolean(false);
						
						MythicMob mMob = aMob.getType();
						List<String> AI = mMob.getAIGoalSelectors();
						List<String> AI2 = mMob.getAITargetSelectors();
						if((AI == null || AI.isEmpty()) && (AI2 == null || AI2.isEmpty())) {
							if(ae.isMonster())
								canTargeting.setValue(true);
						} else {
							if(AI2 != null) {
								AI2.stream().filter(s -> {
									return s.contains("players") || s.contains("attacker");
								}).findAny().ifPresent(unused -> {
									AI.stream().filter(s -> {
										if(s.contains("meleeattack") 
												|| s.contains("arrowattack") 
												|| s.contains("spiderattack")
												|| s.contains("rangedattack")
												|| s.contains("bowattack")
												|| s.contains("bowshoot")
												|| s.contains("bowmaster")
												|| s.contains("crossbowAttack")) {
											return true;
										}
										return false;
									}).findAny().ifPresent(s -> {
										canTargeting.setValue(true);
									});
								});
							}
						}

						if(!canTargeting.booleanValue())
							return;
					}
					
					RuneEffectRuneTemplate.castEffect(
							castableRune,
							RuneEffectType.PROWOKACJA,
							entity,
							target -> {
								if(aMob.hasThreatTable()) {
									aMob.getThreatTable().Taunt(BukkitAdapter.adapt(player));
								} else {
									aMob.setTarget(BukkitAdapter.adapt(player));
								}
							},
							target -> {
								
							},
							new TimingRuneEffect(
									4,
									target -> {
										Location _loc = target.getEyeLocation().clone().add(0, 0.6, 0);
										_loc.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, _loc, 4,
												0.25, 0.25, 0.25, 0.04);
									})
							);
				});
	}

}
