package me.Vark123.EpicRPG.Potions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class PotionUtils {
	
	private PotionUtils() {}
	
	public static RpgPotionType getPotionType(ItemStack it) {
		NBTItem nbt = new NBTItem(it);
		if (!nbt.hasTag("MYTHIC_TYPE"))
			return RpgPotionType.NONE;
		
		switch(nbt.getString("MYTHIC_TYPE")) {
			case "Mana10":
			case "Mana30":
			case "Mana50":
			case "Mana100":
			case "Mana250":
			case "Mana500":
				return RpgPotionType.MANA;
			case "Zycie30":
			case "Zycie50":
			case "Zycie100":
			case "Zycie200":
			case "Zycie400":
			case "Zycie800":
				return RpgPotionType.ZYCIE;
			case "T_Mana10":
			case "T_Mana30":
			case "T_Mana50":
			case "T_Mana100":
			case "T_Mana250":
			case "T_Mana500":
				return RpgPotionType.T_MANA;
			case "T_Zycie30":
			case "T_Zycie50":
			case "T_Zycie100":
			case "T_Zycie200":
			case "T_Zycie400":
			case "T_Zycie800":
				return RpgPotionType.T_ZYCIE;
			case "Wiedzmin1":
			case "Wiedzmin2":
			case "Wiedzmin3":
			case "Wiedzmin4":
			case "Wiedzmin5":
			case "Wiedzmin6":
				return RpgPotionType.WIEDZMIN;
			case "T_Wiedzmin1":
			case "T_Wiedzmin2":
			case "T_Wiedzmin3":
			case "T_Wiedzmin4":
			case "T_Wiedzmin5":
			case "T_Wiedzmin6":
				return RpgPotionType.T_WIEDZMIN;
			case "Wywar_Sila_I":
			case "Wywar_Zdolnosci_I":
			case "Wywar_Zrecznosc_I":
			case "Wywar_Inteligencja_I":
			case "Wywar_Wytrzymalosc_I":
			case "Wywar_Walka_I":
			case "Wywar_Sila_II":
			case "Wywar_Zdolnosci_II":
			case "Wywar_Zrecznosc_II":
			case "Wywar_Inteligencja_II":
			case "Wywar_Wytrzymalosc_II":
			case "Wywar_Walka_II":
			case "Wywar_Sila_III":
			case "Wywar_Zdolnosci_III":
			case "Wywar_Zrecznosc_III":
			case "Wywar_Inteligencja_III":
			case "Wywar_Wytrzymalosc_III":
			case "Wywar_Walka_III":
				return RpgPotionType.WYWAR;
			case "Mikstura_Duszy":
			case "Mikstura_Sily":
			case "Mikstura_Zrecznosci":
			case "Mikstura_Umyslu":
			case "Mikstura_Mysliwska":
			case "Mikstura_Wytrzymalosci":
			case "Mikstura_Walki":
			case "Mikstura_Wojownika":
			case "Mikstura_Mysliwego":
			case "Mikstura_Maga":
			case "Mikstura_Szalu":
			case "Mikstura_Druida":
			case "Mikstura_Szermieza":
			case "Mikstura_Tarczownika":
				return RpgPotionType.STALE;
			case "Alkohol_Mlecznik":
				return RpgPotionType.ALKOHOL;
		}
		return RpgPotionType.NONE;
	}
	
	public static RpgPotionEffect getPotionEffect(RpgPotionType type, final ItemStack potion) {
		RpgPotionEffect effect;
		switch(type) {
			case MANA:
				effect = rpg -> {
					MutableInt mana = new MutableInt();
					potion.getItemMeta().getLore().stream().filter(s -> {
						return s.contains("Przywrocona mana");
					}).findAny().ifPresent(s -> {
						mana.setValue(Integer.parseInt(ChatColor.stripColor(s.replace("§4- §3Przywrocona mana: §7", ""))));
					});
					rpg.getStats().addPresentManaSmart(mana.intValue());
				};
				break;
			case ZYCIE:
				effect = rpg -> {
					MutableInt hp = new MutableInt();
					potion.getItemMeta().getLore().stream().filter(s -> {
						return s.contains("Przywrocone zycie");
					}).findAny().ifPresent(s -> {
						hp.setValue(Integer.parseInt(ChatColor.stripColor(s.replace("§4- §3Przywrocone zycie: §7", ""))));
					});
					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hp.intValue());
					Bukkit.getPluginManager().callEvent(event);
				};
				break;
			case T_MANA:
				effect = rpg -> {
					MutableInt mana = new MutableInt();
					MutableInt time = new MutableInt();
					potion.getItemMeta().getLore().stream().filter(s -> {
						return s.contains("Przywrocona mana") || s.contains("Czas trwania");
					}).forEach(s -> {
						if(s.contains("Przywrocona mana")) {
							mana.setValue(Integer.parseInt(ChatColor.stripColor(
									s.replace("§4- §3Przywrocona mana na sekunde: §7", ""))));
							return;
						}
						time.setValue(Integer.parseInt(ChatColor.stripColor(
								s.replace("§4- §3Czas trwania: §7", "").replace(" sekund", ""))));
					});
					rpg.getStats().createRegenManaTask(time.intValue(), mana.intValue());
				};
				break;
			case T_ZYCIE:
				effect = rpg -> {
					MutableInt hp = new MutableInt();
					MutableInt time = new MutableInt();
					potion.getItemMeta().getLore().stream().filter(s -> {
						return s.contains("Przywrocone zycie") || s.contains("Czas trwania");
					}).forEach(s -> {
						if(s.contains("Przywrocone zycie")) {
							hp.setValue(Integer.parseInt(ChatColor.stripColor(
									s.replace("§4- §3Przywrocone zycie na sekunde: §7", ""))));
							return;
						}
						time.setValue(Integer.parseInt(ChatColor.stripColor(
								s.replace("§4- §3Czas trwania: §7", "").replace(" sekund", ""))));
					});
					rpg.getStats().createRegenHpTask(time.intValue(), hp.intValue());
				};
				break;
			case WIEDZMIN:
				effect = rpg -> {
					MutableInt mana = new MutableInt();
					MutableInt hp = new MutableInt();
					potion.getItemMeta().getLore().stream().filter(s -> {
						return s.contains("Przywrocona mana") || s.contains("Przywrocone zycie");
					}).forEach(s -> {
						if(s.contains("Przywrocona mana")) {
							mana.setValue(Integer.parseInt(ChatColor.stripColor(s.replace("§4- §3Przywrocona mana: §7", ""))));
							return;
						}
						hp.setValue(Integer.parseInt(ChatColor.stripColor(s.replace("§4- §3Przywrocone zycie: §7", ""))));
					});
					rpg.getStats().addPresentManaSmart(mana.intValue());
					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hp.intValue());
					Bukkit.getPluginManager().callEvent(event);
				};
				break;
			case T_WIEDZMIN:
				effect = rpg -> {
					MutableInt mana = new MutableInt();
					MutableInt hp = new MutableInt();
					MutableInt time = new MutableInt();
					potion.getItemMeta().getLore().stream().filter(s -> {
						return s.contains("Przywrocona mana")
								|| s.contains("Przywrocone zycie")
								|| s.contains("Czas trwania");
					}).forEach(s -> {
						if(s.contains("Przywrocona mana")) {
							mana.setValue(Integer.parseInt(ChatColor.stripColor(
									s.replace("§4- §3Przywrocona mana na sekunde: §7", ""))));
							return;
						} else if(s.contains("Przywrocone zycie")) {
							hp.setValue(Integer.parseInt(ChatColor.stripColor(
									s.replace("§4- §3Przywrocone zycie na sekunde: §7", ""))));
							return;
						}
						time.setValue(Integer.parseInt(ChatColor.stripColor(
								s.replace("§4- §3Czas trwania: §7", "").replace(" sekund", ""))));
					});
					rpg.getStats().createRegenHpTask(time.intValue(), hp.intValue());
					rpg.getStats().createRegenManaTask(time.intValue(), mana.intValue());
				};
				break;
			case WYWAR:
				effect = rpg -> {
					String[] tab = potion.getItemMeta().getDisplayName().split(" ");
					int level = getWywarLevel(tab[2]);
					RpgModifiers modifiers = rpg.getModifiers();
					String strMethod = "setPotion"+tab[1];
					String plName = getWywarPlName(tab[1]);
					Class<?> modClass = modifiers.getClass();
					Method method;
					try {
						method = modClass.getMethod(strMethod, int.class);
						method.invoke(modifiers, level);
						rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()+" §aWypiles wywar "+plName+". Za §760 §asekund przestanie dzialac.");
					} catch (NoSuchMethodException 
							| SecurityException 
							| IllegalAccessException 
							| IllegalArgumentException 
							| InvocationTargetException e) {
						Bukkit.getConsoleSender().sendMessage("Blad z metoda "+strMethod);
						Bukkit.getConsoleSender().sendMessage(e.getMessage());
						return;
					}
					new BukkitRunnable() {
						
						@Override
						public void run() {
							try {
								method.invoke(modifiers, 0);
								rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()+"§cWywar "+plName+" wlasnie przestal dzialac.");
							} catch (IllegalAccessException 
									| IllegalArgumentException 
									| InvocationTargetException e) {
								Bukkit.getConsoleSender().sendMessage("Blad z metoda "+strMethod);
								Bukkit.getConsoleSender().sendMessage(e.getMessage());
							}
							
						}
					}.runTaskLater(Main.getInstance(), 20*60);
				};
				break;
			case STALE:
				effect = rpg -> {
					RpgStats stats = rpg.getStats();
					switch(potion.getItemMeta().getDisplayName().split(" ")[1].toLowerCase()) {
						case "duszy":
							stats.addPotionMana(2);
							stats.addPresentManaSmart(2);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 2 punkty many. Obecnie posiadasz §a"+(stats.getMana()+stats.getPotionMana())+" §2punktow many.");
							break;
						case "sily":
							stats.addPotionSila(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty sily.");
							break;
						case "zrecznosci":
							stats.addPotionZrecznosc(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty zrecznosci.");
							break;
						case "umyslu":
							stats.addPotionInteligencja(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty inteligencji.");
							break;
						case "mysliwska":
							stats.addPotionZdolnosci(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty zdolnosci mysliwskich.");
							break;
						case "wytrzymalosci":
							stats.addPotionWytrzymalosc(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty wytrzymalosci.");
							break;
						case "walki":
							stats.addPotionWalka(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty walki.");
							break;
						case "wojownika":
							stats.addPotionSila(1);
							stats.addPotionWytrzymalosc(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty sily.");
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty walki.");
							break;
						case "mysliwego":
							stats.addPotionZrecznosc(1);
							stats.addPotionZdolnosci(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty zrecznosci.");
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty zdolnosci mysliwskich.");
							break;
						case "maga":
							stats.addPotionMana(2);
							stats.addPresentManaSmart(2);
							stats.addPotionInteligencja(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkty inteligencji.");
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 2 punkty many. Obecnie posiadasz §a"+(stats.getMana()+stats.getPotionMana())+" §2punktow many.");
							break;
						case "szalu":
							stats.addPotionWalka(2);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 2 punkty walki.");
							break;
						case "druida":
							stats.addPotionHealth(2);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 2 punkty zycia.");
							break;
						case "szermieza":
							stats.addPotionObrazenia(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkt obrazen.");
							break;
						case "tarczownika":
							stats.addPotionOchrona(1);
							rpg.getPlayer().sendMessage(Main.getInstance().getPrefix()
									+" §2Otrzymales 1 punkt ochrony.");
							break;
					}
				};
				break;
			case ALKOHOL:
			{
				effect = rpg -> {
					Player p = rpg.getPlayer();
					switch(potion.getItemMeta().getDisplayName().toLowerCase()) {
						case "§f§omlecznik":
							p.getActivePotionEffects().forEach(eff -> p.removePotionEffect(eff.getType()));
							PotionEffect pot = new PotionEffect(PotionEffectType.CONFUSION, 40, 9);
							p.addPotionEffect(pot);
							break;
					}
				};
			}
				break;
			default:
				effect = rp -> {};
				break;
		}
		
		return effect;
	}
	
	public static int getWywarLevel(String roman) {
		switch(roman.toUpperCase()) {
			case "I":
				return 1;
			case "II":
				return 2;
			case "III":
				return 3;
			default:
				return 0;
		}
	}
	
	public static String getWywarPlName(String name) {
		switch(name) {
		case "Sila":
			return "sily";
		case "Zrecznosc":
			return "zrecznosci";
		case "Zdolnosci-mysliwskie":
			return "zdolnosci mysliwskich";
		case "Wytrzymalosc":
			return "wytrzymalosci";
		case "Inteligencja":
			return "inteligencji";
		case "Walka":
			return "walki";
		default:
			return null;
		}
	}
	
}
