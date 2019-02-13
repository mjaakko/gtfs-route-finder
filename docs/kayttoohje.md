1. Lataa jokin GTFS-syöte ja pura se haluamaasi kansioon
   * [Täältä](https://api.digitransit.fi/routing-data/v2/finland/) löytyy joidenkin suomalaisten kaupunkien GTFS-syötteitä. Ainakin `tampere.zip` ja 
`HSL.zip` tutkitusti toimivat tällä ohjelmalla.
2. Aja ohjelma komennolla `gradle run`
3. Kun ohjelma sanoo: `Location for GTFS feed:`, syötä aiemmin lataamasi GTFS-syötteen sijainti
4. Ohjelma kysyy haluamasi toiminnon

## 1 - Algoritmien vertailu

1. Ohjelma kysyy kuinka monta kertaa reittihaku toistetaan
   * *Huom!* reittihaku on hidas ja suuremmilla syötteillä (esim. 
HSL-alueen) reitinhaku voi kestää muutamia minuutteja per toisto
2. Kun ohjelma on laskenut halutun määrän reittejä, se tulostaa kuinka 
paljon reitinhakuun käytettiin aikaa A*-algoritmilla ja Dijkstran 
algoritmilla

## 2 - Reittihaku 

1. Kun ohjelma sanoo: `Search for origin stop:`, kirjoita hakusana 
lähtöpysäkille, esim:
   * Jos hakusanalla löytyy vain yksi pysäkki, ohjelma asettaa sen suoraan lähtöpysäkiksi
   * Jos hakusanalla löytyy useampi pysäkki, ohjelma tulostaa niistä listan, esim: ```
1 - Pyynikintie
2 - Pyynikintie
3 - Pyynikinharju
4 - Pyynikinharju
5 - Pyynikintori
6 - Pyynikintori B
7 - Pyynikintori
8 - Pyynikintori A
9 - Pyynikin koulu``` Valitse pysäkki numerolla
2. Kun ohjelma sanoo: `Search for destination stop:`, valitse 
päätepysäkki samalla tavalla kuin lähtöpysäkki
3. Ohjelma laskee reitin ja tulostaa sen
```
Route found in  26904ms
null (WALK) Pyynikintie -> Pyynikintie
25 (BUS) Pyynikintie -> Rosendahl
null (WALK) Rosendahl -> Rosendahl
25 (BUS) Rosendahl -> Piispantalo
null (WALK) Piispantalo -> Mäntypuisto
null (WALK) Mäntypuisto -> Mäntypuisto
null (WALK) Mäntypuisto -> Mariankatu
null (WALK) Mariankatu -> Pyynikintori A
null (WALK) Pyynikintori A -> Pyynikintori
21 (BUS) Pyynikintori -> Aleksanterin kirkko
21 (BUS) Aleksanterin kirkko -> Keskustori L
21 (BUS) Keskustori L -> Koskipuisto F
21 (BUS) Koskipuisto F -> Linja-autoasema
4 (BUS) Linja-autoasema -> Tampereen valtatie
4 (BUS) Tampereen valtatie -> Viinikan liittymä
4 (BUS) Viinikan liittymä -> Iidesranta
4 (BUS) Iidesranta -> Kuikankatu
4 (BUS) Kuikankatu -> Tiirantaival
4 (BUS) Tiirantaival -> Järvensivun koulu
4 (BUS) Järvensivun koulu -> Vuohensilta
4 (BUS) Vuohensilta -> Vuohenoja
4 (BUS) Vuohenoja -> Laulunmaa
4 (BUS) Laulunmaa -> Lukonmäki pohj.
4 (BUS) Lukonmäki pohj. -> Lukonmäki et.
4 (BUS) Lukonmäki et. -> Tieteenkatu
null (WALK) Tieteenkatu -> Shell Hervanta
```

**Huom!** Tällä hetkellä ohjelma ei juurikaan käsittele virhetilanteita 
ja esimerkiksi virheellinen syöte johtaa ohjelman kaatumiseen.
