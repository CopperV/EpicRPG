package me.Vark123.EpicRPG.WildHuntEvents.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.WildHuntEvents.WHEManager;
import me.nikl.calendarevents.CalendarEvent;

public class WHEListener implements Listener {

	@EventHandler
	public void onFirstDate(CalendarEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getLabels().contains("wildhunt1"))
			return;
		WHEManager.get().firstAnnouncement();
	}

	@EventHandler
	public void onSecondDate(CalendarEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getLabels().contains("wildhunt2"))
			return;
		WHEManager.get().secondAnnouncement();
	}


	@EventHandler
	public void onThirdDate(CalendarEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getLabels().contains("wildhunt3"))
			return;
		WHEManager.get().thirdAnnouncement();
	}


}
