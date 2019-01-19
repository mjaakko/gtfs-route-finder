## Aihe

Työn aiheena on reitinhakualgoritmien vertailu käyttäen 
[GTFS](https://developers.google.com/transit/gtfs/)-syötteistä saatavaa 
joukkoliikennedataa.

## Algoritmit

Ohjelma luo GTFS-datasta verkon, joka kuvaa joukkoliikennereittejä eri 
pysäkkien välillä. 
Käyttäjä voi valita verkosta kaksi eri pysäkkiä ja 
ohjelma laskee nopeimman reitin niiden välillä käyttäen sekä Dijkstran 
algoritmia ja A*-algoritmia. Ohjelman tarkoituksena on vertailla 
algoritmien suorituskykyä.

## Tietorakenteet

* *Verkko* - joukkoliikennereittien mallintamiseen
* *Keko* - aputietorakenne reitinhakualgoritmeille
* *Hajautustaulu* - aputietorakenne tiedon käsittelyyn

## Ohjelman syötteet

1. GTFS-syöte, josta rakennetaan joukkoliikennereittejä kuvaava verkko
2. Käyttäjän antama lähtö- ja päätepysäkki joiden välille reitti 
etsitään
3. Käyttäjän antama lähtöaika, josta alkaen reittiä etsitään
   * Lyhyin reitti tarkoittaa siis reittiä, joka on mahdollisimman 
aikasin perillä lähtöajan jälkeen

## Tavoitteelliset aikavaatimukset

* Dijkstran algoritmilla: `O(|E| + |V|log|V|)`
* A*-algoritmilla: `O(|E|)`

## Lähteitä

* https://en.wikipedia.org/wiki/A*_search_algorithm
* https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm 
