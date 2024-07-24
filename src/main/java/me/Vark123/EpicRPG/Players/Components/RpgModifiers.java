package me.Vark123.EpicRPG.Players.Components;

import java.io.Serializable;
import java.util.Date;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;

public class RpgModifiers implements Serializable {

	private static final long serialVersionUID = -9173795626376278411L;

	private RpgPlayer rpg;
	
	private float bowPower;
	private boolean modifier1_lock;
	@SuppressWarnings("unused")
	private boolean modifier2_lock;
	
	private boolean wyostrzoneZmysly;
	private boolean ognistaStrzala;
	private boolean zatrutaStrzala;
	private boolean precyzyjnyStrzal;
	private boolean lodowaStrzala;
	private boolean trans;
	private boolean szalBitewny;
	private boolean gruboskornosc;
	private boolean sfera;
	private boolean totemObronny;
	private boolean inkantacja;
	private boolean skrytobojstwo;
	private boolean zadzaKrwi;
	private boolean cienAssasyna;
	private boolean prowokacja;
	private boolean wampiryzm;
	private boolean wampiryzm_h;
	private boolean wampiryzm_m;
	private boolean penetracja;
	private boolean poswiecenie;
	private boolean tarczaCienia;
	private boolean auraRozproszenia;
	private boolean rytualWzniesienia;
	private boolean swietaStrzala;
	private boolean zyciodajnaZiemia;
	private boolean zyciodajnaZiemia_m;
	private boolean zakazanyRytual;
	private boolean zakazanyRytual_h;
	private boolean zakazanyRytual_m;
	private boolean zewKrwi;
	private double zewKrwiMod = 0;
	private boolean rytualKrwi;
	private boolean krewPrzodkow;
	private boolean gniew;
	private boolean transfuzja;
	private boolean pelnia;
	private boolean lodowyBlok;
	private boolean zrodloNatury;
	private boolean barbarzynskiSzal;
	private boolean silaJednosci;
	private boolean zryw;
	private boolean krwawaStrzala;
	private boolean gradStrzal;
	private boolean wedrownyCien;
	private boolean ciosWPlecy;
	private boolean mord;
	private boolean eksplodujacaStrzala;
	private boolean silaRownowagi;
	private boolean eksplodujacaStrzala_h;
	private boolean silaRownowagi_h;
	private boolean eksplodujacaStrzala_m;
	private boolean silaRownowagi_m;
	private boolean klatwaKrwi;
	private boolean laskaBeliara;
	private boolean trujacaAura;
	private boolean zakletaStrzala;
	private boolean wybraniecBeliara;
	private boolean wtopienie;
	private boolean wtopienie_h;
	private boolean wtopienie_m;
	private boolean ostatniBoj;
	private boolean paktKrwi;
	private boolean paktKrwiMeasure;
	private double paktKrwiHp;
	private boolean szostyZmysl;
	private boolean przyplywEnergii;
	private boolean ostatniBoj_h;
	private boolean paktKrwi_h;
	private boolean paktKrwiMeasure_h;
	private double paktKrwiHp_h;
	private boolean szostyZmysl_h;
	private boolean przyplywEnergii_h;
	
	private int potionSila;
	private int potionZrecznosc;
	private int potionZdolnosci;
	private int potionWytrzymalosc;
	private int potionInteligencja;
	private int potionWalka;
	
	public RpgModifiers(RpgPlayer rpg) {
		this.rpg = rpg;
	}

	public RpgPlayer getRpg() {
		return rpg;
	}

