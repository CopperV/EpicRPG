package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneEntityEffect;

public class TimingEffectRuneTemplate {

	private TimingEffectRuneTemplate() { }
	
	public static void castEffect(
			ACastableRune castableRune,
			LivingEntity target,
			IRuneEntityEffect startEffect,
			IRuneEntityEffect endEffect,
			TimingRuneEffect... effects) {
		int duration = castableRune.getRune().getDurationTime();
		
		Player caster = castableRune.getPlayer();
		
		if(startEffect != null)
			startEffect.playEffect(target);
		
		new BukkitRunnable() {
			double timer = duration;
			@Override
			public void run() {
				
				if(!castableRune.casterInCastWorld() || timer <= 0 
						|| target.isDead() || caster.isDead()
						|| (target instanceof Player && !((Player)target).isOnline()) || !caster.isOnline()
						|| !target.getWorld().getName().equals(caster.getWorld().getName())) {
					
					if(endEffect != null)
						endEffect.playEffect(target);
					
					cancel();
					return;
				}
				
				--timer;
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		for(var eff : effects) {
			new BukkitRunnable() {
				int timer = (int) (duration * (20. / (double) eff.interval));
				@Override
				public void run() {
					if(isCancelled())
						return;
					if(!castableRune.casterInCastWorld() || timer <= 0 
							|| target.isDead() || caster.isDead()
							|| !target.getWorld().getName().equals(caster.getWorld().getName())) {
						cancel();
						return;
					}
					--timer;
					
					eff.effect.playEffect(target);
				}
			}.runTaskTimer(Main.getInstance(), 0, eff.interval);
		}
		
	}
	
	@AllArgsConstructor
	@Getter
	public static class TimingRuneEffect {
		int interval;
		IRuneEntityEffect effect;
	}
	
}
