package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

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
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class BankaEnergii extends ACastableRune {
	
	private static final Random random = new Random();
	private static final PotionEffect potion = new PotionEffect(PotionEffectType.SLOWNESS, 20*3, 1);
	
	private IRuneHitCondition hitCondition;
	
	public BankaEnergii(RpgPlayer rpgPlayer, EpicRune rune) {
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
				20,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_TRIDENT_RETURN, 1.2f, 0.55f);
				},
				loc -> {
					for(int i = 0; i < 10*rune.getObszar(); ++i) {
						double theta = Math.random() * Math.PI * 2;
						double x = rune.getObszar() * Math.sin(theta);
						double y = 0.1;
						double z = rune.getObszar() * Math.cos(theta);
						Location tmp = loc.clone().add(x,y,z);
						tmp.getWorld().spawnParticle(Particle.FIREWORK, tmp, 0, 
								0, 1, 0, random.nextDouble(0.02, 0.08));
					}
					loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc.clone().add(0, 1.5, 0), 12,
							0.6, 0.6, 0.6, 0.03);
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						entity.addPotionEffect(potion);
						
						entity.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, entity.getLocation().clone().add(0,1,0), 12,
								0.4, 0.4, 0.4, 0.1);
						entity.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, startLoc.clone().add(0,1.5,0), 8,
								0.4, 0.4, 0.4, 0.1);
						
						int number = new Random().nextInt(2)+1;
						String sound = "ITEM_TRIDENT_RIPTIDE_"+number;
						entity.getWorld().playSound(loc, Sound.valueOf(sound), 2, 1.2F);
					}
				},
				loc -> { });
	}

}
