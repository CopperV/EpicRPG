package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class FalaDezorientacyjna extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private PotionEffect effect;
	
	public FalaDezorientacyjna(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		
		effect = new PotionEffect(PotionEffectType.SLOWNESS, 20*rune.getDurationTime(), 1);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.1, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				6, 
				0.275, 
				1,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_TRIDENT_THUNDER, 1.5f, 0.8f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.SNEEZE, loc, 5,
							0.1, 0.1, 0.1, 0.05);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, 1, 0.6f);
						
						entity.addPotionEffect(effect);
					}
					
				},
				loc -> { });
	}

}
