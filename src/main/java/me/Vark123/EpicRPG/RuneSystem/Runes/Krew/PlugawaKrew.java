package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class PlugawaKrew extends ACastableRune {
	
	private static final DustOptions dust = new DustOptions(Color.fromRGB(154, 67, 3), 0.8f);
	private static final Random random = new Random();
	
	private IRuneHitCondition hitCondition;
	
	public PlugawaKrew(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.05, 0);
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				rune.getObszar(),
				4,
				10,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_SLIME_DEATH, 1.5f, 0.55f);
				},
				loc -> {
					for(int i = 0; i < 10*rune.getObszar(); ++i) {
						double theta = Math.random() * Math.PI * 2;
						double x = rune.getObszar() * Math.sin(theta);
						double y = 0.1;
						double z = rune.getObszar() * Math.cos(theta);
						Location tmp = loc.clone().add(x,y,z);
						tmp.getWorld().spawnParticle(Particle.ENTITY_EFFECT, tmp, 1, 
								0.05, 0.05, 0.05, random.nextDouble(0.2, 0.4), Color.fromRGB(154, 67, 3));
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune, rune.getDamage()*0.5)) {
						entity.getWorld().spawnParticle(Particle.DUST, entity.getLocation().clone().add(0,1,0), 8,
								0.4, 0.4, 0.4, random.nextDouble(0.1, 0.2), dust);
						
						entity.getWorld().playSound(loc, Sound.ENTITY_SLIME_HURT, 0.8f, 0.55f);
					}
				},
				loc -> { });
	}

}
