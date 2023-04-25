package me.Vark123.EpicRPG.Players;

public class RpgModifiers {

	private RpgPlayer rpg;
	
	private float bowPower;
	private boolean modifier1_lock;
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
	private int zewKrwiMod = 0;
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
		return modifier2_lock;
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

	public int getZewKrwiMod() {
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

	public void addZewKrwiMod(int zewKrwiMod) {
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
}
