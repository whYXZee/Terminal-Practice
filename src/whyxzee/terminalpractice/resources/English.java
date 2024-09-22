package whyxzee.terminalpractice.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class English {
    public static final HashMap<String, String> characters = new HashMap<String, String>() {
        {
            put("a", "1");
            put("b", "2");
            put("c", "3");
            put("d", "4");
            put("e", "5");
            put("f", "6");
            put("g", "7");
            put("h", "8");
            put("i", "9");
            put("j", "10");
            put("k", "11");
            put("l", "12");
            put("m", "13");
            put("n", "14");
            put("o", "15");
            put("p", "16");
            put("q", "17");
            put("r", "18");
            put("s", "19");
            put("t", "20");
            put("u", "21");
            put("v", "22");
            put("w", "23");
            put("x", "24");
            put("y", "25");
            put("z", "26");
        }
    };

    static HashMap<String, String> greekRoots = new HashMap<String, String>() {
        {
            // A
            put("acr-", "height, summit, tip");
            put("aer-", "atmosphere, air");
            put("aesthet-", "feeling,sensation");
            put("agr-", "field");
            put("amph-/amphi-", "both");
            put("a-/an-", "not,without");
            put("an-/ana-", "again,against,back up"); // again,
                                                      // against,
                                                      // back,,up
            put("andr-", "masculine,man");
            put("anem-", "wind");
            put("ant-/anti-", "against,opposed to,preventitive");
            put("anth-", "flower");
            put("anthrop-", "human");
            // put("ap-","away from,seperate,at the farthest point");
            put("apo-", "away from"); // away from, seperate,,at the farthest point
            put("arch-", "ruler"); // can also be "arche-","archi-"
            put("archae-/arche-", "ancient");
            put("arct-", "relating to the cold");
            put("arist-", "excellence");
            put("arthr-", "joint");
            put("astr-", "star");
            put("athl-", "prize");
            put("auto-", "self"); // can
                                  // also
                                  // be
                                  // "aut-"
                                  // as
                                  // well
                                  // as
                                  // directed
                                  // from
                                  // within
            put("axio-", "merit");
            // B
            put("bar-", "weight"); // can also be weight
            put("basi-", "at the bottom"); // double check later
            put("bathy-/batho-", "depth,deep");
            put("bibl-", "book");
            put("bio-", "life");
            put("blenn-", "slime"); // technically from
                                    // mucus but whatevs
            put("blast-", "cell with nucleus,germ,embryo,bud");
            put("bor-", "north");
            put("botan-", "plant");
            put("brachi-", "arm");
            put("brachy-", "short");
            put("brady-", "slow");
            put("branchi-", "gill");
            // put("briz-","slumber");
            put("brom-", "stench");
            put("bronch-", "windpipe");
            put("bront-", "thunder");
            // C
            put("cac-", "bad");
            put("call-/calli-", "beautiful"); // can also be "call-"
            put("calyp-", "cover");
            put("carcin-", "cancer");
            put("cardi-", "relating to the heart");
            put("carp-", "relating to fruit,relating to the wrist");
            put("cat-/cata-", "down");
            put("cathar-", "pure");
            put("cen-", "new,empty");
            put("centr-", "center");
            put("cephal-", "head");
            put("ceram-", "clay");
            // put("cerat-","horn");
            put("chir-", "of the hands");
            // put("chelon-","relating to a turtle");
            put("chlor-", "green");
            put("chore-", "relating to dance");
            // put("chord-","cord");
            put("chrom-", "color");
            put("chron-", "time");
            put("chrys-", "gold");
            put("cine-", "motion");
            put("cirr-", "orange");
            put("clad-", "branch");
            put("clast-", "broken");
            put("clav-", "key");
            put("clist-/cleist-", "closed");
            // put("cleithr-","bar,key");
            // put("chocl-","shell");
            // put("coel-","hollow");
            put("con-", "cone");
            put("copr-", "dung");
            put("corac-", "raven");
            put("cosm-", "universe");
            put("cotyl-", "cup");
            put("-cracy/-crat", "government,authority");
            put("crani-", "skull");
            // put("crep-","shoe");
            put("cris-/crit-", "judge");
            put("cross-", "fringe,tassel");
            put("crypt-", "hidden");
            // put("cten-","comb");
            put("cub-", "cube");
            put("cyan-", "blue");
            put("cycl-", "circular");
            put("cylind-", "roll");
            put("cyn-", "dog");
            put("cyst-", "capsule");
            put("cyt-", "cell");
            // D
            put("dactyl-", "finger,toe"); // also digit
            put("deca-/dec-/deka-/dek-", "ten");
            put("dem-", "people");
            put("dendr-", "resembling a tree");
            put("derm-", "skin");
            // put("deuter-","second");
            put("dexi-", "right"); // double check
            put("di-/dy-", "two");
            put("dia-", "apart,through"); // double check
            // put("dino-","fearfully great,terrible"); // double check
            put("dipl-", "double,twofold");
            put("dodec-", "twelve");
            put("dogmat-/dox-", "opinion,tenet");
            put("dynam-", "power");
            put("dys-", "badly,ill");
            // E
            put("ec-", "out");
            put("eco-", "house");
            put("ecto-/exo-", "outside");
            put("ego-", "self,i");
            put("ego-/eg-", "goat");
            // put("eme-","vomit");
            put("en-/em-", "in");
            put("endo-", "inside");
            // put("engy-","narrow");
            // put("ennae-","nine");
            put("eo-/eos-/eoso-", "east,dawn");
            put("ep-/epi-", "upon");
            put("epistem-", "knowledge,science");
            put("erg-", "work");
            put("erythr-", "red");
            put("eso-", "within");
            put("eth-/ethi-/etho-", "custom,habit");
            put("ethm-", "sieve");
            put("ethn-", "people, race, tribe,nation");
            put("etym-", "true");
            put("eu-", "well,good");
            put("eur-", "wide");
            // F
            put("fant-", "to show");
            // G
            put("galact-", "milk");
            put("gastr-", "stomach");
            put("geo-", "earth");
            put("gen-", "race,birth"); // also kind
            // put("glia-","glue");
            put("gram-", "writing");
            put("graph-", "draw,write");
            put("gymn-", "nude");
            put("gyn-", "woman");
            // H
            put("hem-/haem-", "blood");
            put("hal-", "salt");
            put("heli-", "sun");
            put("hemi-", "half");
            put("hen-", "one");
            put("hendec-", "eleven");
            put("hept-", "seven");
            put("herp-", "creep");
            put("heter-", "different,other");
            put("heur-", "find");
            put("hex-", "six");
            put("hipp-", "horse");
            put("hod-", "way");
            put("hol-", "whole");
            put("hom-", "same");
            put("home-", "similar");
            // put("homal-","even,flat");
            put("hor-", "boundary,hour");
            put("horm-", "that which excites");
            put("hyal-", "glass");
            put("hydr-", "water");
            put("hygr-", "wet");
            put("hyo-", "u-shaped");
            put("hyp-", "under");
            put("hyper-", "above,over");
            put("hypn-", "sleep");
            // I
            put("icthy-", "fish");
            put("icos-", "twenty");
            put("id-", "shape");
            put("ide-", "idea,thought");
            put("idi-", "personal");
            put("is-/iso-", "equal,the same");
            // K
            put("kil-/kilo-", "thousand");
            put("kine-", "motion");
            // put("klept-","steal");
            put("kudo-", "glory");
            // L
            // put("lamp-","shine");
            // put("lei-","smooth");
            // put("lekan-","dish");
            // put("lep-","scale,flake");
            put("leps-", "grasp,sieze");
            put("leuc-/leuco-/leuk-/leuko-", "white");
            put("lip-", "fat");
            put("lith-", "stone");
            put("log-", "thought,word,speech");
            put("lysis-", "dissolving");
            // M
            put("macro-", "long");
            put("mania-", "mental illness");
            put("meg-", "great,large");
            put("mei-", "less");
            put("melan-", "black,dark");
            put("mening-", "membrane");
            put("men-", "month");
            put("mer-", "part");
            put("mes-", "middle");
            put("metr-/meter-", "measure");
            put("meta-", "above,among,beyond");
            put("micr-", "small");
            put("mim-", "repeat");
            put("mis-", "hate");
            put("mit-", "thread");
            put("mne-", "memory");
            put("mon-", "single");
            put("morph-", "form,shape");
            // put("my-","mouse");
            put("myri-", "countless,ten thousand");
            // put("myrmec-","ant");
            put("myth-", "story");
            // put("myx-","slime");
            // put("myz-","suck");
            // N
            put("narc-", "numb");
            put("naut-", "ship");
            put("ne-", "new");
            put("necr-", "dead");
            put("nect-", "swimming");
            // put("nema-","hair");
            // put("nephr-","kidney");
            put("nes-", "island");
            put("neur-", "nerve");
            put("nom-", "arrangement,law");
            put("nomad-", "those who pasture herds");
            // put("noth-","spurious");
            put("noto-", "back,south");
            // O
            put("oct-", "eight");
            put("od-", "path,way");
            put("odont-", "tooth");
            put("oed-", "swollen");
            put("oen-", "wine");
            put("oesophag-", "gullet");
            put("ogdo-", "eighth");
            put("-oid", "like");
            put("olecran-", "skull of elbow");
            put("olig-", "few");
            put("-oma", "cancer");
            put("omm-/opt-/ophthalm-", "eye");
            put("omphal-", "navel");
            put("onom-", "name");
            put("ont-", "existing");
            put("onym-", "name");
            put("oo-", "egg");
            put("ophi-", "snake");
            put("opisth-", "behind");
            // put("opoter-","either");
            put("organ-", "organ,instrument,tool");
            put("ornith-", "bird");
            put("orth-", "straight");
            put("osteo-", "bone");
            put("ostrac-", "shell");
            put("ot-", "ear");
            put("oxy-", "sharp,pointed");
            // P
            put("pach-", "thick");
            put("paed-/ped-", "child");
            put("palae-/pale-", "ancient,old");
            put("palin-", "back");
            put("pan-/pam-", "all");
            put("par-/para-", "beside,near");
            put("parthen-", "virgin");
            put("path-", "feel,hurt");
            put("patr-", "father");
            put("pect-", "fixed");
            put("pent-", "five");
            // put("pentecost-","fifteen");
            put("pept-", "to digest");
            put("peran-", "across,beyond");
            put("peri-", "around");
            // put("persic-","peach");
            put("petr-", "rock");
            put("phae-", "dark");
            put("phag-", "eat");
            // put("phalang-","close formation of troops,finger bones");
            // put("phalar-","patch of white");
            put("pharmac-", "drug,medicine");
            // put("phaner","visible");
            put("pher-", "bear,carry");
            put("phil-/phile-", "love");
            put("phleg-", "heat");
            // put("phloe-","tree bark");
            put("phob-", "fear");
            put("phon-", "sound");
            put("phos-/phot-", "light");
            // put("phragm-","fence");
            // put("phren-","diaphragm,mind");
            put("phryn-", "toad,toad-like");
            put("phyl-", "tribe");
            put("phyll-", "leaf");
            put("phys-", "nature");
            // put("physalid-","bladder");
            put("phyt-", "plant");
            put("pin-", "drink");
            // put("pis-","pea");
            put("plac-", "plate,tablet");
            put("plagi-", "oblique");
            put("plas-", "mould");
            put("platy-", "flat,broad");
            // put("plec-","interwoven");
            put("plesi-", "near");
            put("pleth-", "full");
            put("pleur-", "side");
            // put("plinth-","brick");
            put("plut-", "wealth");
            put("pneu-", "air,lung");
            put("pod-", "foot");
            // put("pogon-","beard");
            put("poie-", "make");
            put("pol-", "pole");
            put("pole-/poli-", "city");
            // put("polem-","war");
            put("poli-", "grey");
            put("poly", "many");
            put("por-", "passage");
            put("porphyr-", "purple");
            put("potam-", "river");
            put("prasin-", "leed-green");
            put("presby-", "old");
            put("pro-", "before,in front of");
            put("proct-", "anus");
            put("pros-", "forward");
            put("prot-", "first");
            put("psuedo-", "false");
            put("psil-", "bare");
            put("psych-", "mind");
            put("psychr-", "cold");
            put("pter-", "wing,feather");
            // put("pto-","fall");
            put("ptych-", "fold,layer");
            put("pyg-", "rump");
            put("pyl-", "gate");
            put("pyr-", "heart,fire");
            // R
            put("raph-", "seam");
            put("rhabd-", "rod");
            put("rhach-/rach-", "spine");
            put("rhag-", "rent,tear");
            put("rhe-", "flow");
            put("rhig-", "chill");
            put("rhin-", "nose,snout");
            // put("rhiz-","root");
            // put("rhod-","rose");
            put("rhomb-", "spinning top");
            put("rhynch-", "snout");
            // S
            put("sacchar-", "sugar");
            put("sarc-", "flesh");
            put("saur-", "reptile,lizard");
            put("scalen-", "uneven");
            // put("scaph-","hollow,bowl,ship"); // double check
            put("scel-", "leg");
            put("schem-", "plan");
            put("schis-", "split");
            put("scler-", "hard");
            put("scoli-", "crooked");
            // put("", "loot at"); // double check w/ thingy
            put("scyph-", "cup");
            put("sei-", "shake");
            put("selen-", "moon");
            put("sema-", "sign");
            put("siph-/sipho-", "tube");
            put("sit-", "food,grain,wheat");
            put("solen-", "pipe,channel");
            put("soma-", "body");
            put("soph-", "wise");
            put("sperm-", "seed");
            // put("sphen-","wedge");
            put("sphinct-", "closing");
            // put("stalact-","");
            // put("stalagm-","");
            put("stea-", "fat,tallow");
            put("steg-", "covering");
            put("sten-", "narrow");
            put("stere-", "solid"); // double check
            // put("stern-","breast bone");
            put("stich-", "line,row");
            // put("stig-","");
            put("stoch-", "aim");
            put("stroph-", "turning");
            put("styl-", "column,pillar");
            // put("","with"); // syn(l/n/m), check
            put("syring-", "pipe");
            // T
            put("tach-", "swift");
            // put("taeni-","ribbon");
            // put("tars-","ankle");
            put("taur-", "bull");
            put("tax-", "arrangement,order");
            put("techn-", "art,skill");
            put("tele-", "far,end,complete");
            put("temn-", "cut");
            put("tetr-", "four");
            put("thalam-", "chamber,bed");
            put("thalass-", "sea");
            put("than-", "death");
            put("the-/thus-", "god");
            put("the-", "put");
            put("theori-", "speculation");
            put("ther-", "beast,animal");
            put("therm-", "heat,warm");
            put("thym-", "mood");
            // put("thyr-","door");
            put("thyr-", "large shield");
            put("tom-", "cut");
            put("ton-", "stretch");
            put("top-", "place");
            // put("tox-","arrow,bone");
            put("trachy-", "rough");
            put("trag-", "goat");
            // put("trapez-","four-sided,table");
            put("traum-", "wound");
            put("treiskaidek-", "thirteen");
            // put("trema-","hole");
            put("tri-", "three");
            put("trich-", "hair");
            put("trit-", "third");
            put("troch-", "wheel");
            put("trop-", "turning");
            put("troph-", "feed,grow");
            put("trympan-", "drum");
            put("typ-", "stamp,model");
            // U
            put("ulo-", "wooly");
            put("ur-/uro-", "tail,urine");
            // X
            put("xanth-", "yellow");
            put("xen-", "foreign");
            put("xer-", "dry");
            put("xiph-", "sword");
            put("xyl-", "wood");
            // Z
            put("zo-", "animal");
            put("zon-", "belt,girdle");
            put("zyg-", "yoke");
            put("zym-", "ferment");
        }
    };

    static HashMap<String, String> latinRoots = new HashMap<String, String>() {
        {
            // A
            put("a-/ab-/abs-", "away from");
            put("ac-/acu-", "sharp"); // also pointed (excluding "acu-")
            put("acerb-", "sharp,bitter,sour");
            put("acid-", "sour,acid");
            put("acr-", "bitter,pungent,sharp,sour");
            put("a-/ad-/ac-/af-/ag-/al-/ap-/ar-/as-/at", "moving toward,in addition");
            put("adip-", "fat");
            put("agri-/-egri-", "field");
            // put("alb-","dull white");
            put("am-/amat-/amor-", "love,loved");
            put("ambi-", "both");
            put("amic-/imic-", "friend");
            put("ampl-", "abundant,ample,bountiful");
            put("anim-", "breath");
            put("ann-/-enn-", "year,yearly");
            put("ante-/anti-", "before");
            put("aqu-", "water");
            put("ar-", "plow,till,be dry");
            put("argent-", "silver");
            put("audi-", "sound,hearing,listening");
            put("aug-/auct-", "grow,increase");
            put("aur-", "relating to gold,gold colored");
            put("auri-", "relating to the ear");
            put("avi-", "bird");
            put("axi-", "axis");
            // B
            // put("bac-","rod-shaped");
            // put("be-/beat-","bless");
            put("bell-", "war");
            put("ben-", "good,well");
            put("bi-", "two");
            put("bib-", "drink");
            put("bon-", "good");
            put("bor-", "north");
            put("bov-", "cow,ox");
            put("brev-/brevi-", "brief,short");
            put("bucc-", "cheek,mouth,cavity");
            // put("bulb-","bulbous");
            put("bull-", "bubble,flask");
            put("burs-", "pouch,purse");
            // C
            put("cad-/-cid-/cas-", "fall");
            put("caed-/-cid-/caes-/-cis-", "cut");
            put("calc-", "stone");
            put("calor-", "heat");
            put("camer-", "valut");
            put("camp-", "field");
            put("can-", "dog");
            put("can-/-cin-/cant", "sing");
            put("cand-", "glowing,iridescent");
            put("cap-/-cip-/capt-/-cept-", "hold,take");
            put("capit-/-cipit-", "head");
            put("capr-", "goat");
            put("caps-", "box,case");
            put("carbo-", "coal");
            put("carcer-", "jail");
            put("carcin-", "cancer");
            put("cardin-", "hinge");
            put("carn-", "flesh");
            put("cast-", "pure");
            put("caten-", "chain");
            put("caud-", "tail");
            put("caus-/-cus-", "cause,motive");
            put("cav-", "hollow");
            put("ced-/cess-", "go");
            put("celer-", "quick");
            // put("cens-","");
            put("cent-", "hundred");
            put("centen-", "hundred each");
            put("centesim-", "hundredth");
            put("cern-", "sift");
            // put("cervic-","relating to the neck,cervix");
            put("ceter-", "other");
            put("chord-", "cord");
            put("cili-", "eyelash");
            put("ciner-", "ash");
            put("cing-/cinct-", "gird");
            put("circ-", "circle");
            put("circum-", "around");
            // put("cirr-","curl,tentacle");
            put("civ-", "citizen");
            put("clar-", "clear");
            put("claud-/-clud-/claus-/-clus-", "close");
            put("clement-", "mild");
            // put("clin-","bed,lean,recline");
            put("cogn-", "know");
            put("col-", "strain");
            put("coll-", "hill,neck");
            put("color-", "color");
            put("con-/co-/col-/com-/cor-", "with,together");
            put("condi-", "season" /*
                                    * as
                                    * in
                                    * spices
                                    */);
            put("contra-", "against");
            put("cord-", "heart");
            put("corn-", "horn");
            put("coron-", "crown");
            put("corpor-", "body");
            put("cortic-", "bark");
            put("cost-", "rib");
            // put("crass-","thick");
            put("cre-", "make");
            put("cred-", "believe,trust");
            put("cribr-", "sieve");
            put("crisp-", "curled");
            // put("crist-","crest");
            put("cruc-", "cross");
            // put("crur-","leg,shank");
            put("cub-", "lie");
            put("culin-", "kitchen");
            put("culp-", "blame,fault");
            put("cune-", "wedge");
            put("curr-/curs-", "run");
            put("curv-", "bent");
            // put("cuspid-","lance,point");
            put("cut-", "skin");
            // D
            put("damn-/-demn-", "to inflict loss upon");
            put("de-", "removing"); // also from, away, and down
            put("deb-", "owe");
            put("decim-", "tenth part");
            // put("den-","ten each");
            put("dens-", "thick");
            put("dent-", "tooth");
            put("dexter-", "right"); // double check
            put("dict-", "say,speak");
            put("digit-", "finger");
            put("doc-/doct-", "teach"); // double check
            put("dom-", "house"); // double check
            put("don-", "give");
            put("dorm-", "sleep");
            put("dors-", "back" /* as in spine */);
            put("du-", "two");
            put("dub-", "doubtful");
            put("duc-/duct-", "lead");
            put("dulc-", "sweet");
            put("dur-", "hard");
            // E
            put("ed-/es-", "eat");
            put("ego-", "self,i");
            put("em-/empt-", "buy"); // double check
            put("emul-", "striving to equal,rivaling");
            // put("ens-","sword");
            put("equ-/-iqu-", "even,level");
            put("equ-", "horse");
            put("err-", "stray");
            put("ex-/e-/ef-", "from,out");
            put("exter-/extra-", "outer");
            put("extrem-", "outermost,utmost");
            // F
            put("f-/fat-", "say,speak"); // double check
            put("fab-", "bean"); // double check
            put("fac-/-fic-/fact-/-fect-", "make");
            put("falc-", "sickle");
            put("fall-/-fell-/fals-", "deceive");
            put("faallac-", "false");
            put("famili-", "close attendant");
            put("fasc-", "bundle");
            put("fatu-", "foolish,useless");
            put("feder-", "treaty,agreement,contract,league,pact");
            put("fel-", "cat");
            put("fell-", "suck");
            put("femin-", "female,woman");
            put("femor-", "thigh");
            put("fend-/fens-", "strike");
            // put("fenestr-","window");
            put("fer-", "carry");
            put("feroc-", "fierce");
            put("ferr-", "iron");
            put("fet-", "stink");
            // put("fic-","fig");
            put("fid-/fis-", "faith,trust");
            put("fil-", "thread");
            put("fili-", "son"); // double check
            put("fin-", "end");
            put("find-/fiss-", "split");
            put("firm-", "fix,settle");
            // put("fistul-","hollow,tube");
            put("fl-", "blow");
            // put("flacc-","flabby");
            put("flav-", "yellow");
            put("flect-/flex-", "bend");
            put("flig-/flict-", "strike");
            put("flor-", "flower");
            put("foc-", "hearth"); // double
                                   // check
                                   // also
                                   // what's
                                   // hearth
                                   // :P
            put("fod-/foss-", "dig");
            // put("foen-","hay");
            put("foli-", "leaf");
            put("font-", "spring"); // double check
            put("for-", "bore,drill"); // double check
            put("form-", "shape");
            // put("fornic-","vault");
            put("fort-", "strong");
            // put("fove-","shallow round depression");
            put("frang-/-fring-/fract-/frag-", "break");
            put("frater-/fratr-", "brother");
            put("fric-/frict-", "rub");
            put("frig-", "cold");
            put("front-", "forehead");
            put("fruct-/frug-", "fruit");
            put("fug-/fugit-", "flee");
            put("fum-", "smoke");
            // put("fund-","bottom"); // double check
            put("fund-/fus-", "pour");
            put("fung-/funct-", "do");
            put("fur-/furt-", "steal");
            // put("furc-","fork");
            put("fusc-", "dark");
            // G
            put("gel-", "icy gold");
            put("ger-/gest-", "bear,carry");
            // put("germin-","sprout");
            // put("glabr-","hairless");
            put("glaci-", "ice");
            put("gladi-", "sword");
            put("glob-", "sphere");
            put("glori-", "glory");
            // put("glutin-","glue");
            put("grad-/-gred-/gress-", "walk, step,,go");
            put("gran-", "grain");
            put("grand-", "grand");
            put("grat-", "thank,please");
            put("grav-", "heavy");
            put("greg-", "flock");
            // put("guvern-","govern,pilot");
            put("gust-", "taste");
            put("gutt-", "drop");
            // put("guttur-","throat");
            // H
            put("hab-/-hib-/habit-/-hibit-", "have");
            put("hal-/-hel-", "breathe");
            put("haur-/haust-", "draw");
            put("her-/hes-", "cling");
            put("herb-", "grass");
            put("hered-", "heir");
            put("hivern-", "wintry");
            put("hiem-", "winter");
            put("hirsut-", "hairy");
            put("hispid-", "bristly");
            // put("histri-","actor");
            // put("homin-","human");
            put("honor-", "esteem");
            put("hort-", "garden");
            put("hospit-", "host");
            put("host-", "enemy");
            put("hum-", "ground");
            // I
            put("ign-", "fire");
            put("in-/im-", "in,on");
            put("in-/il-/im-/ir-", "negation"); // also not
            put("infra-", "below,under");
            put("insul-", "island");
            put("inter-", "among,between");
            put("intra-", "within");
            put("irasc-/irat-", "be angry");
            put("iter-", "again");
            put("itiner-", "route,way");
            // J
            put("jac-", "lie" /* as in "be" and not telling lies */);
            put("jac-/-ject-", "cast,throw");
            put("janu-", "door");
            put("joc-", "joke");
            put("jug-", "yoke"); // whats
                                 // yoke
            put("jung-/junct-", "join");
            put("junior-", "younger");
            put("jus-/jur-/judic-", "law,justice");
            put("juv-/jut-", "help");
            put("juven-", "young,youth");
            // put("juxta-","beside,near");
            // L
            put("lab-/laps-", "slide,slip");
            // put("labi-","lip");
            put("labor-", "toil");
            // put("lacer-","tear");
            put("lacrim-", "cry,tears");
            put("lact-", "milk");
            // put("lamin-","layer,slice");
            put("lapid-", "stone");
            put("larg-", "large");
            put("larv-", "ghost,mask");
            put("lat-", "broad,wide");
            put("laud-/laus-", "praise");
            put("lav-", "wash");
            put("lax-", "not tense");
            put("led-/les-", "hurt");
            put("leg-", "law,send");
            put("leni-", "gentle");
            put("leo-", "lion"); // really
                                 // should
                                 // be
                                 // "leon-"
                                 // but
                                 // womp
                                 // womp
            put("lev-", "lift,light");
            put("liber-", "free");
            put("libr-", "book");
            put("lig-", "blind");
            // put("limac-","slug");
            put("lin-", "line");
            put("lingu-", "language,tongue"); // also tongue
            put("linqu-/lict-", "leave");
            put("liter-", "letter");
            put("loc-", "place");
            put("long-", "long");
            put("loqu-/locut-", "speak");
            put("luc-", "bright,light");
            put("lud-/lus-", "play");
            put("lumin-", "light");
            put("lun-", "moon");
            // M
            put("magn-", "great,large");
            put("maj-", "greater");
            put("mal-", "bad,wretched");
            put("mamm-", "breast");
            put("man-", "flow");
            put("man-", "stay");
            put("mand-/manu-", "hand");
            put("mar-", "sea");
            put("mater-/matr-", "mother");
            put("maxim-", "greatest");
            put("medi-/-midi-", "middle");
            put("melior-", "better");
            put("mell-", "honey");
            put("memor-", "remember");
            put("menstru-", "monthly");
            put("ment-", "mind");
            put("merc-", "reward,wages,hire");
            put("merg-/mers-", "dip,plunge");
            put("mic-", "grain");
            put("migr-", "wander");
            put("milit-", "soldier");
            put("mill-", "thousand");
            put("millen-", "thousand each");
            put("min-", "less,smaller,jut");
            put("mir-", "wonder,amazement");
            put("misce-/mixt-", "mix");
            put("mitt-/miss-", "send");
            put("mol-", "grind");
            put("moll-", "soft");
            put("monil-", "string of beads");
            put("mont-", "mountain");
            put("mord-", "bite");
            put("mort-", "death");
            put("mov-/mot-", "move,motion");
            // put("mulg-/muls-","milk");
            put("mult-", "many,much");
            put("mur-", "wall");
            // put("mus-","thief");
            // put("musc-","fly");
            put("mut-", "change");
            // N
            put("nar-", "nostril");
            put("narr-", "tell");
            put("nas-", "nose");
            put("nasc-/nat-", "born");
            put("nav-", "ship");
            put("nect-/nex-", "join,tie");
            put("neg-", "say no");
            put("nemor-", "grove,woods");
            // put("nict-","wink");
            put("nigr-", "black");
            put("nihil-", "nothing");
            put("noct-", "night");
            put("nod-", "knot");
            put("nomin-", "name");
            put("non-", "not,ninth");
            put("nonagen-", "ninety each");
            put("nonagesim-", "ninetieth");
            put("not-", "letter,note,paper");
            put("nov-", "nine,new");
            put("novendec-", "nineteen");
            put("nox-/noc-", "harmful");
            put("nu-", "nod");
            put("nub-", "to marry,to wed");
            put("nuc", "nut");
            put("nuch-", "back of the neck");
            put("nud-", "naked");
            put("null-", "none");
            put("numer-", "number");
            put("nunci-", "announce");
            // put("nupti-","");
            put("nutri-", "nourish");
            // O
            put("ob-/o-/oc-/of-/og-/op-/os-", "against");
            put("oct-", "eight");
            put("octav-", "eigth");
            put("octogen-", "eighty each");
            put("octogesim-", "eighteith");
            put("octon-", "eight each");
            put("ocul-", "eye");
            put("od-", "hate");
            put("odor-", "fragrant");
            // put("ole-","oil");
            put("olecran-", "skull of elbow");
            put("oliv-", "olive");
            put("omas-", "paunch");
            put("oment-", "fat skin");
            put("omin-", "creepy");
            put("omni-", "all");
            // put("omo-","shoulder");
            put("oner-", "burden");
            put("opac-", "shady");
            put("oper-", "work");
            // put("opercul-","little cover");
            put("opt-", "choose");
            put("optim-", "best");
            put("or-", "mouth");
            put("orb-", "circle");
            put("ordin-", "order");
            put("ori-/ort-", "eastern");
            put("orn-", "decorate");
            put("oscill-", "swing");
            put("oss-", "bone");
            // put("osti-","entrance");
            put("ov-", "egg");
            put("ovi-", "sheep");
            // P
            put("pac-", "peace");
            put("pagin-", "page");
            put("pal-", "stake");
            put("pall-", "be pale");
            // put("palli-","mantle");
            // put("palm-","palm");
            // put("palustr-","in marshes");
            put("pand-/pans-", "spread");
            put("pariet-", "wall");
            put("part-", "part");
            // put("parv-","little");
            put("pasc-/past-", "feed");
            put("pass-", "pace,step");
            // put("passer-","sparrow");
            put("pat-", "be open");
            put("pati-/pass-", "suffer,feel,endure,permit");
            put("patr-", "father");
            put("pauc-", "few");
            // put("pav-","");
            put("pecc-", "sin");
            put("pector-", "chest");
            put("pecun-", "money");
            put("ped-", "foot");
            put("pejor-", "worse");
            put("pell-/puls-", "drive");
            put("pen-", "almost");
            put("pend-/pens-", "hang");
            put("penn-/pinn-", "feather");
            put("per-", "thoroughly,through");
            put("pessim-", "worst");
            put("pet-", "strive forward");
            put("pic-", "pitch");
            put("pil-", "hair");
            put("pin-", "drink,pine");
            put("ping-/pict-", "paint");
            put("pingu-", "fat");
            put("pir-", "pear");
            put("pisc-", "fish");
            put("plac-", "calm");
            put("plac-/-plic-", "please");
            put("plan-", "flat");
            // put("plang-/planct-","");
            put("plud-/-plod-/plaus-/-plos-", "approve,clap");
            put("ple-/plet-", "fill");
            put("pleb-", "people");
            put("plect-/plex-", "plait");
            put("plen-", "full");
            // put("plor-","");
            put("plu-", "rain");
            put("plum-", "feather");
            put("plumb-", "lead");
            put("plur-/plus-", "more");
            put("plurim-", "most");
            put("pluvi-", "rain");
            put("pollic-", "thumb");
            // put("pollin-","");
            put("pon-/posit-", "put");
            put("ponder-", "weight");
            put("pont-", "bridge");
            put("popul-", "people");
            put("porc-", "pig");
            put("port-", "gate,carry");
            put("post", "after,behind");
            put("pot-", "drink");
            put("prat-", "meadow");
            put("prav-", "crooked");
            put("pre-", "before");
            // put("prec-","pray");
            // put("pred-","");
            put("prehend-/prend-/prehens-", "grasp");
            put("prem-/-prim-/press-", "press");
            put("preter-", "past");
            put("preti-", "price");
            put("prim-", "first");
            put("prior-", "former");
            put("priv-/privi-", "separate");
            put("pro-", "for,forward");
            put("prob-", "try");
            put("propri-", "one's own,ownership");
            put("proxim-", "nearest");
            put("prun-", "plum");
            put("pub-", "sexually mature");
            // put("public-","");
            put("pude-", "");
            put("pugn-", "fight");
            // put("pulchr-","beautiful");
            put("pulmon-", "lung");
            put("pulver-", "dust");
            put("pung-/punct-", "prick");
            put("puni-", "punish");
            put("pup-", "doll");
            put("purg-", "cleanse");
            put("purpur-", "purple");
            put("put-", "prune,reckon");
            // Q
            put("quadr-", "four");
            put("quadragen-", "forty each");
            put("quadragesim-", "fortieth");
            put("quasi-", "as if");
            put("qatern-", "four each");
            put("quati-/quass-", "shake");
            put("quer-/-quir-/quesit-/-quisit-", "search,seek");
            put("qui-", "rest");
            put("quin-", "five each");
            put("quindecim-", "fifteenth");
            put("quinden-", "fifteen each");
            put("quinque-", "five");
            put("quint-", "fifth");
            put("quot-", "how many,how great");
            // R
            put("rad-/ras-", "scrape,shave");
            put("radi-", "beam,spoke");
            put("radic-", "root");
            put("ram-", "branch");
            put("ran-", "frog");
            put("ranc-", "rancidness,grudge,bitterness");
            // put("rap-","turnip");
            // put("rar-","");
            put("rauc-", "harsh,hourse");
            put("re-/red-", "again,back");
            put("reg-/-rig-/rect-", "straight");
            put("rem-", "oar");
            put("ren-", "kidney");
            put("rep-/rept-", "crawl,creep");
            put("rid-/ris-", "laugh");
            put("robor-", "oak,strength");
            // put("rod-/ros-","gnaw");
            put("rog-", "ask");
            put("rostr-", "beak,prow");
            put("rot-", "wheel");
            put("ruber-/rubr-", "red");
            // put("rug-","wrinkle");
            // put("rumin-","throat");
            put("rump-/rupt-", "break");
            put("rur-", "country");
            // S
            put("sacr-/secr-", "sacred");
            put("sagac-", "wise");
            // put("sagitt-","arrow");
            put("sal-", "salt");
            put("sali-/-sili-/salt-", "jump");
            put("salic-", "willow");
            put("salv-", "save");
            put("san-", "healthy");
            put("sanc-", "holy");
            put("sanguin-", "blood");
            put("sapi-/-sipi-", "taste,wise");
            put("sapon-", "soap");
            put("sax-", "rock");
            put("scab-", "scratch");
            put("scal-", "ladder,stairs");
            put("scand-/-scend-/scans-/-scens-", "climb");
            put("sci-", "know");
            put("scind-/sciss-", "split");
            put("scrib-/script-", "write");
            put("sculp-", "carve");
            put("scut-", "shield");
            put("se-/sed-", "apart");
            put("seb-", "tallow");
            put("sec-/sect-/seg-", "cut");
            put("sed-", "settle,calm");
            put("sed-/-sid-/sess-", "sit");
            put("sedec-", "sixteen");
            // put("seget-","in the cornfields"); // ??? what
            put("sell-", "saddle,seat");
            put("semi-", "half");
            put("semin-", "seed");
            put("sen-", "old man,six each");
            put("senti-/sens-", "feel");
            put("sept-", "fence,partition,enclosure,seven");
            put("septen-", "seven each");
            put("septim-", "seventh");
            put("septuagen-", "seventy each");
            put("septuagesim-", "seventieth");
            put("sequ-/secut-", "follow");
            put("ser-", "body fluid,late");
            put("serp-", "crawl,creep");
            put("serv-", "save,protect,serve");
            put("sesqui-", "one and a half");
            put("set-", "bristle,hair");
            put("sever-", "stern,strict,serious");
            put("sex-/se-", "six");
            put("sexagen-", "sixty each");
            put("sexagesim-", "sixteith");
            put("sext-", "sixth");
            put("sibil-", "hiss");
            // put("sicc-","dry");
            // put("sider-","star");
            // put("sign-","sign");
            put("sil-", "quiet,still");
            put("silv-/silvi-", "forest");
            put("simi-", "ape,monkey");
            put("simil-", "likeness,trust,group");
            put("simul-", "imitating,feigning");
            put("singul-", "one each");
            put("sinistr-", "left");
            put("sinu-", "to draw a line");
            put("sinus-", "hollow,bay");
            // put("sist-","cause to st and");
            put("soci-", "group");
            put("sol-", "sun,comfort,soothe,alone,only");
            put("solv-/solut-", "loosen,set free");
            put("somn-", "sleep");
            put("somni-", "dream");
            put("son-", "sound");
            put("sorb-/sorpt-", "suck");
            put("sord-", "dirt");
            put("soror-", "sister");
            put("spati-", "space");
            put("spec-/-spic-/spect-", "look");
            put("specul-", "observe");
            // put("sper-","hope");
            put("spic-", "spike");
            put("spin-", "thorn");
            put("spir-", "breathe");
            put("spond-/spons-", "a surety,guarantee,give assurance,promise solemnly");
            put("spu-/sput-", "spew,spit");
            put("squal-", "scaly,dirty,filthy");
            // put("squam-","scale");
            // put("squarros-","spreading at tips");
            put("st-", "stand");
            put("stagn-", "pool of standing water");
            put("stann-", "tin");
            put("statu-/-stitu-", "stand");
            put("stell-", "star");
            put("stern-/strat-", "spread,strew");
            put("still-", "drip");
            put("stimul-", "goad,rouse,excite");
            put("stingu-/stinct-", "apart");
            put("strig-", "compress");
            // put("strigos-","having stiff bristles");
            put("string-/strict-", "upright,stiff");
            put("stru-/struct-", "to make up, build");
            put("stud-", "dedication");
            put("stup-", "wonder");
            put("su-/sut-", "sew");
            put("sui-", "self");
            put("suad-/suas-", "urge");
            put("suav-", "sweet");
            put("sub-/su-/suf-/sug-/sus", "below");
            put("subter-", "under");
            put("sucr-", "sugar");
            put("sud-", "sweat");
            put("sulc-", "furrow");
            put("sum-/sumpt-", "take");
            put("super-/supra-", "above,over");
            put("supin-", "lying back");
            put("surd-", "deaf");
            put("surg-", "rise");
            // T
            put("tac-/-tic-", "be silent");
            put("tal-", "ankle");
            put("tang-/-ting-/tact-/tag-", "touch");
            put("tapet-", "carpet");
            put("tard-", "slow");
            put("taur-", "bull");
            put("teg-/tect-", "cover");
            put("tempor-", "time");
            put("ten-/-tin-/tent-", "hold");
            put("tend-/tens-", "stretch,strain");
            put("tenu-", "slender,thin");
            put("tep-", "be warm");
            put("ter-/trit-", "rub");
            put("teret-", "rounded");
            put("terg-/ters-", "wipe");
            put("termin-", "boundry,limit,end");
            put("tern-", "three each");
            put("terr-", "dry land");
            put("terti-", "third");
            put("test-", "witness");
            put("tex-/text-", "weave");
            put("tim-", "be afraid");
            put("ting-/tinct-", "moisten");
            put("torn-", "cut");
            put("torpe-", "numb");
            put("torqu-/tort-", "twist");
            put("tot-", "all,whole");
            put("trab-", "beam");
            put("trah-/tract-", "draw,pull");
            put("trans-/tra-/tran-", "across");
            put("trecent-", "three hundred");
            put("tredec-", "thirteen");
            put("trem-", "tremble");
            put("tri-", "three");
            put("tricen-", "thirty each");
            // put("tricesim-/trigesim-","thirtieth");
            put("trin-", "three each");
            put("trud-/trus-", "thrust");
            put("tuss-", "cough");
            // U
            put("uber-", "fruitful");
            put("uligin-", "in marshes");
            put("ultim-", "farthest");
            put("ultra-", "beyond");
            // put("umbilic-","navel");
            put("umbr-", "shade,shadow");
            put("un-", "one");
            put("unc-", "hooked");
            put("unci-", "ounce,twelfth");
            put("und-", "wave");
            put("undecim-", "eleventh");
            put("unden-", "eleven each");
            put("ungui-", "claw,nail");
            put("ungul-", "claw,hoof");
            put("urb-", "city");
            put("urg-", "work");
            put("urs-", "bear");
            put("ut-/us-", "use");
            put("uv-", "grape");
            put("uxor-", "wife");
            // V
            put("vac-", "empty");
            put("vad-/vas-", "go");
            put("vag-", "wander");
            put("van-", "empty,vain,idle");
            put("vap-", "lack,lack of");
            put("veh-/vect-", "carry");
            put("vel-", "viel");
            put("vell-/vuls-", "pull");
            put("veloc-", "quick");
            put("ven-", "vein,hunt");
            put("ven-/vent-", "come");
            put("vend-", "sell");
            put("vener-", "respectful");
            put("vent-", "wind");
            put("ventr-", "belly");
            put("ver-", "true");
            put("verb-", "word");
            put("verber-", "whip");
            put("verm-", "worm");
            put("vern-", "spring");
            put("vert-/vers-", "turn");
            put("vesic-", "bladder");
            put("vesper-", "evening,western");
            put("vest-", "clothe,garment");
            put("vestig-", "track");
            put("vet-", "forbid");
            put("veter-", "old");
            put("vi-", "way");
            put("vic-", "change");
            put("vicen-/vigen-", "twenty");
            put("vicesim-/vigesim-", "twentieth");
            put("vid-/vis-", "see");
            put("vil-", "cheap");
            put("vill-", "shaggy hair,velvet");
            put("vin-", "wine");
            put("vinc-/vict-", "conquer");
            put("vir-", "man,green");
            put("visc-", "thick");
            put("viscer-", "organs of the body cavity");
            put("vit-", "life");
            put("vitell-", "yolk");
            put("viti-", "fault");
            put("vitr-", "glass");
            put("viv-", "live");
            put("voc-", "voice");
            put("vol-", "fly,wish");
            put("volv-/volut-", "roll");
            put("vom-", "discharge");
            put("vor-/vorac-", "swallow");
            put("vov-/vot-", "vow");
            put("vulg-", "common,crowd");
            put("vulner-", "wound");
            put("vulp-", "fox");
        }
    };

    public static HashMap<String, String> allRoots = new HashMap<String, String>() {
        {
            putAll(greekRoots);
            putAll(latinRoots);
        }
    };

    public static ArrayList<String> words = new ArrayList<String>(
            Arrays.asList("Balloon", "Committee", "Success", "Doodle", "Keen",
                    "Coffee", "Puppy", "Bubble", "Sassafras", "Letter",
                    "Eerie", "Occur", "Bitter", "Add", "Eclipse",
                    "Mississippi", "Paddle", "Cabbage", "Silly", "Chatter",
                    "Effort", "Affect", "Poppy", "Happen", "Baggage",
                    "Happy", "Fuzzy", "Sneeze", "Coo", "Raccoon",
                    "Cabbie", "Boo", "Creep", "Hoodoo", "Muffin",
                    "Giggling", "Riffraff", "Tattletale", "Zigzag", "Rattle",
                    "Kiddie", "Dillydally", "Buzzer", "Dazzle", "Ripper",
                    "Snazzy", "Tippy", "Nanny", "Fluffy", "Sassy",
                    "Twinkle", "Meme", "Bookkeeper", "Dodo", "Giggle",
                    "Mimic", "Consensus", "Tattle", "Jolly", "Teepee",
                    "Spoon", "Boomerang", "Afford", "Noodle", "Doodad",
                    "Coddle", "Eddied", "Fiddler", "Kettle", "Wobble",
                    "Daffodil", "Nanny", "Gooey", "Popcorn", "Wobbly",
                    "Noodle", "Fiddle", "Cuddle", "Bobby", "Bitter",
                    "Cinnamon", "Dabble", "Riffle", "Addict", "Nuzzle",
                    "Scribble", "Cuddle", "Buzz", "Waddle", "Fuzz",
                    "Razzmatazz", "Muff", "Fluff", "Bobby", "Seesaw"));
}
