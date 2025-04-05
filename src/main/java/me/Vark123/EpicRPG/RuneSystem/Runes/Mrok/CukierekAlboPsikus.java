package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneEffectType;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.RuneEffectRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class CukierekAlboPsikus extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private PotionEffect potion;

	public CukierekAlboPsikus(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	
		potion = new PotionEffect(PotionEffectType.SLOWNESS, 20*rune.getDurationTime(), 1);
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		
		Location startLoc = player.getLocation().clone();
		startLoc.getWorld().playSound(startLoc, Sound.ENTITY_WITCH_CELEBRATE, 1, 0.75f);

		double radius = rune.getObszar();
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				radius,
				0,
				0,
				loc -> { },
				hitCondition,
				(loc, e) -> {
					RuneEffectRuneTemplate.castEffect(
							castableRune,
							RuneEffectType.CUKIEREK_ALBO_PSIKUS,
							e,
							entity -> {
								Location loc2 = entity.getLocation();
								entity.getWorld().playSound(loc2, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 2f, 0.6f);
								
				                loc2.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc2.clone().add(0,1,0),
				                		40, 0.8, 0.8, 0.8, 0.8, Color.fromRGB(88, 57, 39));
							
				                entity.addPotionEffect(potion);
							}, 
							loc2 -> { },
							new TimingRuneEffect(
									4,
									entity -> {
										Location loc2 = entity.getLocation();
										loc2.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc2.clone().add(0,1,0),
						                		18, 0.8, 0.8, 0.8, 0.25, Color.fromRGB(88, 57, 39));
									})
							);
				});
		
		TimingEffectRuneTemplate.castEffect(
				this,
				player,
				entity -> {
					entity.getWorld().playSound(entity, Sound.ENTITY_WITCH_CELEBRATE, 1, .75f);
				}, 
				entity -> { },
				new TimingRuneEffect(
						20,
						entity -> {
							double level = rpgPlayer.getInfo().getLevel();
							double amount = level * 0.46;
							
							RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpgPlayer, amount);
							Bukkit.getPluginManager().callEvent(event);
							if(!event.isCancelled()) {
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BREATH, 0.4f, 0.8f);
								player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0,1,0), 8, 0.6F, 0.9F, 0.6F, 0.2F);
							}
						})
				);
	}

}
