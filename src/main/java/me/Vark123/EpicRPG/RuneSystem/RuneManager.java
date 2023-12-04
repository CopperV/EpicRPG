package me.Vark123.EpicRPG.RuneSystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.Runes.*;

public class RuneManager {
	
	private static final RuneManager instance = new RuneManager();
	
	private final List<Player> globalCd;
	private final Map<Player,Map<String, ItemStackRune>> playerRuneCd;
	private final Map<Player, Date> playerObszarowkiCd;
	
	private RuneManager() {
		globalCd = new ArrayList<>();
		playerRuneCd = new ConcurrentHashMap<>();
		playerObszarowkiCd = new ConcurrentHashMap<>();
	}
	
	public static RuneManager getInstance() {
		return instance;
	}
	
	public boolean castRune(RpgPlayer rpg, ItemStack rune) {
		
		Player p = rpg.getPlayer();
		if(hasGlobalCd(p))
			return false;
		
		ItemStackRune ir = new ItemStackRune(rune);
		if(ir.isReqKlasa()) {
			String proffesion = ChatColor.stripColor(rpg.getInfo().getProffesion());
			if(!proffesion.equalsIgnoreCase(ir.getKlasa())) {
				p.sendMessage("§7[§bEpicRPG§7] §c Tylko "+ir.getKlasa()+" §cmoze uzyc tej runy!");
				return false;
			}
		}
		
		if(rpg.getStats().getKrag() < ir.getKrag()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cMusisz posiadac §7§o"+ir.getKrag()+" §ckrag magii, by uzyc "+ir.getName());
			return false;
		}
		
		if(!usableInRegion(p, ir))
			return false;
		
		if(!regenTimePass(p, ir))
			return false;
		
		if(ir.isHpInsteadMana()) {
			if(!hasEnoughHp(rpg, ir))
				return false;
		} else {
			if(!hasEnoughMana(rpg, ir))
				return false;
		}
		
		ARune r = getRune(ir, p, rune.getType(), rune.getItemMeta().getDisplayName());
		
		RpgModifiers modifiers = rpg.getModifiers();
		if(modifiers.hasModifier1_lock() && r.hasModifier1()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cUzywasz obecnie innej runy modyfikujacej obrazenia.");
			return false;
		}
		if(modifiers.hasModifier2_lock() && r.hasModifier2()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cUzyles przed chwila masowej runy obszarowej. Poczekaj chwile.");
			return false;
		}
		
		createGlobalCd(p);
		
		if(!rpg.getSkills().hasSilaZywiolow()
				|| !silaZywiolowEffect()) {
			if(ir.isHpInsteadMana())
				spendHp(rpg, ir);
			else
				spendMana(rpg, ir);
			createRuneCd(rpg, ir);
		} else {
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.5f);
			p.getWorld().spawnParticle(Particle.NAUTILUS, p.getLocation().add(0, 1, 0), 20, 0.75, 0.5, 0.75, 0.2);
		}

		
		rpg.displayUpdate();
		r.castSpell();
		if(rpg.getModifiers().hasLodowyBlok() && LodowyBlok.getEffected().contains(p) && !(r instanceof LodowyBlok))
			LodowyBlok.getEffected().remove(p);
		
