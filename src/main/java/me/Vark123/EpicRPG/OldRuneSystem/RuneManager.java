package me.Vark123.EpicRPG.OldRuneSystem;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
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

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.OldFightSystem.ManualDamage;
import me.Vark123.EpicRPG.OldRuneSystem.Events.RuneUseEvent;
import me.Vark123.EpicRPG.OldRuneSystem.Runes.*;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

@Deprecated
public class RuneManager {
	
	private static final RuneManager instance = new RuneManager();
	
//	private final List<Player> globalCd;
	private final Map<UUID, Date> globalCd;
	@Getter
	private final Map<UUID, Map<String, ItemStackRune>> playerRuneCd;
	private final Map<UUID, Date> playerObszarowkiCd;
	
	private RuneManager() {
		globalCd = new ConcurrentHashMap<>();
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
//		if(modifiers.hasModifier1_lock() && r.hasModifier1()) {
//			p.sendMessage(Main.getInstance().getPrefix()+" §cUzywasz obecnie innej runy modyfikujacej obrazenia.");
//			return false;
//		}
//		if(modifiers.hasModifier2_lock() && r.hasModifier2()) {
//			p.sendMessage(Main.getInstance().getPrefix()+" §cUzyles przed chwila masowej runy obszarowej. Poczekaj chwile.");
//			return false;
//		}
		
		createGlobalCd(rpg);
		
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

		RuneUseEvent event = new RuneUseEvent(p, ir);
		Bukkit.getPluginManager().callEvent(event);
		
//		rpg.displayUpdate();
		r.castSpell();
		
		return true;
	}
	
	public void createGlobalCd(RpgPlayer rpg) {
		Date now = new Date();
		long globalCd = 1000;
		if(rpg.getStats().getFinalInteligencja() >= 1350)
			globalCd = 250;
		else {
			globalCd = (long) Utils.scaleValue(0, 1350, 1000, 250, rpg.getStats().getFinalInteligencja());
		}
		Date cd = new Date(now.getTime()+globalCd);
		this.globalCd.put(rpg.getPlayer().getUniqueId(), cd);
	}
	
	public void createRuneCd(RpgPlayer rpg, ItemStackRune ir) {
		Player p = rpg.getPlayer();

//		if((rpg.getModifiers().hasTajemnyBlask() || rpg.getModifiers().hasTajemnyBlask_m()) 
//				&& rpg.getInfo().getProffesion().equals("§5Mag")
//				&& !ir.getName().toLowerCase().contains("tajemny blask")) {
//			if(rpg.getModifiers().hasTajemnyBlask_m()) {
//				ir.setRegenTime((int)(ir.getRegenTime()*0.25));
//			} else if(rpg.getModifiers().hasTajemnyBlask()) {
//				ir.setRegenTime((int)(ir.getRegenTime()*0.4));
//			}
//		}
//		else if(rpg.getStats().getFinalMana() > 49) {
//			ir.modifyRegenTime(rpg.getStats());
//		}
		Map<String, ItemStackRune> cds = playerRuneCd.getOrDefault(p.getUniqueId(), new ConcurrentHashMap<>());
		cds.put(ir.getName(), ir);
		playerRuneCd.put(p.getUniqueId(), cds);
	}

	public boolean hasGlobalCd(Player p) {
		return globalCd.containsKey(p.getUniqueId()) && globalCd.get(p.getUniqueId()).after(new Date());
	}
	
