package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import java.util.Date;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;

public class StunRuneTemplate {

	private StunRuneTemplate() { }
	
	public static void castEffect(
			ACastableRune castableRune,
			LivingEntity target) {
		
		long time = new Date().getTime() + 1000 * castableRune.getRune().getDurationTime();
		AbstractEntity ae = BukkitAdapter.adapt(target);
		if(!ae.hasMetadata("epic_stun") || ((long) ae.getMetadata("epic_stun").get()) < time) {
			ae.setMetadata("epic_stun", time);
		}
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled() || ae == null ||
						ae.isDead() || !ae.hasMetadata("epic_stun") ||
						((long) ae.getMetadata("epic_stun").get()) != time)
					return;
				
				ae.removeMetadata("epic_stun");
			}
		}.runTaskLater(Main.getInstance(), 20 * castableRune.getRune().getDurationTime());
		
	}
	
}
