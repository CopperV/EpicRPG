package me.Vark123.EpicRPG.RuneSystem.Effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;

@Getter
@AllArgsConstructor
public class DamageCustomEffect implements IRunePostDamageEffect {

	private int timer;
	private int interval;
	private double damage;
	private ACastableRune castableRune;
	private IRuneLocationEffect effect;
	
	@Override
	public void playeEffect(Player damager, LivingEntity victim, EpicRune rune) {
		new BukkitRunnable() {
			double _timer = timer;
			double step = interval / 20.;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(_timer <= 0 
						|| !castableRune.casterInCastWorld() 
						|| !castableRune.entityInCastWorld(victim)
						|| victim.isDead()) {
					cancel();
					return;
				}
				_timer -= step;

				boolean flag = RuneUtils.damage(damager, victim, rune, damage);
				if(!flag) {
					cancel();
					return;
				}
				
				effect.playEffect(victim.getLocation().clone());
				
			}
		}.runTaskTimer(Main.getInstance(), interval, interval);
	}
	
}
