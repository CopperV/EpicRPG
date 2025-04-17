package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class GniewPrzodkow extends ACastableRune {
	
	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	
	public GniewPrzodkow(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.25, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				12, 
				0.175, 
				2,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_RAVAGER_ROAR, 1.2f, rand.nextFloat(0.01f, 0.5f));
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc, 6,
							0.12, 0.2, 0.12, rand.nextDouble(0.25, 0.65), Color.fromRGB(138, 3, 3));
					loc.getWorld().spawnParticle(Particle.RAID_OMEN, loc, 2,
							0.12, 0.2, 0.12, rand.nextDouble(0.01, 0.06));
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 0.8f, 0.4f);
					}
					
				},
				loc -> { });
	}

}
