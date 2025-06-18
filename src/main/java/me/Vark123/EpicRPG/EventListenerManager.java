package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

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
import me.Vark123.EpicRPG.Consumables.Listeners.ConsumableUseListener;
import me.Vark123.EpicRPG.Consumables.Listeners.LoadConsumablesOnServerLoadListener;
import me.Vark123.EpicRPG.Core.CPS.CPSClickListener;
import me.Vark123.EpicRPG.Core.Events.PlayerUseDisabledBlockEvent;
import me.Vark123.EpicRPG.Core.Events.PlayerUseLeverEvent;
import me.Vark123.EpicRPG.Core.Listeners.ExecutableItemUseListener;
import me.Vark123.EpicRPG.Core.Listeners.HalloweenBossSpawnListener;
import me.Vark123.EpicRPG.Core.Listeners.LevelSystemControlListener;
import me.Vark123.EpicRPG.Core.Listeners.PlayerJumpModifyListener;
import me.Vark123.EpicRPG.Core.Listeners.VipBoostControlListener;
import me.Vark123.EpicRPG.Dungeons.Listeners.KoszmarKrukaPotionDebuffListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc.CiosKrytycznyEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc.CrossbowEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc.DodgeEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc.EnvironmentDebuffListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc.MegaCritEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc.ShulkerEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc.SlugaBeliaraEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc.VanillaPotionEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc.WywarEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.PostMisc.CritInfoEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.PostMisc.DollInfoEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.PostMisc.HpDisplayEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Professions.HunterProfessionModifierListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Professions.MageProfessionModifierListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Professions.WarriorProfessionModifierListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.AuraRozproszeniaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.BlogoslawienstwoPrzedwiecznychEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.CienAssasynaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.CiosWPlecyEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.EksplodujacaStrzalaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.GniewEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.GruboskornoscEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.InkantacjaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.KlatwaKrwiEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.KrwawaStrzalaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.LodowaStrzalaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.MordEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.OgnistaSferaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.OgnistaStrzalaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.PelniaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.PoswiecenieEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.PrecyzyjnyStrzalEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.ProwokacjaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.RytualKrwiEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.RytualWzniesieniaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.SkrytobojstwoEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.SwietaStrzalaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.SzalBitewnyEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.SzalPrzedwiecznychEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.TajemnyBlaskEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.TarczaCieniaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.TotemObronnyEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.TransEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.TrujacaAuraEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.WampiryzmEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.WedrownyCienEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.WybraniecBeliaraEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.WyostrzoneZmyslyEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.WyssanieEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.ZadzaKrwiEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.ZakletaStrzalaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.ZatrutaStrzalaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.ZewNaturyEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes.ZyciodajnaZiemiaEffectListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets.GrobowyZniwiarzSetListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets.MroznaZamiecSetListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets.OstrzeMrozuSetComboListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets.OstrzeMrozu_MSetComboListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets.RunicznyEgzekutorSetListener;
import me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets.WiecznyWedrowiecSetListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.DragonMeleeAttackListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.EntityDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.EntityDeathListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.EntityLastDamageCauseListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.EntityPostDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.FallDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.FireworkDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.LavaDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.MagicEntityDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.ProjectileLaunchListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.SelfShootProtectionListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.VoidDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.WeaknessMeleeAttackListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Crits.CiosKrytycznyCritCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Crits.PotionCritCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Crits.ProfCritCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Crits.StatsCritCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Death.EntityDeathBarbarzynskiSzalRuneListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Death.EntityDeathCmdExecuteListeners;
import me.Vark123.EpicRPG.FightSystem.Listeners.Death.EntityDeathCukierekAlboPsikusRuneListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Death.EntityDeathKrewPrzodkowRuneListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Death.EntityDeathRozprucieSkillListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Death.EntityDeathVaultListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Dodge.RuneDodgeCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Dodge.StatsDodgeCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Piercing.ProfPierceCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Piercing.StatsPierceCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Randomize.RuneRandomizeCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Randomize.StatsRandomizeCalcListener;
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
import me.Vark123.EpicRPG.RubySystem.RubyPlaceProtEvent;
import me.Vark123.EpicRPG.RubySystem.RubyUseEvent;
import me.Vark123.EpicRPG.RuneSystem.Listeners.RuneCalcCostZrodloNaturyListener;
import me.Vark123.EpicRPG.RuneSystem.Listeners.RuneCooldownManaReduceListener;
import me.Vark123.EpicRPG.RuneSystem.Listeners.RuneGlobalCooldownInteligencjaReduceListener;
import me.Vark123.EpicRPG.RuneSystem.Listeners.RuneInteractListener;
import me.Vark123.EpicRPG.RuneSystem.Listeners.RuneStunEffectListener;
import me.Vark123.EpicRPG.RuneSystem.Listeners.RuneTimeCheckListener;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonManager;
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
		
