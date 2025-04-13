package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class ToksycznaChmura extends ACastableRune {
	
	private static final Random random = new Random();
	private static final PotionEffect effect = new PotionEffect(PotionEffectType.SLOWNESS, 20*2, 2);
	private static final DustOptions dust = new DustOptions(Color.fromRGB(0, 187, 0), 0.7f);
	
	private IRuneHitCondition hitCondition;
	
	public ToksycznaChmura(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.5, 0);
		double radius = rune.getObszar();
		double points = radius * 16;
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				rune.getObszar(),
				5,
				20,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_SLIME_HURT, 1.2f, 0.5f);
				},
				loc -> {
					for(int i = 0; i < points; ++i) {
						double r = random.nextDouble(radius);
						double angle1 = random.nextDouble(Math.PI * 2);
						double angle2 = random.nextDouble(Math.PI * 2);
						
						double x = r * Math.cos(angle1) * Math.sin(angle2);
						double y = r * Math.sin(angle1);
						double z = r * Math.cos(angle1) * Math.cos(angle2);
						Location pos = loc.clone().add(x, y, z);
						loc.getWorld().spawnParticle(Particle.DUST, pos, 1,
								0, 0, 0, random.nextDouble(), new DustOptions(Color.fromRGB(0, 187, 0), random.nextFloat(0.01f, 5f)));
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						entity.addPotionEffect(effect);
						
						loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1f, 0.8f);
						loc.getWorld().spawnParticle(Particle.DUST, entity.getLocation().clone().add(0,1,0), 8,
								0.3, 0.5, 0.3, 0.1, dust);
					}
				},
				loc -> { });
	}

}