		return true;
	}
	
	public void createGlobalCd(Player p) {
		if(globalCd.contains(p))
			globalCd.remove(p);
		globalCd.add(p);
		new BukkitRunnable() {
			@Override
			public void run() {
				globalCd.remove(p);
			}
		}.runTaskLater(Main.getInstance(), 15);
	}
	
	public void createRuneCd(RpgPlayer rpg, ItemStackRune ir) {
		Player p = rpg.getPlayer();
		if(rpg.getStats().getFinalMana() > 49) {
			ir.modifyRegenTime(rpg.getStats());
		}
		Map<String, ItemStackRune> cds = playerRuneCd.getOrDefault(p, new ConcurrentHashMap<>());
		cds.put(ir.getName(), ir);
		playerRuneCd.put(p, cds);
	}

	public boolean hasGlobalCd(Player p) {
		return globalCd.contains(p);
	}
	
	public boolean usableInRegion(Player p, ItemStackRune ir) {
		boolean allowPvP = true;
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
		State flag = set.queryValue(null, Flags.PVP);
		if(flag == null || flag.equals(State.DENY) 
				|| p.getWorld().getName().toLowerCase().contains("dungeon")) 
			allowPvP = false;
		switch(ir.getPvp()) {
			case 0:
				p.sendMessage("§7[§bEpicRPG§7] §cMasz przestarzala rune! Nie mozesz jej uzyc");
				return false;
			case 1:
				return true;
			case 2:
				if(allowPvP){
					p.sendMessage("§7[§bEpicRPG§7] §cTej runy nie mozesz uzyc na PvP!");
					return false;
				} else
					return true;
		}
		return true;
	}
	
	public boolean regenTimePass(Player p, ItemStackRune ir) {
		if(!playerRuneCd.containsKey(p))
			return true;
		Map<String, ItemStackRune> playerCd = playerRuneCd.get(p);
		if(!playerCd.containsKey(ir.getName()))
			return true;
		
		ItemStackRune check = playerCd.get(ir.getName());
		Date present = new Date();
		Date old = check.getDate();
		long regenTime = check.getRegenTime();
		
		if((old.getTime() + regenTime) > present.getTime()) {
			int nextUse = (int) Math.ceil(((double)(old.getTime() + regenTime - present.getTime()))/1000.0);
			p.sendMessage("§7[§6EpicRPG§7] §cRuny "+ir.getName()+" §cbedziesz mogl uzyc za §7"+nextUse+" §csekund");
			return false;
		}
		return true;
	}
	
	public boolean regenTimePass(Player p, ItemStack rune) {
		if(rune == null)
			return true;
		
		if(hasGlobalCd(p))
			return false;
		
		ItemStackRune ir = new ItemStackRune(rune);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		String prof = ChatColor.stripColor(rpg.getInfo().getProffesion());
		if(!prof.equalsIgnoreCase(ir.getKlasa())) {
			p.sendMessage("§7[§bEpicRPG§7] §c Tylko "+ir.getKlasa()+" §cmoze uzyc tej runy!");
			return false;
		}
		if(rpg.getStats().getKrag() < ir.getKrag()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cMusisz posiadac §7§o"+ir.getKrag()+" §ckrag magii, by uzyc "+ir.getName());
			return false;
		}
		if(regenTimePass(p, ir)) {
			p.sendMessage("§7[§6EpicRPG§7] §aRuna "+ir.getName()+" §ajest gotowa do uzycia");
			return true;
		}
		
		return true;
	}
	
	public boolean hasEnoughMana(RpgPlayer rpg, ItemStackRune ir) {
		Player p = rpg.getPlayer();
		RpgStats stats = rpg.getStats();
		int price = ir.getPrice();
		if(rpg.getModifiers().hasZrodloNatury())
			price *= 0.8;

		if(stats.getPresentMana() >= price)
			return true;
		
		if(rpg.getSkills().hasMagKrwi()) {
			if(price > stats.getFinalMana()) {
				p.sendMessage("§7[§6EpicRPG§7] §cNie spelniasz wymagan, by uzyc tej runy!");
				return false;
			}
			
			double newPrice = Math.ceil(price*0.5);
			double presentHealth = p.getHealth();
			
			if(newPrice >= (presentHealth + 1)) {
				p.sendMessage("§7[§6EpicRPG§7] §cNie masz ani many, ani zycia by uzyc tej runy!");
				return false;
			}
			return true;
		}

		p.sendMessage("§7[§6EpicRPG§7] §cMasz za malo many by uzyc tej runy!");
		return false;
	}

	public boolean hasEnoughHp(RpgPlayer rpg, ItemStackRune ir) {
		Player p = rpg.getPlayer();
		if(!rpg.getSkills().hasMagKrwi()) {
			p.sendMessage("§7[§6EpicRPG§7] §cMusisz byc magiem krwi, by moc uzyc tej runy!");
			return false;
		}
		double presentHealth = p.getHealth();
		int price = ir.getPrice();
		if(rpg.getModifiers().hasZrodloNatury())
			price *= 0.8;
		

		if(price >= (presentHealth + 1)) {
			p.sendMessage("§7[§6EpicRPG§7] §cNie masz wystarczajaco zycia by uzyc tej runy!");
			return false;
		}
		return true;
	}
	
	public void spendMana(RpgPlayer rpg, ItemStackRune ir) {
		int price = ir.getPrice();
		if(rpg.getModifiers().hasZrodloNatury()){
			price *= 0.8;
		}
		
		RpgStats stats = rpg.getStats();
		if(stats.getPresentMana() >= price) {
			stats.removePresentManaSmart(price);
			return;
		}
		
		double hpPrice = price * 0.5;
		Player p = rpg.getPlayer();
		EntityDamageEvent event = new EntityDamageEvent(p, DamageCause.MAGIC, hpPrice);
		Bukkit.getPluginManager().callEvent(event);
		
		ManualDamage.doDamage(p, hpPrice, event);
	}
	
	public void spendHp(RpgPlayer rpg, ItemStackRune ir) {
		Player p = rpg.getPlayer();
		int price = ir.getPrice();
		if(rpg.getModifiers().hasZrodloNatury()){
			price *= 0.8;
		}
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, p, DamageCause.MAGIC, price);
		Bukkit.getPluginManager().callEvent(event);
		
		ManualDamage.doDamage(p, price, event);
	}
	
	public boolean silaZywiolowEffect() {
		Random rand = new Random();
		return (rand.nextInt(100) < 5);
	}

	public Map<Player, Date> getObszarowkiCd() {
		return playerObszarowkiCd;
	}
	
	public ARune getRune(ItemStackRune dr, Player p, Material material, String name) {
		name = name.toLowerCase();
		switch(material) {
			case MUSIC_DISC_MELLOHI:
				switch(name) {
					case "§cognista strzala":	return new OgnistaStrzala(dr, p);
					case "§cpirokineza":		return new Pirokineza(dr, p);
					case "§ckula ognia":		return new KulaOgnia(dr, p);
					case "§cognista sfera":		return new OgnistaSfera(dr, p);
					case "§c§lduza kula ognia":	return new DuzaKulaOgnia(dr, p);
					case "§cburza ognista":		return new BurzaOgnista(dr, p);
					case "§cognista fala":		return new OgnistaFala(dr, p);
					case "§cmeteor":			return new Meteor(dr, p);
					case "§6§ldeszcz ognia":	return new DeszczOgnia(dr, p);
					case "§c§linkantacja":		return new Inkantacja(dr, p);
					case "§4§lkataklizm":		return new Kataklizm(dr, p);
					case "§4§lkataklizm i":		return new Kataklizm(dr, p);
					case "§4§lkataklizm ii":	return new Kataklizm(dr, p);
					case "§c§oaura rozproszenia":	return new AuraRozproszenia(dr, p);
					case "§cmala burza ognista":return new MalaBurzaOgnista(dr, p);
					case "§cduza burza ognista":return new DuzaBurzaOgnista(dr, p);
					case "§x§8§6§0§1§1§1§lplomien lenga":return new PlomienLenga(dr, p);
					case "§x§8§6§0§1§1§1§lplomien lenga i":return new PlomienLenga(dr, p);
					case "§x§8§6§0§1§1§1§lplomien lenga ii":return new PlomienLenga(dr, p);
					case "§c§lmasowa pirokineza":return new MasowaPirokineza(dr, p);
					case "§c§lmasowa pirokineza i":return new MasowaPirokineza(dr, p);
					case "§c§lmasowa pirokineza ii":return new MasowaPirokineza(dr, p);
					case "§c§oognisty wybuch":	return new OgnistyWybuch(dr, p);
					case "§c§orozerwanie":		return new Rozerwanie(dr, p);
					case "§4§owulkaniczny gejzer":return new WulkanicznyGejzer(dr, p);
					default:					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_BLOCKS:
				switch(name) {
					case "§epiach w oczy": 		return new PiachWOczy(dr, p);
					case "§bhaduoken": 			return new Haduoken(dr, p);
					case "§bgejzer": 			return new Gejzer(dr, p);
					case "§bwodna piesc": 		return new WodnaPiesc(dr, p);
					case "§b§luderzenie chi": 	return new UderzenieChi(dr, p);
					case "§9§lpelnia": 			return new Pelnia(dr, p);
					case "§b§lreka adanosa": 	return new RekaAdanosa(dr, p);
					default: 					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_CHIRP:
				switch(name) {
					case "§blodowa strzala":	return new LodowaStrzala(dr, p);
					case "§blodowa lanca":		return new LodowaLanca(dr, p);
					case "§bbryla lodu":		return new BrylaLodu(dr, p);
					case "§blodowa fala":		return new LodowaFala(dr, p);
					case "§6§lgrom":			return new Grom(dr, p);
					case "§bsopel lodu":		return new SopelLodu(dr, p);
					case "§blodowe przebicie":	return new LodowePrzebicie(dr, p);
					case "§beksplozja lodu":	return new EksplozjaLodu(dr, p);
					case "§b§ofala mrozu":		return new FalaMrozu(dr, p);
					case "§blodowy pocisk":		return new LodowyPocisk(dr, p);
					case "§3§lzamiec":			return new Zamiec(dr, p);
					case "§b§olodowy blok":		return new LodowyBlok(dr, p);
					default: 					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_FAR:
				switch(name) {
					case "§3swieta strzala":	return new SwietaStrzala(dr, p);
					case "§3wypedzanie zla":	return new WypedzanieZla(dr, p);
					case "§3trans":				return new Trans(dr, p);
					case "§3zniszczenie zla":	return new ZniszczenieZla(dr, p);
					case "§3swiete slowo":		return new SwieteSlowo(dr, p);
					case "§3szal bitewny":		return new SzalBitewny(dr, p);
					case "§3gruboskornosc":		return new Gruboskornosc(dr, p);
					case "§3zadza krwi":		return new ZadzaKrwi(dr, p);
					case "§3prowokacja":		return new Prowokacja(dr, p);
					case "§3§lwampiryzm":		return new Wampiryzm(dr, p);
					case "§3§lwampiryzm i":		return new Wampiryzm_H(dr, p);
					case "§3§lwampiryzm ii":	return new Wampiryzm_M(dr, p);
					case "§3swiety pocisk":		return new SwietyPocisk(dr, p);
					case "§3swiety plomien":	return new SwietyPlomien(dr, p);
					case "§3blogoslawiona ziemia":	return new BlogoslawionaZiemia(dr, p);
					case "§3poswiecenie":		return new Poswiecenie(dr, p);
					case "§3rytual wzniesienia":return new RytualWzniesienia(dr, p);
					case "§3aura czystosci":	return new AuraCzystosci(dr, p);
					case "§3swieta krucjata":	return new SwietaKrucjata(dr, p);
					case "§3barbarzynski szal":	return new BarbarzynskiSzal(dr, p);
					case "§3sila jednosci":		return new SilaJednosci(dr, p);
					case "§3zryw":				return new Zryw(dr, p);
					case "§3§lfala dezorientacyjna":return new FalaDezorientacyjna(dr, p);
					case "§3§lfala dezorientacyjna i":return new FalaDezorientacyjna(dr, p);
					case "§3§lfala dezorientacyjna ii":return new FalaDezorientacyjna(dr, p);
					case "§3swiety mrok":		return new SwietyMrok(dr, p);
					case "§3wybraniec beliara":	return new WybraniecBeliara(dr, p);
					default: 					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_MALL:
				switch(name) {
					case "§awyostrzone zmysly":	return new WyostrzoneZmysly(dr, p);
					case "§aognista strzala":	return new OgnistaStrzalaLcz(dr, p);
					case "§azatruta strzala":	return new ZatrutaStrzala(dr, p);
					case "§aprecyzyjny strzal":	return new PrecyzyjnyStrzal(dr, p);
					case "§alodowa strzala":	return new LodowaStrzalaLcz(dr, p);
					case "§azacma":				return new Zacma(dr, p);
					case "§askrytobojstwo":		return new Skrytobojstwo(dr, p);
					case "§acien assasyna":		return new CienAssasyna(dr, p);
					case "§a§llowy":			return new Lowy(dr, p);
					case "§a§llowy i":			return new Lowy_H(dr, p);
					case "§a§llowy ii":			return new Lowy_M(dr, p);
					case "§apenetracja":		return new Penetracja(dr, p);
					case "§aswieta strzala":	return new SwietaStrzalaLcz(dr, p);
					case "§akrwawa strzala":	return new KrwawaStrzala(dr, p);
					case "§agrad strzal":		return new GradStrzal(dr, p);
					case "§awedrowny cien":		return new WedrownyCien(dr, p);
					case "§acios w plecy":		return new CiosWPlecy(dr, p);
					case "§amord":				return new Mord(dr, p);
					case "§aodwrocenie uwagi":	return new OdwrocenieUwagi(dr, p);
					case "§a§leksplodujaca strzala":return new EksplodujacaStrzala(dr, p);
					case "§a§leksplodujaca strzala i":return new EksplodujacaStrzala_H(dr, p);
					case "§a§leksplodujaca strzala ii":return new EksplodujacaStrzala_M(dr, p);
					case "§atrujaca aura":		return new TrujacaAura(dr, p);
					case "§azakleta strzala":	return new ZakletaStrzala(dr, p);
					default: 					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_STAL:
				switch(name) {
					case "§7wir":				return new Wir(dr, p);
					case "§8noc w dzien":		return new NocWDzien(dr, p);
					case "§7rezonans":			return new Rezonans(dr, p);
					case "§e§okorzen":			return new Korzen(dr, p);
					case "§6§lsfera corristo":	return new SferaCorristo(dr, p);
					case "§2§otrzesienie ziemii":	return new TrzesienieZiemii(dr, p);
					case "§eeksplozja":			return new Eksplozja(dr, p);
					case "§9totem obronny":		return new TotemObronny(dr, p);
					case "§7§lteleportacja krotkodystansowa":	return new TeleportacjaKrotkodystansowa(dr, p);
					case "§e§lfala uderzeniowa":return new FalaUderzeniowa(dr, p);
					default: 					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_STRAD:
				switch(name) {
					case "§2uderzenie wiatru":	return new UderzenieWiatru(dr, p);
					case "§2uderzenie burzy":	return new UderzenieBurzy(dr, p);
					case "§8§lszal pustki":		return new SzalPustki(dr, p);
					case "§8§lszal pustki i":	return new SzalPustki(dr, p);
					case "§8§lszal pustki ii":	return new SzalPustki(dr, p);
					case "§a§oroj owadow":		return new RojOwadow(dr, p);
					case "§2trujace ukaszenie":	return new TrujaceUkaszenie(dr, p);
					case "§x§0§0§b§b§0§0toksyczna chmura":	return new ToksycznaChmura(dr, p);
					case "§x§0§0§9§a§0§0§lzrodlo natury":	return new ZrodloNatury(dr, p);
					default: 					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_11:
				switch(name) {
					case "§8smierc ozywiencom":	return new SmiercOzywiencom(dr, p);
					case "§8strzala ciemnosci":	return new StrzalaCiemnosci(dr, p);
					case "§8tchnienie smierci": return new TchnienieSmierci(dr, p);
					case "§8uderzenie cienia":	return new UderzenieCienia(dr, p);
					case "§5§l§okula smierci":	return new UderzenieCienia(dr, p);
					case "§5§lsmiertelna fala":	return new SmiertelnaFala(dr, p);
					case "§6§lukazanie smierci":return new UkazanieSmierci(dr, p);
					case "§5§lmasowe zniszczenie":return new MasoweZniszczenie(dr, p);
					case "§8§lzeslanie mroku":	return new ZeslanieMroku(dr, p);
					case "§8§lzeslanie mroku i":return new ZeslanieMroku(dr, p);
					case "§8§lzeslanie mroku ii":return new ZeslanieMroku(dr, p);
					case "§8§lspetanie":		return new Spetanie(dr, p);
					case "§8§lspetanie i":		return new Spetanie(dr, p);
					case "§8§lspetanie ii":		return new Spetanie(dr, p);
					case "§x§0§0§5§5§0§0§lkrzyk umarlych":	return new KrzykUmarlych(dr, p);
					case "§9rozerwanie duszy":	return new RozerwanieDuszy(dr, p);
					case "§8§ltarcza cienia":	return new TarczaCienia(dr, p);
					case "§8§luscisk umarlych":	return new UsciskUmarlych(dr, p);
					case "§8§luscisk umarlych i":	return new UsciskUmarlych(dr, p);
					case "§8§luscisk umarlych ii":	return new UsciskUmarlych(dr, p);
					case "§8§llaska beliara":	return new LaskaBeliara(dr, p);
					case "§8strzala mroku":		return new StrzalaMroku(dr, p);
					case "§8§owlocznia ciemnosci":return new WloczniaCiemnosci(dr, p);
					case "§8§ozmrok":			return new Zmrok(dr, p);
					case "§8zaglada":			return new Zaglada(dr, p);
					default: 					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_13:
				switch(name) {
					case "§5magiczny pocisk": 	return new MagicznyPocisk(dr, p);
					case "§emala blyskawica":	return new MalaBlyskawica(dr, p);
					case "§eblyskawica":		return new MalaBlyskawica(dr, p);
					case "§ewiazka elektryczna":return new WiazkaElektryczna(dr, p);
					case "§epiorun kulisty":	return new PiorunKulisty(dr, p);
					case "§bbanka energii":		return new BankaEnergii(dr, p);
					case "§bfala elektryczna":	return new FalaElektryczna(dr, p);
					case "§3§lsztorm":			return new Sztorm(dr, p);
					case "§eporazenie elektryczne":	return new PorazenieElektryczne(dr, p);
					case "§5§omagiczny iskra":	return new MagicznaIskra(dr, p);
					case "§e§owstrzas elektryczny":	return new WstrzasElektryczny(dr, p);
					case "§eprzywolanie blyskawicy":return new PrzywolanieBlyskawicy(dr, p);
					case "§5magiczne pociski":	return new MagicznePociski(dr, p);
					case "§e§l§oelektryczne przeciazenie":return new ElektrycznePrzeciazenie(dr, p);
					case "§e§l§oelektryczne przeciazenie i":return new ElektrycznePrzeciazenie(dr, p);
					case "§e§l§oelektryczne przeciazenie ii":return new ElektrycznePrzeciazenie(dr, p);
					default: 					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_CAT:
				switch(name) {
					case "§aleczenie":			return new Leczenie(dr, p);
					case "§5wyssanie":			return new Wyssanie(dr, p);
					case "§6§lgrupowe leczenie":return new GrupoweLeczenie(dr, p);
					case "§c§ozdroj zycia":		return new ZdrojZycia(dr, p);
					case "§ezlodziej energii":	return new ZlodziejEnergii(dr, p);
					case "§5§lpotezna runa domisia":return new PoteznaRunaDomisia(dr, p);
					case "§5§lpotezna runa domisia i":return new PoteznaRunaDomisia(dr, p);
					case "§5§lpotezna runa domisia ii":return new PoteznaRunaDomisia(dr, p);
					case "§c§lzyciodajna ziemia":return new ZyciodajnaZiemia(dr, p);
					case "§c§lzyciodajna ziemia i":return new ZyciodajnaZiemia_M(dr, p);
					default: 					return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_WARD:
				switch(name) {
					case "§c§lkrwawy deszcz":		return new KrwawyDeszcz(dr, p);
					case "§c§lkrwawa fala":			return new KrwawaFala(dr, p);
					case "§c§lkrwawa fala i":		return new KrwawaFala(dr, p);
					case "§c§lkrwawa fala ii":		return new KrwawaFala(dr, p);
					case "§d§l§oszept przedwiecznych":	return new SzeptPrzedwiecznych(dr, p);
					case "§c§l§oszpon beliara":		return new SzponBeliaraInt(dr, p);
					case "§c§lszpon beliara":		return new SzponBeliaraMana(dr, p);
					case "§5§lszal beliara":		return new SzalBeliara(dr, p);
					case "§5§lzakazany rytual":		return new ZakazanyRytual(dr, p);
					case "§5§lzakazany rytual i":	return new ZakazanyRytual_H(dr, p);
					case "§5§lzakazany rytual ii":	return new ZakazanyRytual_M(dr, p);
					case "§x§8§a§0§3§0§3§okrwawy pocisk":return new KrwawyPocisk(dr, p);
					case "§x§8§a§0§3§0§3spirala krwi":return new SpiralaKrwi(dr, p);
					case "§x§a§a§5§3§0§3zatruta krew":return new ZatrutaKrew(dr, p);
					case "§x§9§a§0§3§0§3§lrozprucie":return new Rozprucie(dr, p);
					case "§x§8§a§0§3§0§3furia":		return new Furia(dr, p);
					default: 						return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_WAIT:
				switch(name) {
					case "§x§e§7§e§7§e§7§oswiatlo":		return new Swiatlo(dr, p);
					case "§7czystka":					return new Czystka(dr, p);
					case "§x§0§0§f§f§f§fwlocznia elysian":	return new WloczniaElysian(dr, p);
					case "§7§lkamienny obserwator":		return new KamiennyObserwator(dr, p);
					default: 							return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_PIGSTEP:
				switch(name) {
					case "§x§d§a§0§3§0§3§lzew krwi":		return new ZewKrwi(dr, p);
					case "§x§d§a§0§3§0§3§orytual krwi":		return new RytualKrwi(dr, p);
					case "§x§9§a§4§3§0§3§oplugawa krew":	return new PlugawaKrew(dr, p);
					case "§x§9§a§0§3§4§3krew przodkow":		return new KrewPrzodkow(dr, p);
					case "§x§8§a§0§3§0§3§ogniew":			return new Gniew(dr, p);
					case "§x§c§d§0§0§0§0§ldrenaz":			return new Drenaz(dr, p);
					case "§x§8§a§0§3§0§3§lklatwa krwi":		return new KlatwaKrwi(dr, p);
					case "§x§e§e§0§5§0§5§ltransfuzja":		return new Transfuzja(dr, p);
					default: 							return new OgnistaStrzala(dr, p);
				}
			case MUSIC_DISC_OTHERSIDE:
				switch(name) {
					case "§f§lsila rownowagi":			return new SilaRownowagi(dr, p);
					case "§f§lsila rownowagi i":		return new SilaRownowagi_H(dr, p);
					case "§f§lsila rownowagi ii":		return new SilaRownowagi_M(dr, p);
					default: 							return new OgnistaStrzala(dr, p);
				}
			default:
				System.out.println(name);
				return new OgnistaStrzala(dr, p);
		}
	}
	
}
