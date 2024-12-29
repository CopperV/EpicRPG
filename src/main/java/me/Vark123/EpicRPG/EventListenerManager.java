package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;
import org.bukkit.Particle;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import me.Vark123.EpicRPG.API.EpicRPGApi;
import me.Vark123.EpicRPG.AdvancedBuySystem.AdvancedBuyListener;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockAddAllEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockAddEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockEntryEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockHealDebuffEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockRemoveAllEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockRemoveEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockResetEvent;
import me.Vark123.EpicRPG.BoosterSystem.Listeners.BoosterModifyListener;
import me.Vark123.EpicRPG.Chat.ChatMsgSendEvent;
import me.Vark123.EpicRPG.Core.CPS.CPSClickListener;
import me.Vark123.EpicRPG.Core.Events.PlayerUseDisabledBlockEvent;
import me.Vark123.EpicRPG.Core.Events.PlayerUseLeverEvent;
import me.Vark123.EpicRPG.Core.Listeners.ExecutableItemUseListener;
import me.Vark123.EpicRPG.Core.Listeners.HalloweenBossSpawnListener;
import me.Vark123.EpicRPG.Core.Listeners.LevelSystemControlListener;
import me.Vark123.EpicRPG.Core.Listeners.PlayerJumpModifyListener;
import me.Vark123.EpicRPG.Core.Listeners.VipBoostControlListener;
import me.Vark123.EpicRPG.Dungeons.Listeners.KoszmarKrukaPotionDebuffListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.EntityDamagerListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.EntityLastDamageCauseListener;
import me.Vark123.EpicRPG.Gems.GemPlaceProtEvent;
import me.Vark123.EpicRPG.HealthSystem.PlayerHealEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseDismountEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseInventoryEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseProtectionListener;
import me.Vark123.EpicRPG.HorseSystem.HorseRemoveOnPlayerDeathEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseStickUseEvent;
import me.Vark123.EpicRPG.Jewelry.Listeners.JewelryMenuInteractListener;
import me.Vark123.EpicRPG.Jewelry.Listeners.JewelryMenuOpenListener;
import me.Vark123.EpicRPG.Klejnoty.GrindstoneUseEvent;
import me.Vark123.EpicRPG.KosturSystem.KosturUseEvent;
import me.Vark123.EpicRPG.MMExtension.CustomConditionLoadEvent;
import me.Vark123.EpicRPG.MMExtension.CustomMechanicsLoadEvent;
import me.Vark123.EpicRPG.MMExtension.CustomTargeterLoadEvent;
import me.Vark123.EpicRPG.MMExtension.Misc.ProtectorDropKillEvent;
import me.Vark123.EpicRPG.Options.Listeners.CompassOptionRegistryListener;
import me.Vark123.EpicRPG.Options.Listeners.HorseOptionRegistryListener;
import me.Vark123.EpicRPG.Options.Listeners.MarkerRegistryListener;
import me.Vark123.EpicRPG.Options.Listeners.ResourceInfoRegistryListener;
import me.Vark123.EpicRPG.Options.Listeners.ScoreboardOptionRegistryListener;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerArrowWeaponUseEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerChangeEqEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerDropEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerJoinEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerQuitEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerRespawnEvent;
import me.Vark123.EpicRPG.Players.Components.Compass.Listeners.PlayerCompassUpdateEvent;
import me.Vark123.EpicRPG.Players.SkillControllers.HungerSkillEvent;
import me.Vark123.EpicRPG.Potions.PotionDrinkEvent;
import me.Vark123.EpicRPG.RubySystem.RubyPlaceProtEvent;
import me.Vark123.EpicRPG.RubySystem.RubyUseEvent;
import me.Vark123.EpicRPG.RuneSystem.Listeners.RuneInteractListener;
import me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb.LoathebHealDebuffListener;
import me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb.LoathebProjectileNeutralizeListener;
import me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb.LoathebProjectileReflectListener;
import me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb.LoathebWaeponDebuffListener;
import me.Vark123.EpicRPG.Scrolls.EpicBossScrollEvent;
import me.Vark123.EpicRPG.Scrolls.Katedra2ScrollEvent;
import me.Vark123.EpicRPG.Scrolls.KatedraScrollEvent;
import me.Vark123.EpicRPG.Scrolls.KlasaResetScrollEvent;
import me.Vark123.EpicRPG.Scrolls.StatResetScrollEvent;
import me.Vark123.EpicRPG.UpgradableSystem.Listeners.InhibitorInventoryClickListener;
import me.Vark123.EpicRPG.UpgradableSystem.Listeners.InhibitorInventoryCloseListener;
import me.Vark123.EpicRPG.UpgradableSystem.Listeners.UpgradableAnvilUseListener;
import me.Vark123.EpicRPG.WildHuntEvents.Listeners.WHEListener;

public class EventListenerManager {

	private static final Main inst = Main.getInstance();
	
