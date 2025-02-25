package me.Vark123.EpicRPG.Consumables.Listeners;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Consumables.ConsumableManager;
import me.Vark123.EpicRPG.Consumables.Impl.DrinkConsumable;
import me.Vark123.EpicRPG.Consumables.Impl.EffectPotionConsumable;
import me.Vark123.EpicRPG.Consumables.Impl.HpPotionConsumable;
import me.Vark123.EpicRPG.Consumables.Impl.ManaPotionConsumable;
import me.Vark123.EpicRPG.Consumables.Impl.TimingHpPotionConsumable;
import me.Vark123.EpicRPG.Consumables.Impl.TimingWitcherPotionConsumable;
import me.Vark123.EpicRPG.Consumables.Impl.WitcherPotionConsumable;
import me.Vark123.EpicRPG.Consumables.Impl.WywarPotionConsumable;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class LoadConsumablesOnServerLoadListener implements Listener {

	@EventHandler
	private void onServerLoad(ServerLoadEvent e) {
		ConsumableManager manager = ConsumableManager.inst();
		
		//COMMON
		manager.registerConsumable("Zycie1", new HpPotionConsumable(50));
		manager.registerConsumable("Zycie2", new HpPotionConsumable(100));
		manager.registerConsumable("Zycie3", new HpPotionConsumable(200));
		manager.registerConsumable("Zycie4", new HpPotionConsumable(400));
		manager.registerConsumable("Zycie5", new HpPotionConsumable(750));
		manager.registerConsumable("Zycie6", new HpPotionConsumable(1400));

		manager.registerConsumable("Mana1", new ManaPotionConsumable(30));
		manager.registerConsumable("Mana2", new ManaPotionConsumable(50));
		manager.registerConsumable("Mana3", new ManaPotionConsumable(100));
		manager.registerConsumable("Mana4", new ManaPotionConsumable(250));
		manager.registerConsumable("Mana5", new ManaPotionConsumable(500));
		manager.registerConsumable("Mana6", new ManaPotionConsumable(1000));

		manager.registerConsumable("Wiedzmin1", new WitcherPotionConsumable(60, 35));
		manager.registerConsumable("Wiedzmin2", new WitcherPotionConsumable(125, 60));
		manager.registerConsumable("Wiedzmin3", new WitcherPotionConsumable(250, 125));
		manager.registerConsumable("Wiedzmin4", new WitcherPotionConsumable(500, 300));
		manager.registerConsumable("Wiedzmin5", new WitcherPotionConsumable(900, 625));
		manager.registerConsumable("Wiedzmin6", new WitcherPotionConsumable(1600, 1200));

		//TIMING
		manager.registerConsumable("T_Zycie1", new TimingHpPotionConsumable(5, 15));
		manager.registerConsumable("T_Zycie2", new TimingHpPotionConsumable(10, 15));
		manager.registerConsumable("T_Zycie3", new TimingHpPotionConsumable(15, 20));
		manager.registerConsumable("T_Zycie4", new TimingHpPotionConsumable(20, 25));
		manager.registerConsumable("T_Zycie5", new TimingHpPotionConsumable(30, 30));
		manager.registerConsumable("T_Zycie6", new TimingHpPotionConsumable(40, 40));

		manager.registerConsumable("T_Mana1", new TimingHpPotionConsumable(2, 20));
		manager.registerConsumable("T_Mana2", new TimingHpPotionConsumable(3, 25));
		manager.registerConsumable("T_Mana3", new TimingHpPotionConsumable(5, 30));
		manager.registerConsumable("T_Mana4", new TimingHpPotionConsumable(10, 35));
		manager.registerConsumable("T_Mana5", new TimingHpPotionConsumable(15, 45));
		manager.registerConsumable("T_Mana6", new TimingHpPotionConsumable(25, 50));

		manager.registerConsumable("T_Wiedzmin1", new TimingWitcherPotionConsumable(7, 4, 15));
		manager.registerConsumable("T_Wiedzmin2", new TimingWitcherPotionConsumable(9, 5, 20));
		manager.registerConsumable("T_Wiedzmin3", new TimingWitcherPotionConsumable(15, 8, 25));
		manager.registerConsumable("T_Wiedzmin4", new TimingWitcherPotionConsumable(20, 14, 30));
		manager.registerConsumable("T_Wiedzmin5", new TimingWitcherPotionConsumable(27, 20, 40));
		manager.registerConsumable("T_Wiedzmin6", new TimingWitcherPotionConsumable(38, 30, 50));
		
		//STALE
		manager.registerConsumable("Mikstura_Duszy", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionMana(2);
			stats.addPresentManaSmart(2);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 2 punkty many. Obecnie posiadasz §a"+(stats.getMana()+stats.getPotionMana())+" §2punktow many");
		}, Color.fromRGB(23, 81, 126)));
		manager.registerConsumable("Mikstura_Sily", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionSila(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt sily");
		}, Color.fromRGB(255, 72, 49)));
		manager.registerConsumable("Mikstura_Zrecznosci", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionZrecznosc(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt zrecznosci");
		}, Color.GREEN));
		manager.registerConsumable("Mikstura_Umyslu", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionInteligencja(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt inteligencji");
		}, Color.PURPLE));
		manager.registerConsumable("Mikstura_Mysliwska", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionZdolnosci(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt zdolnosci mysliwskich");
		}, Color.LIME));
		manager.registerConsumable("Mikstura_Wytrzymalosci", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionWytrzymalosc(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt wytrzymalosci");
		}, Color.OLIVE));
		manager.registerConsumable("Mikstura_Walki", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionWalka(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt walki");
		}, Color.fromRGB(213, 181, 110)));
		
		manager.registerConsumable("Mikstura_Wojownika", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionSila(1);
			stats.addPotionWytrzymalosc(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt sily");
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt wytrzymalosci");
		}, Color.fromRGB(161, 61, 45)));
		manager.registerConsumable("Mikstura_Mysliwego", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionZdolnosci(1);
			stats.addPotionZrecznosc(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt zrecznosci");
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt zdolnosci mysliwskich");
		}, Color.fromRGB(39, 174, 125)));
		manager.registerConsumable("Mikstura_Maga", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionMana(2);
			stats.addPotionInteligencja(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt inteligencji");
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 2 punkty many. Obecnie posiadasz §a"+(stats.getMana()+stats.getPotionMana())+" §2punktow many");
		}, Color.fromRGB(8, 37, 103)));
		manager.registerConsumable("Mikstura_Szalu", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionWalka(2);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 2 punkty walki");
		}, Color.fromRGB(128, 32, 48)));
		
		manager.registerConsumable("Mikstura_Druida", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionHealth(2);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 2 punkty zycia");
		}, Color.fromRGB(255, 72, 49)));
		manager.registerConsumable("Mikstura_Szermieza", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionObrazenia(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt obrazen");
		}, Color.fromRGB(100, 100, 100)));
		manager.registerConsumable("Mikstura_Tarczownika", new DrinkConsumable(rpg -> {
			RpgStats stats = rpg.getStats();
			stats.addPotionOchrona(1);
			rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
					+" §2Otrzymales 1 punkt ochrony");
		}, Color.fromRGB(196, 164, 132)));
		
		//VANILLA
		manager.registerConsumable("Mikstura_Szybkosci1", 
				new EffectPotionConsumable(PotionEffectType.SPEED, 600, 1, Color.fromRGB(224, 224, 224)));
		manager.registerConsumable("Mikstura_Szybkosci2", 
				new EffectPotionConsumable(PotionEffectType.SPEED, 600, 2, Color.fromRGB(224, 224, 224)));
	
		//ALCOHOLS
		manager.registerConsumable("Alkohol_Mlecznik", new DrinkConsumable(rpg -> {
			Player p = rpg.getPlayer();
			p.getActivePotionEffects().forEach(eff -> p.removePotionEffect(eff.getType()));

			PotionEffect pot = new PotionEffect(PotionEffectType.NAUSEA, 40, 9);
			p.addPotionEffect(pot);
		}, Color.GRAY));
		
		//WYWARY
		manager.registerConsumable("Wywar_Sila_I", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionSila(), 
				(rpg, val) -> rpg.getModifiers().createWywarSilaTask(60, val),
				1, 
				Color.fromRGB(231, 133, 135)));
		manager.registerConsumable("Wywar_Sila_II", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionSila(), 
				(rpg, val) -> rpg.getModifiers().createWywarSilaTask(60, val),
				2, 
				Color.fromRGB(231, 133, 135)));
		manager.registerConsumable("Wywar_Sila_III", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionSila(), 
				(rpg, val) -> rpg.getModifiers().createWywarSilaTask(60, val),
				3, 
				Color.fromRGB(231, 133, 135)));

		manager.registerConsumable("Wywar_Zdolnosci_I", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionZdolnosci(), 
				(rpg, val) -> rpg.getModifiers().createWywarZdolnosciTask(60, val),
				1, 
				Color.fromRGB(152, 251, 152)));
		manager.registerConsumable("Wywar_Zdolnosci_II", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionZdolnosci(), 
				(rpg, val) -> rpg.getModifiers().createWywarZdolnosciTask(60, val),
				2, 
				Color.fromRGB(152, 251, 152)));
		manager.registerConsumable("Wywar_Zdolnosci_III", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionZdolnosci(), 
				(rpg, val) -> rpg.getModifiers().createWywarZdolnosciTask(60, val),
				3, 
				Color.fromRGB(152, 251, 152)));
		
		manager.registerConsumable("Wywar_Zrecznosc_I", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionZrecznosc(), 
				(rpg, val) -> rpg.getModifiers().createWywarZrecznoscTask(60, val),
				1, 
				Color.fromRGB(79, 145, 83)));
		manager.registerConsumable("Wywar_Zrecznosc_II", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionZrecznosc(), 
				(rpg, val) -> rpg.getModifiers().createWywarZrecznoscTask(60, val),
				2, 
				Color.fromRGB(79, 145, 83)));
		manager.registerConsumable("Wywar_Zrecznosc_III", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionZrecznosc(), 
				(rpg, val) -> rpg.getModifiers().createWywarZrecznoscTask(60, val),
				3, 
				Color.fromRGB(79, 145, 83)));
		
		manager.registerConsumable("Wywar_Inteligencja_I", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionInteligencja(), 
				(rpg, val) -> rpg.getModifiers().createWywarInteligencjaTask(60, val),
				1, 
				Color.fromRGB(250, 230, 250)));
		manager.registerConsumable("Wywar_Inteligencja_II", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionInteligencja(), 
				(rpg, val) -> rpg.getModifiers().createWywarInteligencjaTask(60, val),
				2, 
				Color.fromRGB(250, 230, 250)));
		manager.registerConsumable("Wywar_Inteligencja_III", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionInteligencja(), 
				(rpg, val) -> rpg.getModifiers().createWywarInteligencjaTask(60, val),
				3, 
				Color.fromRGB(250, 230, 250)));

		manager.registerConsumable("Wywar_Wytrzymalosc_I", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionWytrzymalosc(), 
				(rpg, val) -> rpg.getModifiers().createWywarWytrzymaloscTask(60, val),
				1, 
				Color.fromRGB(152, 118, 84)));
		manager.registerConsumable("Wywar_Wytrzymalosc_II", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionWytrzymalosc(), 
				(rpg, val) -> rpg.getModifiers().createWywarWytrzymaloscTask(60, val),
				2, 
				Color.fromRGB(152, 118, 84)));
		manager.registerConsumable("Wywar_Wytrzymalosc_III", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionWytrzymalosc(), 
				(rpg, val) -> rpg.getModifiers().createWywarWytrzymaloscTask(60, val),
				3, 
				Color.fromRGB(152, 118, 84)));

		manager.registerConsumable("Wywar_Walka_I", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionWalka(), 
				(rpg, val) -> rpg.getModifiers().createWywarWalkaTask(60, val),
				1, 
				Color.fromRGB(230, 190, 138)));
		manager.registerConsumable("Wywar_Walka_II", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionWalka(), 
				(rpg, val) -> rpg.getModifiers().createWywarWalkaTask(60, val),
				2, 
				Color.fromRGB(230, 190, 138)));
		manager.registerConsumable("Wywar_Walka_III", new WywarPotionConsumable(
				rpg -> rpg.getModifiers().getPotionWalka(), 
				(rpg, val) -> rpg.getModifiers().createWywarWalkaTask(60, val),
				3, 
				Color.fromRGB(230, 190, 138)));
	}
	
}