	public boolean usableInRegion(Player p, ItemStackRune ir) {
		boolean allowPvP = true;
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
		State flag = set.queryValue(null, Flags.PVP);
		if(flag == null || flag.equals(State.DENY) 
				|| p.getWorld().getName().toLowerCase().contains("dungeon")
				|| p.getWorld().getName().toLowerCase().contains("raid")) 
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
		if(!playerRuneCd.containsKey(p.getUniqueId()))
			return true;
		Map<String, ItemStackRune> playerCd = playerRuneCd.get(p.getUniqueId());
		if(!playerCd.containsKey(ir.getName()))
			return true;
		
		ItemStackRune check = playerCd.get(ir.getName());
		Date present = new Date();
		Date old = check.getDate();
		long regenTime = check.getRegenTime();
		
		if((old.getTime() + regenTime) > present.getTime()) {
			int nextUse = (int) Math.ceil(((double)(old.getTime() + regenTime - present.getTime()))/1000.0);
			if(nextUse < 2) {
				p.playSound(p, Sound.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT, 2, .7f);
				p.spawnParticle(Particle.SMOKE, p.getLocation().add(0,1,0), 6, .4f, .4f, .4f, .03f);
			} else
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
		if(!ir.getName().toLowerCase().contains("tajemny blask")) {
//			if(rpg.getModifiers().hasTajemnyBlask_m() && rpg.getInfo().getProffesion().equals("§5Mag")) {
//				price *= 0.5;
//			} else if(rpg.getModifiers().hasTajemnyBlask() && rpg.getInfo().getProffesion().equals("§5Mag")) {
//				price *= 0.7;
//			}
		}
//		else if(rpg.getModifiers().hasZrodloNatury())
//			price *= 0.8;
		if(p.getWorld().getName().toLowerCase().contains("dungeon12")
				&& p.getNearbyEntities(30, 10, 30)
					.stream()
					.filter(entity -> {
						if(!entity.getName().equals("§2§l§oLoatheb"))
							return false;
						String phase = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity).getStance();
						if(!phase.equals("phase2") && !phase.equals("phase3"))
							return false;
						return true;
					})
					.findAny()
					.isPresent()) {
			price *= 1.5;
		}
		
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
//		if(rpg.getModifiers().hasZrodloNatury())
//			price *= 0.8;
		if(p.getWorld().getName().toLowerCase().contains("dungeon12")
				&& p.getNearbyEntities(30, 10, 30)
					.stream()
					.filter(entity -> {
						if(!entity.getName().equals("§2§l§oLoatheb"))
							return false;
						String phase = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity).getStance();
						if(!phase.equals("phase2") && !phase.equals("phase3"))
							return false;
						return true;
					})
					.findAny()
					.isPresent()) {
			price *= 1.5;
		}

		if(price >= (presentHealth + 1)) {
			p.sendMessage("§7[§6EpicRPG§7] §cNie masz wystarczajaco zycia by uzyc tej runy!");
			return false;
		}
		return true;
	}
	
	public void spendMana(RpgPlayer rpg, ItemStackRune ir) {
		int price = ir.getPrice();
//		if(rpg.getModifiers().hasZrodloNatury()){
//			price *= 0.8;
//		}
		
		Player p = rpg.getPlayer();
		if(p.getWorld().getName().toLowerCase().contains("dungeon12")
				&& p.getNearbyEntities(30, 10, 30)
					.stream()
					.filter(entity -> {
						if(!entity.getName().equals("§2§l§oLoatheb"))
							return false;
						String phase = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity).getStance();
						if(!phase.equals("phase2") && !phase.equals("phase3"))
							return false;
						return true;
					})
					.findAny()
					.isPresent()) {
			price *= 1.5;
		}
		
		RpgStats stats = rpg.getStats();
		if(stats.getPresentMana() >= price) {
			stats.removePresentManaSmart(price);
			return;
		}
		
		double hpPrice = price * 0.5;
		EntityDamageEvent event = new EntityDamageEvent(p, DamageCause.MAGIC, hpPrice);
		Bukkit.getPluginManager().callEvent(event);
		
		ManualDamage.doDamage(p, hpPrice, event);
	}
	
	public void spendHp(RpgPlayer rpg, ItemStackRune ir) {
		Player p = rpg.getPlayer();
		int price = ir.getPrice();
//		if(rpg.getModifiers().hasZrodloNatury()){
//			price *= 0.8;
//		}
		if(p.getWorld().getName().toLowerCase().contains("dungeon12")
				&& p.getNearbyEntities(30, 10, 30)
					.stream()
					.filter(entity -> {
						if(!entity.getName().equals("§2§l§oLoatheb"))
							return false;
						String phase = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity).getStance();
						if(!phase.equals("phase2") && !phase.equals("phase3"))
							return false;
						return true;
					})
					.findAny()
					.isPresent()) {
			price *= 1.5;
		}
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, p, DamageCause.MAGIC, price);
		Bukkit.getPluginManager().callEvent(event);
		
		ManualDamage.doDamage(p, price, event);
	}
	
	public boolean silaZywiolowEffect() {
		Random rand = new Random();
		return (rand.nextInt(100) < 5);
	}

	public Map<UUID, Date> getObszarowkiCd() {
		return playerObszarowkiCd;
	}
	
	public ARune getRune(ItemStackRune dr, Player p, Material material, String name) {
		name = name.toLowerCase();
		switch(material) {
			default:
				return null;
		}
	}
	
}
