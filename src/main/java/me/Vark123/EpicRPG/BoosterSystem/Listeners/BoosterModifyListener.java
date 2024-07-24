package me.Vark123.EpicRPG.BoosterSystem.Listeners;

import java.util.Date;
import java.util.Map.Entry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.BoosterSystem.Booster;
import me.Vark123.EpicRPG.BoosterSystem.BoosterManager;
import me.Vark123.EpicRPG.Core.Events.CoinsModifyEvent;
import me.Vark123.EpicRPG.Core.Events.EventCurrency2ModifyEvent;
import me.Vark123.EpicRPG.Core.Events.ExpModifyEvent;
import me.Vark123.EpicRPG.Core.Events.MoneyModifyEvent;
import me.Vark123.EpicRPG.Core.Events.RudaModifyEvent;
import me.Vark123.EpicRPG.Core.Events.StygiaModifyEvent;

public class BoosterModifyListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onExp(ExpModifyEvent e) {
		if(e.isCancelled())
			return;
		
		Entry<Booster, Date> result = BoosterManager.get().getTopBooster("xp");
		if(result == null)
			return;
		
		Booster booster = result.getKey();
		double modifier = booster.getModifier();
		if(e.getReason().equalsIgnoreCase("quest"))
			modifier /= 10.;
		e.setModifier(e.getModifier()+modifier);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onMoney(MoneyModifyEvent e) {
		if(e.isCancelled())
			return;
		
		Entry<Booster, Date> result = BoosterManager.get().getTopBooster("money");
		if(result == null)
			return;
		
		Booster booster = result.getKey();
		double modifier = booster.getModifier();
		if(e.getReason().equalsIgnoreCase("quest"))
			modifier /= 10.;
		e.setModifier(e.getModifier()+modifier);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onStygia(StygiaModifyEvent e) {
		if(e.isCancelled())
			return;
		
		Entry<Booster, Date> result = BoosterManager.get().getTopBooster("stygia");
		if(result == null)
			return;
		
		Booster booster = result.getKey();
		double modifier = booster.getModifier();
		if(e.getReason().equalsIgnoreCase("quest"))
			modifier /= 10.;
		e.setModifier(e.getModifier()+modifier);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDragonCoins(CoinsModifyEvent e) {
		if(e.isCancelled())
			return;
		
		Entry<Booster, Date> result = BoosterManager.get().getTopBooster("coins");
		if(result == null)
			return;
		
		Booster booster = result.getKey();
		double modifier = booster.getModifier();
		if(e.getReason().equalsIgnoreCase("quest"))
			modifier /= 10.;
		e.setModifier(e.getModifier()+modifier);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onRuda(RudaModifyEvent e) {
		if(e.isCancelled())
			return;
		
		Entry<Booster, Date> result = BoosterManager.get().getTopBooster("ruda");
		if(result == null)
			return;
		
		Booster booster = result.getKey();
		double modifier = booster.getModifier();
		if(e.getReason().equalsIgnoreCase("quest"))
			modifier /= 10.;
		e.setModifier(e.getModifier()+modifier);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEvent2(EventCurrency2ModifyEvent e) {
		if(e.isCancelled())
			return;
		
		Entry<Booster, Date> result = BoosterManager.get().getTopBooster("event2");
		if(result == null)
			return;
		
		Booster booster = result.getKey();
		double modifier = booster.getModifier();
		if(e.getReason().equalsIgnoreCase("quest"))
			modifier /= 10.;
		e.setModifier(e.getModifier()+modifier);
	}
	
}
