package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class TrzesienieZiemi extends ACastableRune {
	
	private static final PotionEffect effect = new PotionEffect(PotionEffectType.SLOWNESS, 20*3, 1);
	private static final BlockData blockData = Material.DIRT.createBlockData();
	private static final Random random = new Random();
	
	private IRuneHitCondition hitCondition;
	
	public TrzesienieZiemi(RpgPlayer rpgPlayer, EpicRune rune) {
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
					new BukkitRunnable() {
						int timer = rune.getDurationTime() * 10;
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(timer <= 0 || !casterInCastWorld()) {
								this.cancel();
								return;
							}
							--timer;

							float vol = 1.5f + random.nextFloat(4f);
							float pitch = 0.1f + random.nextFloat(0.25f);
							loc.getWorld().playSound(loc, Sound.ENTITY_HUSK_STEP, vol, pitch);
						}
					}.runTaskTimer(Main.getInstance(), 0, 2);
				},
				loc -> {
					int points = (int) (rune.getObszar() * 6);
					for(int i = 0; i < points; ++i) {
						double theta = Math.random() * Math.PI * 2;
						double radius = random.nextDouble(rune.getObszar());
						double x = radius * Math.sin(theta);
						double z = radius * Math.cos(theta);
						Location tmp = loc.clone().add(x,0,z);
						
						tmp.getWorld().spawnParticle(Particle.BLOCK, tmp, 3,
								0.1, 0.05, 0.1, blockData);
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						entity.addPotionEffect(effect);
						if(random.nextDouble() < 0.2) {
							entity.setVelocity(new Vector(0, Math.random()*0.5+0.01, 0));
						}
					}
				},
				loc -> { });
	}

}
