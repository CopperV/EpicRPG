package me.Vark123.EpicRPG.RuneSystem.Effects;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;

@Getter
@AllArgsConstructor
public class DamageFreezeEffect implements IRunePostDamageEffect {

	private int timer;
	private double damage;
	private ACastableRune castableRune;
	
	@Override
	public void playeEffect(Player damager, LivingEntity victim, EpicRune rune) {
		new BukkitRunnable() {
			int _timer = timer;
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
				--_timer;

				boolean flag = RuneUtils.damage(damager, victim, rune, damage);
				if(!flag) {
					cancel();
					return;
				}

				victim.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, victim.getLocation().clone().add(0,1,0),
						8, .4f, .4f, .4f, 0.05f);
				victim.getWorld().playSound(victim.getLocation().clone().add(0,1,0), Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 0.8f);
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

}