	public static void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerRespawnEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerDropEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerChangeEqEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerArrowWeaponUseEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new ChatMsgSendEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new JewelryMenuOpenListener(), inst);
		Bukkit.getPluginManager().registerEvents(new JewelryMenuInteractListener(), inst);

		Bukkit.getPluginManager().registerEvents(new PlayerHealEvent(), inst);
		
		Bukkit.getPluginManager().registerEvents(new PotionDrinkEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RubyUseEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RubyPlaceProtEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new KosturUseEvent(), inst);
		
		Bukkit.getPluginManager().registerEvents(new CustomMechanicsLoadEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new CustomTargeterLoadEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new CustomConditionLoadEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new ProtectorDropKillEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new HorseDismountEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseInventoryEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseRemoveOnPlayerDeathEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseStickUseEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new BlackrockAddAllEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockAddEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockEntryEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockRemoveAllEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockRemoveEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockResetEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockHealDebuffEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new KatedraScrollEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new Katedra2ScrollEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new EpicBossScrollEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new StatResetScrollEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new KlasaResetScrollEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new GemPlaceProtEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerUseDisabledBlockEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerUseLeverEvent(), inst);
		
		Bukkit.getPluginManager().registerEvents(new HungerSkillEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new VipBoostControlListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LevelSystemControlListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ExecutableItemUseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new BoosterModifyListener(), inst);

		Bukkit.getPluginManager().registerEvents(new CompassOptionRegistryListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ScoreboardOptionRegistryListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ResourceInfoRegistryListener(), inst);
		Bukkit.getPluginManager().registerEvents(new MarkerRegistryListener(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseOptionRegistryListener(), inst);
		
		Bukkit.getPluginManager().registerEvents(new PlayerCompassUpdateEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new AdvancedBuyListener(), inst);
		Bukkit.getPluginManager().registerEvents(new GrindstoneUseEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new LoathebHealDebuffListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LoathebProjectileNeutralizeListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LoathebProjectileReflectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LoathebWaeponDebuffListener(), inst);

		Bukkit.getPluginManager().registerEvents(new KoszmarKrukaPotionDebuffListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CPSClickListener(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseProtectionListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerJumpModifyListener(), inst);

		Bukkit.getPluginManager().registerEvents(new InhibitorInventoryClickListener(), inst);
		Bukkit.getPluginManager().registerEvents(new InhibitorInventoryCloseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new UpgradableAnvilUseListener(), inst);

		Bukkit.getPluginManager().registerEvents(new WHEListener(), inst);
		Bukkit.getPluginManager().registerEvents(new HalloweenBossSpawnListener(), inst);

		Bukkit.getPluginManager().registerEvents(new EntityDamagerListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityLastDamageCauseListener(), inst);

		Bukkit.getPluginManager().registerEvents(new RuneInteractListener(), inst);
		
		//Calendar Events
		if(EpicRPGApi.getApi().getCalendarManager().isRegisteredEvent("reset_blackrock")) 
			EpicRPGApi.getApi().getCalendarManager().removeEvent("reset_blackrock");
		EpicRPGApi.getApi().getCalendarManager().addEvent("reset_blackrock", "every day", "00:05");
		
		if(EpicRPGApi.getApi().getCalendarManager().isRegisteredEvent("wildhunt1")) 
			EpicRPGApi.getApi().getCalendarManager().removeEvent("wildhunt1");
		EpicRPGApi.getApi().getCalendarManager().addEvent("wildhunt1", "every day", "17:15");
		if(EpicRPGApi.getApi().getCalendarManager().isRegisteredEvent("wildhunt2")) 
			EpicRPGApi.getApi().getCalendarManager().removeEvent("wildhunt2");
		EpicRPGApi.getApi().getCalendarManager().addEvent("wildhunt2", "every day", "17:25");
		if(EpicRPGApi.getApi().getCalendarManager().isRegisteredEvent("wildhunt3")) 
			EpicRPGApi.getApi().getCalendarManager().removeEvent("wildhunt3");
		EpicRPGApi.getApi().getCalendarManager().addEvent("wildhunt3", "every day", "17:30");
		if(EpicRPGApi.getApi().getCalendarManager().isRegisteredEvent("halloween_event")) 
			EpicRPGApi.getApi().getCalendarManager().removeEvent("halloween_event");
		EpicRPGApi.getApi().getCalendarManager().addEvent("halloween_event", "31.10", "xx:00,xx:15,xx:30,xx:45");
		
		addDisableDamageParticlesPacketListener();
	}
	
	private static void addDisableDamageParticlesPacketListener() {
		Main.getInstance().getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES) {
			@Override
			public void onPacketSending(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				if(event.getPacketType().equals(PacketType.Play.Server.WORLD_PARTICLES)) {
					if(packet.getNewParticles().read(0).getParticle().equals(Particle.DAMAGE_INDICATOR)) {
						packet.getIntegers().write(0, 0);
						event.setCancelled(true);
					}
				}
			}
		});
	}
	
}
