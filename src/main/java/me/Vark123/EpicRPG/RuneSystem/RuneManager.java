package me.Vark123.EpicRPG.RuneSystem;

import java.util.Arrays;
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
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune.RuneLockerTypes;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastCostCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastGlobalCdCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastRuneCdCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneUseEvent;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.BlogoslawienstwoPrzedwiecznych;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.BlogoslawienstwoPrzedwiecznych_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.BlogoslawienstwoPrzedwiecznych_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.MasoweZniszczenie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.RozerwanieDuszy;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.Spetanie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.SzalPrzedwiecznych;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.SzalPrzedwiecznych_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.SzalPrzedwiecznych_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.SzalPustki;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.SzeptNZotha;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.SzeptNZotha_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.SzeptNZotha_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.SzeptPrzedwiecznych;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.ZakazanyRytual;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.ZakazanyRytual_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.ZakazanyRytual_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.BarbarzynskiSzal;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.Drenaz;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.Furia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.Gniew;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.GniewPrzodkow;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.KlatwaKrwi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.KrewPrzodkow;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.KrwawaFala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.KrwawaStrzala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.KrwawyBicz;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.KrwawyDeszcz;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.KrwawyPocisk;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.Mord;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.PlugawaKrew;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.Rozprucie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.RytualKrwi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.SpiralaKrwi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.Transfuzja;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.Wampiryzm;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.Wampiryzm_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.Wampiryzm_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.WiezyKrwi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.ZadzaKrwi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.ZatrutaKrew;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.ZewSmierci;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.ZewSmierci_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.ZewSmierci_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.CienAssasyna;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.CiosWPlecy;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.CukierekAlboPsikus;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.KrzykUmarlych;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.KulaSmierci;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.OdwrocenieUwagi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.Skrytobojstwo;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.SmiercOzywiencom;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.SmiertelnaFala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.StrzalaCiemnosci;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.StrzalaMroku;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.SwietyMrok;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.SzalBeliara;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.SzponBeliaraInt;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.SzponBeliaraMana;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.TarczaCienia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.TchnienieSmierci;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.TrupiJek;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.UderzenieCienia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.UkazanieSmierci;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.UsciskUmarlych;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.WedrownyCien;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.WloczniaCiemnosci;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.WybraniecBeliara;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.Wyssanie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.Zaglada;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.ZeslanieMroku;
import me.Vark123.EpicRPG.RuneSystem.Runes.Mrok.Zmrok;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.BankaEnergii;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Blyskawica;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.EksplodujacaStrzala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.EksplodujacaStrzala_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.EksplodujacaStrzala_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Eksplozja;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.FalaElektryczna;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.FalaUderzeniowa;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Gruboskornosc;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.GrupoweLeczenie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Kataklizm;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Korzen;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Leczenie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Lowy;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Lowy_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Lowy_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.MalaBlyskawica;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Meteor;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.NocWDzien;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Penetracja;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.PiachWOczy;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.PiorunKulisty;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.PorazenieElektryczne;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.PrecyzyjnyStrzal;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.PrzywolanieBlyskawicy;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Rezonans;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.RojOwadow;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.SekretWielkanocy;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.SzostyZmysl;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.SzostyZmysl_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.SzostyZmysl_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Sztorm;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.ToksycznaChmura;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.TotemObronny;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.TrujacaAura;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.TrujaceUkaszenie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.TrzesienieZiemi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.UderzenieBurzy;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.UderzenieWiatru;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.WiazkaElektryczna;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Wir;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.WstrzasElektryczny;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Wtopienie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Wtopienie_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Wtopienie_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.Zacma;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.ZatrutaStrzala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.ZdrojZycia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.ZewNatury;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.ZlodziejEnergii;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.ZrodloNatury;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.ZyciodajnaZiemia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Natura.ZyciodajnaZiemia_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.AuraRozproszenia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.BurzaOgnista;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.DeszczOgnia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.DuzaBurzaOgnista;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.DuzaKulaOgnia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.Inkantacja;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.KulaOgnia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.MalaBurzaOgnista;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.MasowaPirokineza;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.OgnistaFala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.OgnistaSfera;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.OgnistaStrzala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.OgnistaStrzalaLcz;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.OgnistyWybuch;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.Pirokineza;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.PlomienLenga;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.Rozerwanie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.WiecznyOgien;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.WulkanicznyGejzer;
import me.Vark123.EpicRPG.RuneSystem.Runes.Rownowaga.KamiennyObserwator;
import me.Vark123.EpicRPG.RuneSystem.Runes.Rownowaga.SferaCorristo;
import me.Vark123.EpicRPG.RuneSystem.Runes.Rownowaga.SilaRownowagi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Rownowaga.SilaRownowagi_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Rownowaga.SilaRownowagi_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Summons.ObudzenieGolema;
import me.Vark123.EpicRPG.RuneSystem.Runes.Summons.PrzyzwanieDzika;
import me.Vark123.EpicRPG.RuneSystem.Runes.Summons.PrzyzwanieGoblina;
import me.Vark123.EpicRPG.RuneSystem.Runes.Summons.PrzyzwanieNiedzwiedzia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Summons.PrzyzwanieSzkieleta;
import me.Vark123.EpicRPG.RuneSystem.Runes.Summons.PrzyzwanieWilka;
import me.Vark123.EpicRPG.RuneSystem.Runes.Summons.PrzyzwanieZombie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.AuraCzystosci;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.BlogoslawionaZiemia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.Czystka;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.OstatniBoj;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.OstatniBoj_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.OstatniBoj_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.Poswiecenie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.RytualWzniesienia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.SilaJednosci;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.Swiatlo;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.SwietaStrzala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.SwietaStrzalaLcz;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.SwieteSlowo;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.SwietyPlomien;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.SwietyPocisk;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.SzalBitewny;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.Trans;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.WloczniaElysian;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.WypedzanieZla;
import me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo.ZniszczenieZla;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.Haduoken;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.MagicznaIskra;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.MagicznaSfera;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.MagicznePociski;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.MagicznyPocisk;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.PoteznaRunaDomisia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.PoteznaRunaDomisia_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.PoteznaRunaDomisia_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.PozeraczDusz;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.PozeraczDusz_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.PozeraczDusz_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.Prowokacja;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.TajemnyBlask;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.TajemnyBlask_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.TajemnyGrad;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.TajemnyGrad_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.TajemnyGrad_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.TeleportacjaKrotkodystansowa;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.UderzenieChi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.WyostrzoneZmysly;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.ZakletaStrzala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna.Zryw;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.BrylaLodu;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.EksplozjaLodu;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.FalaMrozu;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.Gejzer;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.Grom;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowaAura;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowaFala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowaLanca;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowaStrzala;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowaStrzalaLcz;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowaTarcza;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowaTarcza_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowaTarcza_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowaWlocznia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowePrzebicie;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowyBlok;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.LodowyPocisk;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.Pelnia;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.SopelLodu;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.WodnaPiesc;
import me.Vark123.EpicRPG.RuneSystem.Runes.Woda.Zamiec;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public final class RuneManager {

	private static final RuneManager instance = new RuneManager();
	
	public final long RUNE_GLOBAL_CD_DEFAULT_VALUE = 1000;
	
	private final Map<UUID, Date> globalCd;
	private final Map<UUID, Map<String, Date>> runeCd;
	
	private RuneManager() {
		globalCd = new ConcurrentHashMap<>();
		runeCd = new ConcurrentHashMap<>();
	}
	
	public static final RuneManager get() {
		return instance;
	}
	
	public boolean tryCastRune(RpgPlayer rpg, ItemStack itRune) {
		Player p = rpg.getPlayer();
		if(hasGlobalCd(p))
			return false;
		
		if(!Utils.canUseItem(itRune, p)) {
			p.sendMessage("§7[§bEpicRPG§7] §cTa runa jest przypisana do kogos innego! Nie mozesz jej uzyc!");
			return false;
		}
		
		EpicRune rune = new EpicRune(itRune);
		if(rune.isClassRequired()) {
			String proffesion = ChatColor.stripColor(rpg.getInfo().getProffesion());
			if(!proffesion.equalsIgnoreCase(rune.getKlasa())) {
				p.sendMessage("§7[§bEpicRPG§7] §cTylko "+rune.getKlasa()+" §cmoze uzyc tej runy!");
				return false;
			}
		}
		
		if(rpg.getStats().getKrag() < rune.getKrag()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cMusisz posiadac §7§o"+rune.getKrag()+" §ckrag magii, by uzyc "+rune.getName());
			return false;
		}
		
		if(!rune.canUseInPlayerLocation(p)) {
			return false;
		}
		
		if(!isRegenTimePassed(p, rune)) {
			return false;
		}
		
		if(rune.getSummonPoints() > rpg.getStats().getCurrentSummonPoints()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cMusisz miec przynajmnie §7§o"+rune.getSummonPoints()+" §cwolne Punkty Przywolan, by uzyc "+rune.getName());
			return false;
		}
		
		RuneCastCostCalcEvent calcCostEvent = new RuneCastCostCalcEvent(rpg, rune);
		Bukkit.getPluginManager().callEvent(calcCostEvent);
		if(calcCostEvent.isCancelled())
			return false;
		
		int finalCost = calcCostEvent.getFinalCost();
		RpgModifiers modifiers = rpg.getModifiers();
		if(!rune.getMythicType().toLowerCase().startsWith("tajemnyblask")) {
			String profession = rpg.getInfo().getProffesion();
			if(Utils.hasEntityBuff(p, EpicModifierTypes.TAJEMNY_BLASK_M) &&
					profession.equals("§5Mag"))
				finalCost = (int) Math.min(finalCost, (rune.getPrice() * 0.6));
			else if(Utils.hasEntityBuff(p, EpicModifierTypes.TAJEMNY_BLASK) &&
					profession.equals("§5Mag"))
				finalCost = (int) Math.min(finalCost, (rune.getPrice() * 0.8));
		}
		
		if(rune.isHpInsteadMana()) {
			if(!hasEnoughHp(rpg, finalCost))
				return false;
		} else {
			if(!hasEnoughMana(rpg, finalCost))
				return false;
		}
		
		ACastableRune castableRune = getRune(rpg, rune, itRune);
		
		for(RuneLockerTypes locker : castableRune.lockers) {
			if(modifiers.hasActiveLocker(locker)) {
				p.sendMessage(Main.getInstance().getPrefix()+" "+locker.getMessage());
				return false;
			}
		}
		
		createGlobalCooldown(rpg, rune);
		
		if(!isSilaZywiolowEffect(rpg)) {
			if(rune.isHpInsteadMana())
				spendHp(rpg, finalCost);
			else
				spendMana(rpg, finalCost);
			createRegenTime(rpg, rune);
		} else {
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.5f);
			p.getWorld().spawnParticle(Particle.NAUTILUS, p.getLocation().add(0, 1, 0), 20, 0.75, 0.5, 0.75, 0.2);
		}
		
		RuneUseEvent useEvent = new RuneUseEvent(rpg, rune);
		Bukkit.getPluginManager().callEvent(useEvent);

		if(Utils.hasEntityBuff(p, EpicModifierTypes.LODOWY_BLOK))
			Utils.unsetEntityBuff(p, EpicModifierTypes.LODOWY_BLOK);
		
		castableRune.castSpell();
		
		return true;
	}
	
	public boolean isRegenTimePassed(Player p, ItemStack itRune) {
		if(itRune == null || itRune.getType().equals(Material.AIR))
			return false;
		
		if(hasGlobalCd(p))
			return false;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		EpicRune rune = new EpicRune(itRune);
		if(rune.isClassRequired()) {
			String proffesion = ChatColor.stripColor(rpg.getInfo().getProffesion());
			if(!proffesion.equalsIgnoreCase(rune.getKlasa())) {
				p.sendMessage(Main.getInstance().getPrefix()+" §cTylko "+rune.getKlasa()+" §cmoze uzyc tej runy!");
				return false;
			}
		}

		if(rpg.getStats().getKrag() < rune.getKrag()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cMusisz posiadac §7§o"+rune.getKrag()+" §ckrag magii, by uzyc "+rune.getName());
			return false;
		}
		
		if(!isRegenTimePassed(p, rune))
			return false;
		
		p.sendMessage(Main.getInstance().getPrefix()+" §aRuna §r§f"+rune.getName()+" §ajest gotowa do uzycia");
		return true;
	}
	
	public boolean isRegenTimePassed(Player p, EpicRune rune) {
		UUID uid = p.getUniqueId();
		String mythicType = rune.getMythicType();
		if(!runeCd.containsKey(uid) 
				|| !runeCd.get(uid).containsKey(mythicType))
			return true;
		
		Date regenDate = runeCd.get(uid).get(mythicType);
		Date now = new Date();
		if(now.before(regenDate)) {
			double cooldownInSeconds = (double)(regenDate.getTime() - now.getTime())/1000.;
			if(cooldownInSeconds < 2) {
				p.playSound(p, Sound.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT, 2, .7f);
				p.spawnParticle(Particle.SMOKE, p.getLocation().add(0,1,0), 6, .4f, .4f, .4f, .03f);
			}
			
			p.sendMessage("§7[§6EpicRPG§7] §cRuny "+rune.getName()+" §cbedziesz mogl uzyc za §7"+String.format("%.2f", cooldownInSeconds)+" §csekund");
			return false;
		}
		return true;
	}
	
	public void createRegenTime(RpgPlayer rpgPlayer, EpicRune rune) {
		Player p = rpgPlayer.getPlayer();
		UUID uid = p.getUniqueId();
		if(!runeCd.containsKey(uid)) {
			runeCd.put(uid, new ConcurrentHashMap<>());
		}
		
		RuneCastRuneCdCalcEvent event = new RuneCastRuneCdCalcEvent(rpgPlayer, rune);
		Bukkit.getPluginManager().callEvent(event);

		Date now = new Date();
		long cd = event.getFinalCd();
		
		String profession = rpgPlayer.getInfo().getProffesion();
		if(Utils.hasEntityBuff(p, EpicModifierTypes.TAJEMNY_BLASK_M) &&
				profession.equals("§5Mag"))
			cd = (long) (rune.getRegenTime() * 0.35 * 1000);
		else if(Utils.hasEntityBuff(p, EpicModifierTypes.TAJEMNY_BLASK) &&
				profession.equals("§5Mag"))
			cd = (long) (rune.getRegenTime() * 0.5 * 1000);
		
		Date cdDate = new Date(now.getTime() + cd);
		runeCd.get(uid).put(rune.getMythicType(), cdDate);
	}

	public boolean hasGlobalCd(Player p) {
		return globalCd.containsKey(p.getUniqueId()) && globalCd.get(p.getUniqueId()).after(new Date());
	}
	
	public void createGlobalCooldown(RpgPlayer rpgPlayer, EpicRune rune) {
		RuneCastGlobalCdCalcEvent event = new RuneCastGlobalCdCalcEvent(rpgPlayer, rune);
		Bukkit.getPluginManager().callEvent(event);
		
		Date now = new Date();
		long globalCd = event.getFinalGlobalCd();
		
		Date cd = new Date(now.getTime() + globalCd);
		this.globalCd.put(rpgPlayer.getPlayer().getUniqueId(), cd);
	}
	
	public boolean hasEnoughHp(RpgPlayer rpg, int cost) {
		Player p = rpg.getPlayer();
		if(!rpg.getSkills().hasMagKrwi()) {
			p.sendMessage("§7[§6EpicRPG§7] §cMusisz byc magiem krwi, by moc uzyc tej runy!");
			return false;
		}
		
		double presentHealth = p.getHealth();
		if(cost < (presentHealth + 1))
			return true;
		
		p.sendMessage("§7[§6EpicRPG§7] §cNie masz wystarczajaco zycia by uzyc tej runy!");
		return false;
		
	}
	
	public void spendHp(RpgPlayer rpg, int cost) {
		Utils.takeEntityHp(rpg.getPlayer(), cost);
	}
	
	public boolean hasEnoughMana(RpgPlayer rpg, int cost) {
		Player p = rpg.getPlayer();
		RpgStats stats = rpg.getStats();
		
		if(stats.getPresentMana() >= cost)
			return true;
		
		if(rpg.getSkills().hasMagKrwi()) {
			if(cost > stats.getFinalMana()) {
				p.sendMessage("§7[§6EpicRPG§7] §cMasz za malo maksymalnej many, by uzyc tej runy jako Mag Krwi!");
				return false;
			}
			
			cost = (int) Math.ceil(cost*0.25);
			double presentHealth = p.getHealth();
			if(cost < (presentHealth + 1))
				return true;
			
			p.sendMessage("§7[§6EpicRPG§7] §cNie masz ani many, ani zycia by uzyc tej runy!");
			return false;
		}

		p.sendMessage("§7[§6EpicRPG§7] §cMasz za malo many by uzyc tej runy!");
		return false;
	}
	
	public void spendMana(RpgPlayer rpg, int cost) {
		RpgStats stats = rpg.getStats();
		if(stats.getPresentMana() >= cost) {
			stats.removePresentManaSmart(cost);
			return;
		}

		cost = (int) Math.ceil(cost*0.25);
		spendHp(rpg, cost);
	}
	
	public boolean isSilaZywiolowEffect(RpgPlayer rpgPlayer) {
		if(!rpgPlayer.getSkills().hasSilaZywiolow())
			return false;
		
		Random rand = new Random();
		return (rand.nextInt(100) < 5);
	}
	
	public ACastableRune getRune(RpgPlayer rpgPlayer, EpicRune rune, ItemStack itRune) {
		switch(itRune.getType()) {
			case MUSIC_DISC_11:
				switch(rune.getMythicType()) {
					case "CukierekAlboPsikus":			return new CukierekAlboPsikus(rpgPlayer, rune);
					case "StrzalaMroku":				return new StrzalaMroku(rpgPlayer, rune);
					case "StrzalaCiemnosci":			return new StrzalaCiemnosci(rpgPlayer, rune);
					case "SmiercOzywiencom":			return new SmiercOzywiencom(rpgPlayer, rune);
					case "WloczniaCiemnosci":			return new WloczniaCiemnosci(rpgPlayer, rune);
					case "Wyssanie":					return new Wyssanie(rpgPlayer, rune);
					case "TchnienieSmierci":			return new TchnienieSmierci(rpgPlayer, rune);
					case "Zmrok":						return new Zmrok(rpgPlayer, rune);
					case "SmiertelnaFala":				return new SmiertelnaFala(rpgPlayer, rune);
					case "UderzenieCienia":				return new UderzenieCienia(rpgPlayer, rune);
					case "KulaSmierci":					return new KulaSmierci(rpgPlayer, rune);
					case "TarczaCienia":				return new TarczaCienia(rpgPlayer, rune);
					case "TrupiJek":					return new TrupiJek(rpgPlayer, rune);
					case "UkazanieSmierci":				return new UkazanieSmierci(rpgPlayer, rune);
					case "ZeslanieMroku":				return new ZeslanieMroku(rpgPlayer, rune);
					case "ZeslanieMroku_H":				return new ZeslanieMroku(rpgPlayer, rune);
					case "ZeslanieMroku_M":				return new ZeslanieMroku(rpgPlayer, rune);
					case "RozerwanieDuszy":				return new RozerwanieDuszy(rpgPlayer, rune);
					case "Spetanie":					return new Spetanie(rpgPlayer, rune);
					case "Spetanie_H":					return new Spetanie(rpgPlayer, rune);
					case "Spetanie_M":					return new Spetanie(rpgPlayer, rune);
					case "MasoweZniszczenie":			return new MasoweZniszczenie(rpgPlayer, rune);
					case "KrzykUmarlych":				return new KrzykUmarlych(rpgPlayer, rune);
					case "UsciskUmarlych":				return new UsciskUmarlych(rpgPlayer, rune);
					case "UsciskUmarlych_H":			return new UsciskUmarlych(rpgPlayer, rune);
					case "UsciskUmarlych_M":			return new UsciskUmarlych(rpgPlayer, rune);
					case "Zaglada":						return new Zaglada(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_13:
				switch(rune.getMythicType()) {
					case "MagicznyPocisk":				return new MagicznyPocisk(rpgPlayer, rune);
					case "MagicznaIskra":				return new MagicznaIskra(rpgPlayer, rune);
					case "MagicznePociski":				return new MagicznePociski(rpgPlayer, rune);
					case "MalaBlyskawica":				return new MalaBlyskawica(rpgPlayer, rune);
					case "Blyskawica":					return new Blyskawica(rpgPlayer, rune);
					case "GromG1":						return new WiazkaElektryczna(rpgPlayer, rune);
					case "FalaElektryczna":				return new FalaElektryczna(rpgPlayer, rune);
					case "PorazenieElektryczne":		return new PorazenieElektryczne(rpgPlayer, rune);
					case "BankaEnergii":				return new BankaEnergii(rpgPlayer, rune);
					case "PiorunKulisty":				return new PiorunKulisty(rpgPlayer, rune);
					case "PrzywolanieBlyskawicy":		return new PrzywolanieBlyskawicy(rpgPlayer, rune);
					case "Sztorm":						return new Sztorm(rpgPlayer, rune);
					case "WstrzasElektryczny":			return new WstrzasElektryczny(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_5:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_BLOCKS:
				switch(rune.getMythicType()) {
					case "Gejzer":						return new Gejzer(rpgPlayer, rune);
					case "WodnaPiesc":					return new WodnaPiesc(rpgPlayer, rune);
					case "Haduoken":					return new Haduoken(rpgPlayer, rune);
					case "UderzenieChi":				return new UderzenieChi(rpgPlayer, rune);
					case "PiachWOczy":					return new PiachWOczy(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_CAT:
				switch(rune.getMythicType()) {
					case "PoteznaRunaDomisia":			return new PoteznaRunaDomisia(rpgPlayer, rune);
					case "PoteznaRunaDomisia_H":		return new PoteznaRunaDomisia_H(rpgPlayer, rune);
					case "PoteznaRunaDomisia_M":		return new PoteznaRunaDomisia_M(rpgPlayer, rune);
					case "ZyciodajnaZiemia":			return new ZyciodajnaZiemia(rpgPlayer, rune);
					case "ZyciodajnaZiemia_M":			return new ZyciodajnaZiemia_M(rpgPlayer, rune);
					case "Leczenie":					return new Leczenie(rpgPlayer, rune);
					case "ZlodziejEnergii":				return new ZlodziejEnergii(rpgPlayer, rune);
					case "ZdrojZycia":					return new ZdrojZycia(rpgPlayer, rune);
					case "GrupoweLeczenie":				return new GrupoweLeczenie(rpgPlayer, rune);
					case "LodowaTarcza":				return new LodowaTarcza(rpgPlayer, rune);
					case "LodowaTarcza_H":				return new LodowaTarcza_H(rpgPlayer, rune);
					case "LodowaTarcza_M":				return new LodowaTarcza_M(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_CHIRP:
				switch(rune.getMythicType()) {
					case "LodowaStrzala":				return new LodowaStrzala(rpgPlayer, rune);
					case "SopelLodu":					return new SopelLodu(rpgPlayer, rune);
					case "BrylaLodu":					return new BrylaLodu(rpgPlayer, rune);
					case "LodowaLanca":					return new LodowaLanca(rpgPlayer, rune);
					case "LodowaWlocznia":				return new LodowaWlocznia(rpgPlayer, rune);
					case "LodowePrzebicie":				return new LodowePrzebicie(rpgPlayer, rune);
					case "LodowyPocisk":				return new LodowyPocisk(rpgPlayer, rune);
					case "Pelnia":						return new Pelnia(rpgPlayer, rune);
					case "EksplozjaLodu":				return new EksplozjaLodu(rpgPlayer, rune);
					case "LodowaFala":					return new LodowaFala(rpgPlayer, rune);
					case "FalaMrozu":					return new FalaMrozu(rpgPlayer, rune);
					case "Grom":						return new Grom(rpgPlayer, rune);
					case "Zamiec":						return new Zamiec(rpgPlayer, rune);
					case "LodowyBlok":					return new LodowyBlok(rpgPlayer, rune);
				}
				
				break;
			case MUSIC_DISC_CREATOR:
				switch(rune.getMythicType()) {
					case "PrzyzwanieWilka":				return new PrzyzwanieWilka(rpgPlayer, rune);
					case "PrzyzwanieDzika":				return new PrzyzwanieDzika(rpgPlayer, rune);
					case "PrzyzwanieNiedzwiedzia":		return new PrzyzwanieNiedzwiedzia(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_CREATOR_MUSIC_BOX:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_FAR:
				switch(rune.getMythicType()) {
					case "SwietyMrok":					return new SwietyMrok(rpgPlayer, rune);
					case "WybraniecBeliara":			return new WybraniecBeliara(rpgPlayer, rune);
					case "Prowokacja":					return new Prowokacja(rpgPlayer, rune);
					case "Zryw":						return new Zryw(rpgPlayer, rune);
					case "SwietaStrzala":				return new SwietaStrzala(rpgPlayer, rune);
					case "Poswiecenie":					return new Poswiecenie(rpgPlayer, rune);
					case "RytualWzniesienia":			return new RytualWzniesienia(rpgPlayer, rune);
					case "SwietyPocisk":				return new SwietyPocisk(rpgPlayer, rune);
					case "Trans":						return new Trans(rpgPlayer, rune);
					case "WypedzanieZla":				return new WypedzanieZla(rpgPlayer, rune);
					case "BlogoslawionaZiemia":			return new BlogoslawionaZiemia(rpgPlayer, rune);
					case "SilaJednosci":				return new SilaJednosci(rpgPlayer, rune);
					case "SwieteSlowo":					return new SwieteSlowo(rpgPlayer, rune);
					case "SwietyPlomien":				return new SwietyPlomien(rpgPlayer, rune);
					case "SzalBitewny":					return new SzalBitewny(rpgPlayer, rune);
					case "ZniszczenieZla":				return new ZniszczenieZla(rpgPlayer, rune);
					case "AuraCzystosci":				return new AuraCzystosci(rpgPlayer, rune);
					case "OstatniBoj":					return new OstatniBoj(rpgPlayer, rune);
					case "OstatniBoj_H":				return new OstatniBoj_H(rpgPlayer, rune);
					case "OstatniBoj_M":				return new OstatniBoj_M(rpgPlayer, rune);
					case "BlogoslawienstwoPrzedwiecznych":return new BlogoslawienstwoPrzedwiecznych(rpgPlayer, rune);
					case "BlogoslawienstwoPrzedwiecznych_H":return new BlogoslawienstwoPrzedwiecznych_H(rpgPlayer, rune);
					case "BlogoslawienstwoPrzedwiecznych_M":return new BlogoslawienstwoPrzedwiecznych_M(rpgPlayer, rune);
					case "Gruboskornosc":				return new Gruboskornosc(rpgPlayer, rune);
					case "BarbarzynskiSzal":			return new BarbarzynskiSzal(rpgPlayer, rune);
					case "Wampiryzm":					return new Wampiryzm(rpgPlayer, rune, Arrays.asList(RuneLockerTypes.WAMPIRYZM));
					case "Wampiryzm_H":					return new Wampiryzm_H(rpgPlayer, rune, Arrays.asList(RuneLockerTypes.WAMPIRYZM));
					case "Wampiryzm_M":					return new Wampiryzm_M(rpgPlayer, rune, Arrays.asList(RuneLockerTypes.WAMPIRYZM));
					case "ZadzaKrwi":					return new ZadzaKrwi(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_MALL:
				switch(rune.getMythicType()) {
					case "OgnistaStrzalaLcz":			return new OgnistaStrzalaLcz(rpgPlayer, rune);
					case "LodowaStrzalaLcz":			return new LodowaStrzalaLcz(rpgPlayer, rune);
					case "CiosWPlecy":					return new CiosWPlecy(rpgPlayer, rune);
					case "CienAssasyna":				return new CienAssasyna(rpgPlayer, rune);
					case "OdwrocenieUwagi":				return new OdwrocenieUwagi(rpgPlayer, rune);
					case "Skrytobojstwo":				return new Skrytobojstwo(rpgPlayer, rune);
					case "WedrownyCien":				return new WedrownyCien(rpgPlayer, rune);
					case "WyostrzoneZmysly":			return new WyostrzoneZmysly(rpgPlayer, rune);
					case "ZakletaStrzala":				return new ZakletaStrzala(rpgPlayer, rune);
					case "SwietaStrzalaLcz":			return new SwietaStrzalaLcz(rpgPlayer, rune);
					case "SzalPrzedwiecznych":			return new SzalPrzedwiecznych(rpgPlayer, rune);
					case "SzalPrzedwiecznych_H":		return new SzalPrzedwiecznych_H(rpgPlayer, rune);
					case "SzalPrzedwiecznych_M":		return new SzalPrzedwiecznych_M(rpgPlayer, rune);
					case "TrujacaAura":					return new TrujacaAura(rpgPlayer, rune);
					case "ZatrutaStrzala":				return new ZatrutaStrzala(rpgPlayer, rune);
					case "Lowy":						return new Lowy(rpgPlayer, rune);
					case "Lowy_H":						return new Lowy_H(rpgPlayer, rune);
					case "Lowy_M":						return new Lowy_M(rpgPlayer, rune);
					case "Zacma":						return new Zacma(rpgPlayer, rune);
					case "Penetracja":					return new Penetracja(rpgPlayer, rune);
					case "PrecyzyjnyStrzal":			return new PrecyzyjnyStrzal(rpgPlayer, rune);
					case "EksplodujacaStrzala":			return new EksplodujacaStrzala(rpgPlayer, rune, Arrays.asList(RuneLockerTypes.EKSPLODUJACA_STRZALA));
					case "EksplodujacaStrzala_H":		return new EksplodujacaStrzala_H(rpgPlayer, rune, Arrays.asList(RuneLockerTypes.EKSPLODUJACA_STRZALA));
					case "EksplodujacaStrzala_M":		return new EksplodujacaStrzala_M(rpgPlayer, rune, Arrays.asList(RuneLockerTypes.EKSPLODUJACA_STRZALA));
					case "SzostyZmysl":					return new SzostyZmysl(rpgPlayer, rune);
					case "SzostyZmysl_H":				return new SzostyZmysl_H(rpgPlayer, rune);
					case "SzostyZmysl_M":				return new SzostyZmysl_M(rpgPlayer, rune);
					case "Mord":						return new Mord(rpgPlayer, rune);
					case "KrwawaStrzala":				return new KrwawaStrzala(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_MELLOHI:
				switch(rune.getMythicType()) {
					case "OgnistaStrzala":				return new OgnistaStrzala(rpgPlayer, rune);
					case "MalaBurzaOgnista":			return new MalaBurzaOgnista(rpgPlayer, rune);
					case "Pirokineza":					return new Pirokineza(rpgPlayer, rune);
					case "KulaOgnia":					return new KulaOgnia(rpgPlayer, rune);
					case "OgnistaSfera":				return new OgnistaSfera(rpgPlayer, rune);
					case "WulkanicznyGejzer":			return new WulkanicznyGejzer(rpgPlayer, rune);
					case "BurzaOgnista":				return new BurzaOgnista(rpgPlayer, rune);
					case "DuzaKulaOgnia":				return new DuzaKulaOgnia(rpgPlayer, rune);
					case "OgnistyWybuch":				return new OgnistyWybuch(rpgPlayer, rune);
					case "AuraRozproszenia":			return new AuraRozproszenia(rpgPlayer, rune);
					case "MasowaPirokineza":			return new MasowaPirokineza(rpgPlayer, rune);
					case "MasowaPirokineza_H":			return new MasowaPirokineza(rpgPlayer, rune);
					case "MasowaPirokineza_M":			return new MasowaPirokineza(rpgPlayer, rune);
					case "OgnistaFala":					return new OgnistaFala(rpgPlayer, rune);
					case "Rozerwanie":					return new Rozerwanie(rpgPlayer, rune);
					case "Inkantacja":					return new Inkantacja(rpgPlayer, rune);
					case "DeszczOgnia":					return new DeszczOgnia(rpgPlayer, rune);
					case "DuzaBurzaOgnista":			return new DuzaBurzaOgnista(rpgPlayer, rune);
					case "Kataklizm":					return new Kataklizm(rpgPlayer, rune);
					case "Kataklizm_H":					return new Kataklizm(rpgPlayer, rune);
					case "Kataklizm_M":					return new Kataklizm(rpgPlayer, rune);
					case "Meteor":						return new Meteor(rpgPlayer, rune);
					case "PlomienLenga":				return new PlomienLenga(rpgPlayer, rune);
					case "PlomienLenga_H":				return new PlomienLenga(rpgPlayer, rune);
					case "PlomienLenga_M":				return new PlomienLenga(rpgPlayer, rune);
					case "WiecznyOgien":				return new WiecznyOgien(rpgPlayer, rune);
					case "WiecznyOgien_H":				return new WiecznyOgien(rpgPlayer, rune);
					case "WiecznyOgien_M":				return new WiecznyOgien(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_OTHERSIDE:
				switch(rune.getMythicType()) {
					case "SilaRownowagi":				return new SilaRownowagi(rpgPlayer, rune);
					case "SilaRownowagi_H":				return new SilaRownowagi_H(rpgPlayer, rune);
					case "SilaRownowagi_M":				return new SilaRownowagi_M(rpgPlayer, rune);
					case "Czystka":						return new Czystka(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_PIGSTEP:
				switch(rune.getMythicType()) {
					case "WiezyKrwi":					return new WiezyKrwi(rpgPlayer, rune);
					case "Gniew":						return new Gniew(rpgPlayer, rune);
					case "KrewPrzodkow":				return new KrewPrzodkow(rpgPlayer, rune);
					case "PlugawaKrew":					return new PlugawaKrew(rpgPlayer, rune);
					case "KlatwaKrwi":					return new KlatwaKrwi(rpgPlayer, rune);
					case "RytualKrwi":					return new RytualKrwi(rpgPlayer, rune);
					case "ZewSmierci":					return new ZewSmierci(rpgPlayer, rune);
					case "ZewSmierci_H":				return new ZewSmierci_H(rpgPlayer, rune);
					case "ZewSmierci_M":				return new ZewSmierci_M(rpgPlayer, rune);
					case "Drenaz":						return new Drenaz(rpgPlayer, rune);
					case "Transfuzja":					return new Transfuzja(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_PRECIPICE:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_RELIC:
				switch(rune.getMythicType()) {
					case "PrzyzwanieGoblina":			return new PrzyzwanieGoblina(rpgPlayer, rune);
					case "PrzyzwanieZombie":			return new PrzyzwanieZombie(rpgPlayer, rune);
					case "PrzyzwanieSzkieleta":			return new PrzyzwanieSzkieleta(rpgPlayer, rune);
					case "ObudzenieGolema":				return new ObudzenieGolema(rpgPlayer, rune);
					case "PrzyzwanieSzkieletaStrzelca":	return new PrzyzwanieSzkieleta(rpgPlayer, rune);
					case "PrzyzwanieSzkieletaWojownika":return new PrzyzwanieSzkieleta(rpgPlayer, rune);
					case "PrzyzwanieSzkieletaMaga":		return new PrzyzwanieSzkieleta(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_STAL:
				switch(rune.getMythicType()) {
					case "SferaCorristo":				return new SferaCorristo(rpgPlayer, rune);
					case "TeleportacjaKrotkodystansowa":return new TeleportacjaKrotkodystansowa(rpgPlayer, rune);
					case "SzeptNZotha":					return new SzeptNZotha(rpgPlayer, rune);
					case "SzeptNZotha_H":				return new SzeptNZotha_H(rpgPlayer, rune);
					case "SzeptNZotha_M":				return new SzeptNZotha_M(rpgPlayer, rune);
					case "SzalPustki":					return new SzalPustki(rpgPlayer, rune);
					case "SzalPustki_H":				return new SzalPustki(rpgPlayer, rune);
					case "SzalPustki_M":				return new SzalPustki(rpgPlayer, rune);
					case "Wir":							return new Wir(rpgPlayer, rune);
					case "Eksplozja":					return new Eksplozja(rpgPlayer, rune);
					case "NocWDzien":					return new NocWDzien(rpgPlayer, rune);
					case "TotemObronny":				return new TotemObronny(rpgPlayer, rune);
					case "FalaUderzeniowa":				return new FalaUderzeniowa(rpgPlayer, rune);
					case "Korzen":						return new Korzen(rpgPlayer, rune);
					case "Rezonans":					return new Rezonans(rpgPlayer, rune);
					case "TrzesienieZiemi":				return new TrzesienieZiemi(rpgPlayer, rune);
					case "Wtopienie":					return new Wtopienie(rpgPlayer, rune);
					case "Wtopienie_H":					return new Wtopienie_H(rpgPlayer, rune);
					case "Wtopienie_M":					return new Wtopienie_M(rpgPlayer, rune);
					case "PozeraczDusz":				return new PozeraczDusz(rpgPlayer, rune);
					case "PozeraczDusz_H":				return new PozeraczDusz_H(rpgPlayer, rune);
					case "PozeraczDusz_M":				return new PozeraczDusz_M(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_STRAD:
				switch(rune.getMythicType()) {
					case "RojOwadow":					return new RojOwadow(rpgPlayer, rune);
					case "UderzenieWiatru":				return new UderzenieWiatru(rpgPlayer, rune);
					case "TrujaceUkaszenie":			return new TrujaceUkaszenie(rpgPlayer, rune);
					case "UderzenieBurzy":				return new UderzenieBurzy(rpgPlayer, rune);
					case "ZewNatury":					return new ZewNatury(rpgPlayer, rune);
					case "ZrodloNatury":				return new ZrodloNatury(rpgPlayer, rune);
					case "ToksycznaChmura":				return new ToksycznaChmura(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_WAIT:
				switch(rune.getMythicType()) {
					case "KamiennyObserwator":			return new KamiennyObserwator(rpgPlayer, rune);
					case "TajemnyBlask":				return new TajemnyBlask(rpgPlayer, rune);
					case "TajemnyBlask_M":				return new TajemnyBlask_M(rpgPlayer, rune);
					case "TajemnyGrad":					return new TajemnyGrad(rpgPlayer, rune);
					case "TajemnyGrad_H":				return new TajemnyGrad_H(rpgPlayer, rune);
					case "TajemnyGrad_M":				return new TajemnyGrad_M(rpgPlayer, rune);
					case "MagicznaSfera":				return new MagicznaSfera(rpgPlayer, rune);
					case "MagicznaSfera_H":				return new MagicznaSfera(rpgPlayer, rune);
					case "MagicznaSfera_M":				return new MagicznaSfera(rpgPlayer, rune);
					case "Swiatlo":						return new Swiatlo(rpgPlayer, rune);
					case "WloczniaElysian":				return new WloczniaElysian(rpgPlayer, rune);
					case "SekretWielkanocy":			return new SekretWielkanocy(rpgPlayer, rune);
					case "LodowaAura":					return new LodowaAura(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_WARD:
				switch(rune.getMythicType()) {
					case "SzponBeliaraInt":				return new SzponBeliaraInt(rpgPlayer, rune);
					case "SzponBeliaraMana":			return new SzponBeliaraMana(rpgPlayer, rune);
					case "SzponBeliaraInt_I":			return new SzponBeliaraInt(rpgPlayer, rune);
					case "SzponBeliaraMana_I":			return new SzponBeliaraMana(rpgPlayer, rune);
					case "SzponBeliaraInt_II":			return new SzponBeliaraInt(rpgPlayer, rune);
					case "SzponBeliaraMana_II":			return new SzponBeliaraMana(rpgPlayer, rune);
					case "SzponBeliaraInt_III":			return new SzponBeliaraInt(rpgPlayer, rune);
					case "SzponBeliaraMana_III":		return new SzponBeliaraMana(rpgPlayer, rune);
					case "SzeptPrzedwiecznych":			return new SzeptPrzedwiecznych(rpgPlayer, rune);
					case "KrwawyPocisk":				return new KrwawyPocisk(rpgPlayer, rune);
					case "ZatrutaKrew":					return new ZatrutaKrew(rpgPlayer, rune);
					case "Rozprucie":					return new Rozprucie(rpgPlayer, rune);
					case "GniewPrzodkow":				return new GniewPrzodkow(rpgPlayer, rune);
					case "GniewPrzodkow_H":				return new GniewPrzodkow(rpgPlayer, rune);
					case "GniewPrzodkow_M":				return new GniewPrzodkow(rpgPlayer, rune);
					case "KrwawyDeszcz":				return new KrwawyDeszcz(rpgPlayer, rune);
					case "SpiralaKrwi":					return new SpiralaKrwi(rpgPlayer, rune);
					case "KrwawyBicz":					return new KrwawyBicz(rpgPlayer, rune);
					case "KrwawaFala":					return new KrwawaFala(rpgPlayer, rune);
					case "KrwawaFala_H":				return new KrwawaFala(rpgPlayer, rune);
					case "KrwawaFala_M":				return new KrwawaFala(rpgPlayer, rune);
					case "ZakazanyRytual":				return new ZakazanyRytual(rpgPlayer, rune);
					case "ZakazanyRytual_H":			return new ZakazanyRytual_H(rpgPlayer, rune);
					case "ZakazanyRytual_M":			return new ZakazanyRytual_M(rpgPlayer, rune);
					case "Furia":						return new Furia(rpgPlayer, rune);
					case "SzalBeliara":					return new SzalBeliara(rpgPlayer, rune);
				}
				break;
		default:
			return null;
		}
		return null;
	}
}