	public void setPow(float pow) {
		this.bowPower = pow;
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

	public float getBowPower() {
		return bowPower;
	}

	public boolean hasModifier1_lock() {
		return modifier1_lock;
	}

	public boolean hasModifier2_lock() {
		if(!RuneManager.getInstance().getObszarowkiCd().containsKey(rpg.getPlayer()))
			return false;
		Date date = RuneManager.getInstance().getObszarowkiCd().get(rpg.getPlayer());
		if((new Date().getTime()) - date.getTime() < 1000*5)
			return true;
		return false;
	}

	public boolean hasWyostrzoneZmysly() {
		return wyostrzoneZmysly;
	}

	public boolean hasOgnistaStrzala() {
		return ognistaStrzala;
	}

	public boolean hasZatrutaStrzala() {
		return zatrutaStrzala;
	}

	public boolean hasPrecyzyjnyStrzal() {
		return precyzyjnyStrzal;
	}

	public boolean hasLodowaStrzala() {
		return lodowaStrzala;
	}

	public boolean hasTrans() {
		return trans;
	}

	public boolean hasSzalBitewny() {
		return szalBitewny;
	}

	public boolean hasGruboskornosc() {
		return gruboskornosc;
	}

	public boolean hasSfera() {
		return sfera;
	}

	public boolean hasTotemObronny() {
		return totemObronny;
	}

	public boolean hasInkantacja() {
		return inkantacja;
	}

	public boolean hasSkrytobojstwo() {
		return skrytobojstwo;
	}

	public boolean hasZadzaKrwi() {
		return zadzaKrwi;
	}

	public boolean hasCienAssasyna() {
		return cienAssasyna;
	}

	public boolean hasProwokacja() {
		return prowokacja;
	}

	public boolean hasWampiryzm() {
		return wampiryzm;
	}

	public boolean hasWampiryzm_h() {
		return wampiryzm_h;
	}

	public boolean hasWampiryzm_m() {
		return wampiryzm_m;
	}

	public boolean hasPenetracja() {
		return penetracja;
	}

	public boolean hasPoswiecenie() {
		return poswiecenie;
	}

	public boolean hasTarczaCienia() {
		return tarczaCienia;
	}

	public boolean hasAuraRozproszenia() {
		return auraRozproszenia;
	}

	public boolean hasRytualWzniesienia() {
		return rytualWzniesienia;
	}

	public boolean hasSwietaStrzala() {
		return swietaStrzala;
	}

	public boolean hasZyciodajnaZiemia() {
		return zyciodajnaZiemia;
	}

	public boolean hasZyciodajnaZiemia_m() {
		return zyciodajnaZiemia_m;
	}

	public boolean hasZakazanyRytual() {
		return zakazanyRytual;
	}

	public boolean hasZakazanyRytual_h() {
		return zakazanyRytual_h;
	}

	public boolean hasZakazanyRytual_m() {
		return zakazanyRytual_m;
	}

	public boolean hasZewKrwi() {
		return zewKrwi;
	}

	public double getZewKrwiMod() {
		return zewKrwiMod;
	}

	public boolean hasRytualKrwi() {
		return rytualKrwi;
	}

	public boolean hasKrewPrzodkow() {
		return krewPrzodkow;
	}

	public boolean hasGniew() {
		return gniew;
	}

	public boolean hasTransfuzja() {
		return transfuzja;
	}

	public boolean hasPelnia() {
		return pelnia;
	}

	public boolean hasLodowyBlok() {
		return lodowyBlok;
	}

	public boolean hasZrodloNatury() {
		return zrodloNatury;
	}

	public boolean hasBarbarzynskiSzal() {
		return barbarzynskiSzal;
	}

	public boolean hasSilaJednosci() {
		return silaJednosci;
	}

	public boolean hasZryw() {
		return zryw;
	}

	public boolean hasKrwawaStrzala() {
		return krwawaStrzala;
	}

	public boolean hasGradStrzal() {
		return gradStrzal;
	}

	public boolean hasWedrownyCien() {
		return wedrownyCien;
	}

	public boolean hasCiosWPlecy() {
		return ciosWPlecy;
	}

	public boolean hasMord() {
		return mord;
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

	public void setBowPower(float bowPower) {
		this.bowPower = bowPower;
	}

	public void setModifier1_lock(boolean modifier1_lock) {
		this.modifier1_lock = modifier1_lock;
	}

	public void setModifier2_lock(boolean modifier2_lock) {
		this.modifier2_lock = modifier2_lock;
	}

	public void setWyostrzoneZmysly(boolean wyostrzoneZmysly) {
		this.wyostrzoneZmysly = wyostrzoneZmysly;
	}

	public void setOgnistaStrzala(boolean ognistaStrzala) {
		this.ognistaStrzala = ognistaStrzala;
	}

	public void setZatrutaStrzala(boolean zatrutaStrzala) {
		this.zatrutaStrzala = zatrutaStrzala;
	}

	public void setPrecyzyjnyStrzal(boolean precyzyjnyStrzal) {
		this.precyzyjnyStrzal = precyzyjnyStrzal;
	}

	public void setLodowaStrzala(boolean lodowaStrzala) {
		this.lodowaStrzala = lodowaStrzala;
	}

	public void setTrans(boolean trans) {
		this.trans = trans;
	}

	public void setSzalBitewny(boolean szalBitewny) {
		this.szalBitewny = szalBitewny;
	}

	public void setGruboskornosc(boolean gruboskornosc) {
		this.gruboskornosc = gruboskornosc;
	}

	public void setSfera(boolean sfera) {
		this.sfera = sfera;
	}

	public void setTotemObronny(boolean totemObronny) {
		this.totemObronny = totemObronny;
	}

	public void setInkantacja(boolean inkantacja) {
		this.inkantacja = inkantacja;
	}

	public void setSkrytobojstwo(boolean skrytobojstwo) {
		this.skrytobojstwo = skrytobojstwo;
	}

	public void setZadzaKrwi(boolean zadzaKrwi) {
		this.zadzaKrwi = zadzaKrwi;
	}

	public void setCienAssasyna(boolean cienAssasyna) {
		this.cienAssasyna = cienAssasyna;
	}

	public void setProwokacja(boolean prowokacja) {
		this.prowokacja = prowokacja;
	}

	public void setWampiryzm(boolean wampiryzm) {
		this.wampiryzm = wampiryzm;
	}

	public void setWampiryzm_h(boolean wampiryzm_h) {
		this.wampiryzm_h = wampiryzm_h;
	}

	public void setWampiryzm_m(boolean wampiryzm_m) {
		this.wampiryzm_m = wampiryzm_m;
	}

	public void setPenetracja(boolean penetracja) {
		this.penetracja = penetracja;
	}

	public void setPoswiecenie(boolean poswiecenie) {
		this.poswiecenie = poswiecenie;
	}

	public void setTarczaCienia(boolean tarczaCienia) {
		this.tarczaCienia = tarczaCienia;
	}

	public void setAuraRozproszenia(boolean auraRozproszenia) {
		this.auraRozproszenia = auraRozproszenia;
	}

	public void setRytualWzniesienia(boolean rytualWzniesienia) {
		this.rytualWzniesienia = rytualWzniesienia;
	}

	public void setSwietaStrzala(boolean swietaStrzala) {
		this.swietaStrzala = swietaStrzala;
	}

	public void setZyciodajnaZiemia(boolean zyciodajnaZiemia) {
		this.zyciodajnaZiemia = zyciodajnaZiemia;
	}

	public void setZyciodajnaZiemia_m(boolean zyciodajnaZiemia_m) {
		this.zyciodajnaZiemia_m = zyciodajnaZiemia_m;
	}

	public void setZakazanyRytual(boolean zakazanyRytual) {
		this.zakazanyRytual = zakazanyRytual;
	}

	public void setZakazanyRytual_h(boolean zakazanyRytual_h) {
		this.zakazanyRytual_h = zakazanyRytual_h;
	}

	public void setZakazanyRytual_m(boolean zakazanyRytual_m) {
		this.zakazanyRytual_m = zakazanyRytual_m;
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

	public void setRytualKrwi(boolean rytualKrwi) {
		this.rytualKrwi = rytualKrwi;
	}

	public void setKrewPrzodkow(boolean krewPrzodkow) {
		this.krewPrzodkow = krewPrzodkow;
	}

	public void setGniew(boolean gniew) {
		this.gniew = gniew;
	}

	public void setTransfuzja(boolean transfuzja) {
		this.transfuzja = transfuzja;
	}

	public void setPelnia(boolean pelnia) {
		this.pelnia = pelnia;
	}

	public void setLodowyBlok(boolean lodowyBlok) {
		this.lodowyBlok = lodowyBlok;
	}

	public void setZrodloNatury(boolean zrodloNatury) {
		this.zrodloNatury = zrodloNatury;
	}

	public void setBarbarzynskiSzal(boolean barbarzynskiSzal) {
		this.barbarzynskiSzal = barbarzynskiSzal;
	}

	public void setSilaJednosci(boolean silaJednosci) {
		this.silaJednosci = silaJednosci;
	}

	public void setZryw(boolean zryw) {
		this.zryw = zryw;
	}

	public void setKrwawaStrzala(boolean krwawaStrzala) {
		this.krwawaStrzala = krwawaStrzala;
	}

	public void setGradStrzal(boolean gradStrzal) {
		this.gradStrzal = gradStrzal;
	}

	public void setWedrownyCien(boolean wedrownyCien) {
		this.wedrownyCien = wedrownyCien;
	}

	public void setCiosWPlecy(boolean ciosWPlecy) {
		this.ciosWPlecy = ciosWPlecy;
	}

	public void setMord(boolean mord) {
		this.mord = mord;
	}

	public boolean hasEksplodujacaStrzala() {
		return eksplodujacaStrzala;
	}

	public void setEksplodujacaStrzala(boolean eksplodujacaStrzala) {
		this.eksplodujacaStrzala = eksplodujacaStrzala;
	}

	public boolean hasSilaRownowagi() {
		return silaRownowagi;
	}

	public void setSilaRownowagi(boolean silaRownowagi) {
		this.silaRownowagi = silaRownowagi;
	}

	public boolean hasEksplodujacaStrzala_h() {
		return eksplodujacaStrzala_h;
	}

	public void setEksplodujacaStrzala_h(boolean eksplodujacaStrzala_h) {
		this.eksplodujacaStrzala_h = eksplodujacaStrzala_h;
	}

	public boolean hasSilaRownowagi_h() {
		return silaRownowagi_h;
	}

	public void setSilaRownowagi_h(boolean silaRownowagi_h) {
		this.silaRownowagi_h = silaRownowagi_h;
	}

	public boolean hasEksplodujacaStrzala_m() {
		return eksplodujacaStrzala_m;
	}

	public void setEksplodujacaStrzala_m(boolean eksplodujacaStrzala_m) {
		this.eksplodujacaStrzala_m = eksplodujacaStrzala_m;
	}

	public boolean hasSilaRownowagi_m() {
		return silaRownowagi_m;
	}

	public void setSilaRownowagi_m(boolean silaRownowagi_m) {
		this.silaRownowagi_m = silaRownowagi_m;
	}

	public boolean hasKlatwaKrwi() {
		return klatwaKrwi;
	}

	public void setKlatwaKrwi(boolean klatwaKrwi) {
		this.klatwaKrwi = klatwaKrwi;
	}

	public boolean hasLaskaBeliara() {
		return laskaBeliara;
	}

	public void setLaskaBeliara(boolean laskaBeliara) {
		this.laskaBeliara = laskaBeliara;
	}

	public boolean hasTrujacaAura() {
		return trujacaAura;
	}

	public void setTrujacaAura(boolean trujacaAura) {
		this.trujacaAura = trujacaAura;
	}

	public boolean hasZakletaStrzala() {
		return zakletaStrzala;
	}

	public void setZakletaStrzala(boolean zakletaStrzala) {
		this.zakletaStrzala = zakletaStrzala;
	}

	public boolean hasWybraniecBeliara() {
		return wybraniecBeliara;
	}

	public void setWybraniecBeliara(boolean wybraniecBeliara) {
		this.wybraniecBeliara = wybraniecBeliara;
	}

	public boolean hasWtopienie() {
		return wtopienie;
	}

	public void setWtopienie(boolean wtopienie) {
		this.wtopienie = wtopienie;
	}

	public boolean hasWtopienie_h() {
		return wtopienie_h;
	}

	public void setWtopienie_h(boolean wtopienie_h) {
		this.wtopienie_h = wtopienie_h;
	}

	public boolean hasWtopienie_m() {
		return wtopienie_m;
	}

	public void setWtopienie_m(boolean wtopienie_m) {
		this.wtopienie_m = wtopienie_m;
	}

	public boolean hasOstatniBoj() {
		return ostatniBoj;
	}

	public void setOstatniBoj(boolean ostatniBoj) {
		this.ostatniBoj = ostatniBoj;
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

	public boolean hasSzostyZmysl() {
		return szostyZmysl;
	}

	public void setSzostyZmysl(boolean szostyZmysl) {
		this.szostyZmysl = szostyZmysl;
	}

	public boolean hasPrzyplywEnergii() {
		return przyplywEnergii;
	}

	public void setPrzyplywEnergii(boolean przyplywEnergii) {
		this.przyplywEnergii = przyplywEnergii;
	}

	public boolean hasOstatniBoj_h() {
		return ostatniBoj_h;
	}

	public void setOstatniBoj_h(boolean ostatniBoj) {
		this.ostatniBoj_h = ostatniBoj;
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

	public boolean hasSzostyZmysl_h() {
		return szostyZmysl_h;
	}

	public void setSzostyZmysl_h(boolean szostyZmysl) {
		this.szostyZmysl_h = szostyZmysl;
	}

	public boolean hasPrzyplywEnergii_h() {
		return przyplywEnergii_h;
	}

	public void setPrzyplywEnergii_h(boolean przyplywEnergii) {
		this.przyplywEnergii_h = przyplywEnergii;
	}
	
}
