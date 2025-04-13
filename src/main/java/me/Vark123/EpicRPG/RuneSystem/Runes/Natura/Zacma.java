package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
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

public class Zacma extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private PotionEffect effect1;
	private PotionEffect effect2;
	private BlockData blockData = Material.DIRT.createBlockData();
	
	public Zacma(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		
		effect1 = new PotionEffect(PotionEffectType.SLOWNESS, 20*rune.getDurationTime(), 2);
		effect2 = new PotionEffect(PotionEffectType.DARKNESS, 20*rune.getDurationTime(), 2);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.15, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				8, 
				0.25, 
				1,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_CREEPER_HURT, 1.7f, 0.5f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.BLOCK, loc, 4,
							0.12, 0.07, 0.12, 0.07, blockData);
					loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 3,
							0.12, 0.07, 0.12, 0.07, blockData);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_CREEPER_DEATH, 1, 0.3f);
						
						entity.addPotionEffect(effect1);
						entity.addPotionEffect(effect2);
					}
					
				},
				loc -> { });
	}

}