//		Bukkit.getPluginManager().registerEvents(new PotionDrinkEvent(), inst);
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

		Bukkit.getPluginManager().registerEvents(new MagicEntityDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityPostDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityLastDamageCauseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProjectileLaunchListener(), inst);
		Bukkit.getPluginManager().registerEvents(new FallDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new FireworkDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LavaDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new VoidDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SelfShootProtectionListener(), inst);
		Bukkit.getPluginManager().registerEvents(new DragonMeleeAttackListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WeaknessMeleeAttackListener(), inst);

		Bukkit.getPluginManager().registerEvents(new EntityDeathCmdExecuteListeners(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityDeathVaultListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityDeathRozprucieSkillListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityDeathCukierekAlboPsikusRuneListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityDeathBarbarzynskiSzalRuneListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityDeathKrewPrzodkowRuneListener(), inst);

		Bukkit.getPluginManager().registerEvents(new WarriorProfessionModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new HunterProfessionModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new MageProfessionModifierListener(), inst);

		Bukkit.getPluginManager().registerEvents(new WywarEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CrossbowEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SlugaBeliaraEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new VanillaPotionEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ShulkerEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EnvironmentDebuffListener(), inst);
		Bukkit.getPluginManager().registerEvents(new DodgeEffectListener(), inst);
		
		Bukkit.getPluginManager().registerEvents(new CiosKrytycznyCritCalcListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PotionCritCalcListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProfCritCalcListener(), inst);
		Bukkit.getPluginManager().registerEvents(new StatsCritCalcListener(), inst);

		Bukkit.getPluginManager().registerEvents(new StatsPierceCalcListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProfPierceCalcListener(), inst);
		
		Bukkit.getPluginManager().registerEvents(new RuneDodgeCalcListener(), inst);
		Bukkit.getPluginManager().registerEvents(new StatsDodgeCalcListener(), inst);

		Bukkit.getPluginManager().registerEvents(new StatsRandomizeCalcListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneRandomizeCalcListener(), inst);

		Bukkit.getPluginManager().registerEvents(new AuraRozproszeniaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new OgnistaSferaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new InkantacjaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new OgnistaStrzalaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LodowaStrzalaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PelniaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CiosWPlecyEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CienAssasynaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WyssanieEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SkrytobojstwoEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WedrownyCienEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WybraniecBeliaraEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TarczaCieniaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WyostrzoneZmyslyEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProwokacjaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TajemnyBlaskEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ZakletaStrzalaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PoswiecenieEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RytualWzniesieniaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TransEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SwietaStrzalaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SzalBitewnyEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new BlogoslawienstwoPrzedwiecznychEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SzalPrzedwiecznychEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ZyciodajnaZiemiaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TrujacaAuraEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ZatrutaStrzalaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new GruboskornoscEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TotemObronnyEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EksplodujacaStrzalaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ZewNaturyEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PrecyzyjnyStrzalEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new MordEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WampiryzmEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ZadzaKrwiEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new KrwawaStrzalaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new GniewEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new KlatwaKrwiEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RytualKrwiEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CiosKrytycznyEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new MegaCritEffectListener(), inst);

		Bukkit.getPluginManager().registerEvents(new DollInfoEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new HpDisplayEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CritInfoEffectListener(), inst);

		Bukkit.getPluginManager().registerEvents(new RuneInteractListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneTimeCheckListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneStunEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneCooldownManaReduceListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneGlobalCooldownInteligencjaReduceListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneCalcCostZrodloNaturyListener(), inst);

		Bukkit.getPluginManager().registerEvents(new GrobowyZniwiarzSetListener(), inst);
		Bukkit.getPluginManager().registerEvents(new MroznaZamiecSetListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RunicznyEgzekutorSetListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WiecznyWedrowiecSetListener(), inst);
		Bukkit.getPluginManager().registerEvents(new OstrzeMrozuSetComboListener(), inst);
		Bukkit.getPluginManager().registerEvents(new OstrzeMrozu_MSetComboListener(), inst);

		Bukkit.getPluginManager().registerEvents(new ConsumableUseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LoadConsumablesOnServerLoadListener(), inst);

		Bukkit.getPluginManager().registerEvents(SummonManager.get(), inst);
		
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
		
		PacketEvents.getAPI().getEventManager().registerListener(new DamageParticleListener(), PacketListenerPriority.NORMAL);
	}
	
}
