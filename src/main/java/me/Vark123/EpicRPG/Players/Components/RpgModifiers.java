package me.Vark123.EpicRPG.Players.Components;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicOptions.Main;
import me.Vark123.EpicRPG.Consumables.Impl.WywarPotionConsumable.IWywarSetter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune.RuneLockerTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class RpgModifiers implements Serializable {

	private static final long serialVersionUID = -9173795626376278411L;

	private RpgPlayer rpg;
	
	private boolean zewKrwi;
	private double zewKrwiMod = 0;
	private boolean paktKrwi;
	private boolean paktKrwiMeasure;
	private double paktKrwiHp;
	private boolean paktKrwi_h;
	private boolean paktKrwiMeasure_h;
	private double paktKrwiHp_h;
	private boolean paktKrwi_m;
	private boolean paktKrwiMeasure_m;
	private double paktKrwiHp_m;
	
	private static final double BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_BASE_RENEW_PERCENT = 0.9;
	private double blogoslawienstwoPrzedwiecznychRenewChance = BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_BASE_RENEW_PERCENT;
	private static final double BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_H_BASE_RENEW_PERCENT = 0.95;
	private double blogoslawienstwoPrzedwiecznych_hRenewChance = BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_H_BASE_RENEW_PERCENT;
	private static final double BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_M_BASE_RENEW_PERCENT = 1;
	private double blogoslawienstwoPrzedwiecznych_mRenewChance = BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_H_BASE_RENEW_PERCENT;
	
	private int potionSila;
	private int potionZrecznosc;
	private int potionZdolnosci;
	private int potionWytrzymalosc;
	private int potionInteligencja;
	private int potionWalka;
	
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BossBar wywarSilaBar;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BossBar wywarWytrzymaloscBar;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BossBar wywarZrecznoscBar;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BossBar wywarZdolnosciBar;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BossBar wywarInteligencjaBar;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BossBar wywarWalkaBar;

	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BukkitTask wywarSilaTask;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BukkitTask wywarWytrzymaloscTask;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BukkitTask wywarZrecznoscTask;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BukkitTask wywarZdolnosciTask;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BukkitTask wywarInteligencjaTask;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BukkitTask wywarWalkaTask;
	
	private Collection<RuneLockerTypes> activeLockers = new HashSet<>();
	private Collection<EpicModifierTypes> activeModifiers = new HashSet<>();
	
	public RpgModifiers(RpgPlayer rpg) {
		this.rpg = rpg;

		wywarSilaBar = Bukkit.createBossBar("§7§oWywar Sily I", BarColor.WHITE, BarStyle.SOLID);
		wywarSilaBar.addPlayer(rpg.getPlayer());
		wywarSilaBar.setVisible(false);

		wywarWytrzymaloscBar = Bukkit.createBossBar("§7§oWywar Wytrzymalosci I", BarColor.WHITE, BarStyle.SOLID);
		wywarWytrzymaloscBar.addPlayer(rpg.getPlayer());
		wywarWytrzymaloscBar.setVisible(false);

		wywarZrecznoscBar = Bukkit.createBossBar("§7§oWywar Zrecznosci I", BarColor.WHITE, BarStyle.SOLID);
		wywarZrecznoscBar.addPlayer(rpg.getPlayer());
		wywarZrecznoscBar.setVisible(false);

		wywarZdolnosciBar = Bukkit.createBossBar("§7§oWywar Zdolnosci I", BarColor.WHITE, BarStyle.SOLID);
		wywarZdolnosciBar.addPlayer(rpg.getPlayer());
		wywarZdolnosciBar.setVisible(false);

		wywarInteligencjaBar = Bukkit.createBossBar("§7§oWywar Inteligencji I", BarColor.WHITE, BarStyle.SOLID);
		wywarInteligencjaBar.addPlayer(rpg.getPlayer());
		wywarInteligencjaBar.setVisible(false);

		wywarWalkaBar = Bukkit.createBossBar("§7§oWywar Walki I", BarColor.WHITE, BarStyle.SOLID);
		wywarWalkaBar.addPlayer(rpg.getPlayer());
		wywarWalkaBar.setVisible(false);
	}

	public RpgPlayer getRpg() {
		return rpg;
	}
	
	public boolean hasActiveLocker(RuneLockerTypes locker) {
		return activeLockers.contains(locker);
	}
	
	public void addActiveLocker(RuneLockerTypes locker) {
		if(activeLockers.contains(locker))
			return;
		activeLockers.add(locker);
	}
	
	public void removeActiveLocker(RuneLockerTypes locker) {
		activeLockers.remove(locker);
	}
	
	public boolean hasActiveModifier(EpicModifierTypes modifier) {
		return activeModifiers.contains(modifier);
	}
	
	public void addActiveModifier(EpicModifierTypes modifier) {
		if(activeModifiers.contains(modifier))
			return;
		activeModifiers.add(modifier);
	}
	
	public void removeActiveModifier(EpicModifierTypes modifier) {
		activeModifiers.remove(modifier);
	}
	
	public static enum EpicModifierTypes {
		LODOWA_STRZALA,
		WYOSTRZONE_ZMYSLY,
		OGNISTA_STRZALA,
		ZATRUTA_STRZALA,
		PRECYZYJNY_STRZAL,
		TRANS,
		SZAL_BITEWNY,
		GRUBOSKORNOSC,
		SFERA,
		TOTEM_OBRONNY,
		INKANTACJA,
		SKRYTOBOJSTWO,
		ZADZA_KRWI,
		CIEN_ASSASYNA,
		PROWOKACJA,
		WAMPIRYZM,
		WAMPIRYZM_H,
		WAMPIRYZM_M,
		PENETRACJA,
		POSWIECENIE,
		TARCZA_CIENIA,
		AURA_ROZPROSZENIA,
		RYTUAL_WZNIESIENIA,
		SWIETA_STRZALA,
		ZYCIODAJNA_ZIEMIA,
		ZYCIODAJNA_ZIEMIA_M,
		ZEW_KRWI,
		ZAKAZANY_RYTUAL,
		ZAKAZANY_RYTUAL_H,
		ZAKAZANY_RYTUAL_M,
		RYTUAL_KRWI,
		KREW_PRZODKOW,
		GNIEW,
		TRANSFUZJA,
		PELNIA,
		LODOWY_BLOK,
		ZRODLO_NATURY,
		BARBARZYNSKI_SZAL,
		SILA_JEDNOSCI,
		ZRYW,
		KRWAWA_STRZALA,
		GRAD_STRZAL,
		WEDROWNY_CIEN,
		CIOS_W_PLECY,
		MORD,
		EKSPLODUJACA_STRZALA,
		EKSPLODUJACA_STRZALA_H,
		EKSPLODUJACA_STRZALA_M,
		SILA_ROWNOWAGI,
		SILA_ROWNOWAGI_H,
		SILA_ROWNOWAGI_M,
		KLATWA_KRWI,
		LASKA_BELIARA,
		TRUJACA_AURA,
		ZAKLETA_STRZALA,
		WYBRANIEC_BELIARA,
		WTOPIENIE,
		WTOPIENIE_H,
		WTOPIENIE_M,
		OSTATNI_BOJ,
		PAKT_KRWI,
		PAKT_KRWI_MEASURE,
		SZOSTY_ZMYSL,
		PRZYPLYW_ENERGII,
		OSTATNI_BOJ_H,
		PAKT_KRWI_H,
		PAKT_KRWI_MEASURE_H,
		SZOSTY_ZMYSL_H,
		PRZYPLYW_ENERGII_H,
		OSTATNI_BOJ_M,
		PAKT_KRWI_M,
		PAKT_KRWI_MEASURE_M,
		SZOSTY_ZMYSL_M,
		PRZYPLYW_ENERGII_M,
		ZEW_NATURY,
		SZAL_PRZEDWIECZNYCH,
		SZAL_PRZEDWIECZNYCH_H,
		SZAL_PRZEDWIECZNYCH_M,
		BLOGOSLAWIENSTWO_PRZEDWIECZNYCH,
		BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_H,
		BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_M,
		TAJEMNY_BLASK,
		TAJEMNY_BLASK_M,
		LODOWA_TARCZA,
		LODOWA_TARCZA_H,
		LODOWA_TARCZA_M,
		POTEZNA_RUNA_DOMISIA,
		POTEZNA_RUNA_DOMISIA_H,
		POTEZNA_RUNA_DOMISIA_M,
		SWIATLO,
		AURA_CZYSTOSCI,
		SEKRET_WIELKANOCY,
		LODOWA_AURA
	}

	public void createWywarSilaTask(int seconds, int level) {
		if(wywarSilaTask != null && !wywarSilaTask.isCancelled()) {
			wywarSilaTask.cancel();
		}
		
		wywarSilaBar.setProgress(1);
		wywarSilaBar.setTitle("§7§oWywar Sily "+Utils.toRomeValue(level));
		wywarSilaBar.setVisible(true);
		
		wywarSilaTask = createWywarTask(wywarSilaBar, seconds, (rpg, val) -> setPotionSila(val), level);
	}

	public void createWywarWytrzymaloscTask(int seconds, int level) {
		if(wywarWytrzymaloscTask != null && !wywarWytrzymaloscTask.isCancelled()) {
			wywarWytrzymaloscTask.cancel();
		}
		
		wywarWytrzymaloscBar.setProgress(1);
		wywarWytrzymaloscBar.setTitle("§7§oWywar Wytrzymalosci "+Utils.toRomeValue(level));
		wywarWytrzymaloscBar.setVisible(true);
		
		wywarWytrzymaloscTask = createWywarTask(wywarWytrzymaloscBar, seconds, (rpg, val) -> setPotionWytrzymalosc(val), level);
	}

	public void createWywarZrecznoscTask(int seconds, int level) {
		if(wywarZrecznoscTask != null && !wywarZrecznoscTask.isCancelled()) {
			wywarZrecznoscTask.cancel();
		}
		
		wywarZrecznoscBar.setProgress(1);
		wywarZrecznoscBar.setTitle("§7§oWywar Zrecznosci "+Utils.toRomeValue(level));
		wywarZrecznoscBar.setVisible(true);
		
		wywarZrecznoscTask = createWywarTask(wywarZrecznoscBar, seconds, (rpg, val) -> setPotionZrecznosc(val), level);
	}

	public void createWywarZdolnosciTask(int seconds, int level) {
		if(wywarZdolnosciTask != null && !wywarZdolnosciTask.isCancelled()) {
			wywarZdolnosciTask.cancel();
		}
		
		wywarZdolnosciBar.setProgress(1);
		wywarZdolnosciBar.setTitle("§7§oWywar Zdolnosci "+Utils.toRomeValue(level));
		wywarZdolnosciBar.setVisible(true);
		
		wywarZdolnosciTask = createWywarTask(wywarZdolnosciBar, seconds, (rpg, val) -> setPotionZdolnosci(val), level);
	}

	public void createWywarInteligencjaTask(int seconds, int level) {
		if(wywarInteligencjaTask != null && !wywarInteligencjaTask.isCancelled()) {
			wywarInteligencjaTask.cancel();
		}
		
		wywarInteligencjaBar.setProgress(1);
		wywarInteligencjaBar.setTitle("§7§oWywar Inteligencji "+Utils.toRomeValue(level));
		wywarInteligencjaBar.setVisible(true);
		
		wywarInteligencjaTask = createWywarTask(wywarInteligencjaBar, seconds, (rpg, val) -> setPotionInteligencja(val), level);
	}

	public void createWywarWalkaTask(int seconds, int level) {
		if(wywarWalkaTask != null && !wywarWalkaTask.isCancelled()) {
			wywarWalkaTask.cancel();
		}
		
		wywarWalkaBar.setProgress(1);
		wywarWalkaBar.setTitle("§7§oWywar Walki "+Utils.toRomeValue(level));
		wywarWalkaBar.setVisible(true);
		
		wywarWalkaTask = createWywarTask(wywarWalkaBar, seconds, (rpg, val) -> setPotionWalka(val), level);
	}
	
	private BukkitTask createWywarTask(BossBar bar, int seconds, IWywarSetter setter, int level) {
		setter.setWywarLevel(rpg, level);
		return new BukkitRunnable() {
			int sec = seconds;
			@Override
			public void run() {
				if(sec <= 0) {
					bar.setVisible(false);
					setter.setWywarLevel(rpg, 0);
					
					Player p = rpg.getPlayer();
					p.sendMessage(me.Vark123.EpicRPG.Main.getInstance().getPrefix()+" §eEfekt wywaru skonczyl sie!");
					p.spawnParticle(Particle.SMOKE, p.getLocation().add(0,1,0), 25, .6, .6, .6, 0);
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.8f);
					this.cancel();
					return;
				}
				
				double percent = (double) sec / (double) seconds;
				bar.setProgress(percent);
				
				--sec;
			}
		}.runTaskTimer(Main.getInst(), 0, 20);
	}
	
	public void setPotionSila(int potionSila) {
		this.potionSila = potionSila;
	}

	public void setPotionZrecznosc(int potionZrecznosc) {
		this.potionZrecznosc = potionZrecznosc;
	}

	public void setPotionZdolnosci(int potionZdolnosci) {
		this.potionZdolnosci = potionZdolnosci;
	}

	public void setPotionWytrzymalosc(int potionWytrzymalosc) {
		this.potionWytrzymalosc = potionWytrzymalosc;
	}

	public void setPotionInteligencja(int potionInteligencja) {
		this.potionInteligencja = potionInteligencja;
	}

	public void setPotionWalka(int potionWalka) {
		this.potionWalka = potionWalka;
	}
	
	public int getPotionSila() {
		return potionSila;
	}

	public int getPotionZrecznosc() {
		return potionZrecznosc;
	}

	public int getPotionZdolnosci() {
		return potionZdolnosci;
	}

	public int getPotionWytrzymalosc() {
		return potionWytrzymalosc;
	}

	public int getPotionInteligencja() {
		return potionInteligencja;
	}

	public int getPotionWalka() {
		return potionWalka;
	}

	public void setZewKrwi(boolean zewKrwi) {
		this.zewKrwi = zewKrwi;
	}

	public void setZewKrwiMod(int zewKrwiMod) {
		this.zewKrwiMod = zewKrwiMod;
	}

	public void addZewKrwiMod(double zewKrwiMod) {
		this.zewKrwiMod += zewKrwiMod;
	}

	public void resetZewKrwiMod() {
		this.zewKrwiMod = 0;
	}

	public boolean hasPaktKrwi() {
		return paktKrwi;
	}

	public void setPaktKrwi(boolean paktKrwi) {
		this.paktKrwi = paktKrwi;
	}

	public boolean hasPaktKrwiMeasure() {
		return paktKrwiMeasure;
	}

	public void setPaktKrwiMeasure(boolean paktKrwiMeasure) {
		this.paktKrwiMeasure = paktKrwiMeasure;
	}

	public double getPaktKrwiHp() {
		return paktKrwiHp;
	}

	public void setPaktKrwiHp(double paktKrwiHp) {
		this.paktKrwiHp = paktKrwiHp;
	}

	public boolean hasPaktKrwi_h() {
		return paktKrwi_h;
	}

	public void setPaktKrwi_h(boolean paktKrwi) {
		this.paktKrwi_h = paktKrwi;
	}

	public boolean hasPaktKrwiMeasure_h() {
		return paktKrwiMeasure_h;
	}

	public void setPaktKrwiMeasure_h(boolean paktKrwiMeasure) {
		this.paktKrwiMeasure_h = paktKrwiMeasure;
	}

	public double getPaktKrwiHp_h() {
		return paktKrwiHp_h;
	}

	public void setPaktKrwiHp_h(double paktKrwiHp) {
		this.paktKrwiHp_h = paktKrwiHp;
	}

	public boolean hasPaktKrwi_m() {
		return paktKrwi_m;
	}

	public void setPaktKrwi_m(boolean paktKrwi) {
		this.paktKrwi_m = paktKrwi;
	}

	public boolean hasPaktKrwiMeasure_m() {
		return paktKrwiMeasure_m;
	}

	public void setPaktKrwiMeasure_m(boolean paktKrwiMeasure) {
		this.paktKrwiMeasure_m = paktKrwiMeasure;
	}

	public double getPaktKrwiHp_m() {
		return paktKrwiHp_m;
	}

	public void setPaktKrwiHp_m(double paktKrwiHp) {
		this.paktKrwiHp_m = paktKrwiHp;
	}

	public double getBlogoslawienstwoPrzedwiecznychRenewChance() {
		return blogoslawienstwoPrzedwiecznychRenewChance;
	}

	public void setBlogoslawienstwoPrzedwiecznychRenewChance(double blogoslawienstwoPrzedwiecznychRenewChance) {
		this.blogoslawienstwoPrzedwiecznychRenewChance = blogoslawienstwoPrzedwiecznychRenewChance;
	}

	public static double getBlogoslawienstwoPrzedwiecznychBaseRenewPercent() {
		return BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_BASE_RENEW_PERCENT;
	}

	public double getBlogoslawienstwoPrzedwiecznych_hRenewChance() {
		return blogoslawienstwoPrzedwiecznych_hRenewChance;
	}

	public void setBlogoslawienstwoPrzedwiecznych_hRenewChance(double blogoslawienstwoPrzedwiecznych_hRenewChance) {
		this.blogoslawienstwoPrzedwiecznych_hRenewChance = blogoslawienstwoPrzedwiecznych_hRenewChance;
	}

	public static double getBlogoslawienstwoPrzedwiecznych_hBaseRenewPercent() {
		return BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_H_BASE_RENEW_PERCENT;
	}

	public double getBlogoslawienstwoPrzedwiecznych_mRenewChance() {
		return blogoslawienstwoPrzedwiecznych_mRenewChance;
	}

	public void setBlogoslawienstwoPrzedwiecznych_mRenewChance(double blogoslawienstwoPrzedwiecznych_mRenewChance) {
		this.blogoslawienstwoPrzedwiecznych_mRenewChance = blogoslawienstwoPrzedwiecznych_mRenewChance;
	}

	public static double getBlogoslawienstwoPrzedwiecznych_mBaseRenewPercent() {
		return BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_M_BASE_RENEW_PERCENT;
	}
	
}
