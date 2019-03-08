## Rakenne

Ohjelman päätoiminnallisuus on toteutettu luokissa 
[GTFSGraph](https://github.com/mjaakko/gtfs-route-finder/blob/master/src/main/java/xyz/malkki/gtfsroutefinder/gtfs/graph/GTFSGraph.java) ja 
[AStar](https://github.com/mjaakko/gtfs-route-finder/blob/master/src/main/java/xyz/malkki/gtfsroutefinder/graph/algorithms/AStar.java).

`GTFSGraph` kuvaa GTFS-syötteestä muodostettua joukkoliikennereittien verkkoa ja sisältää metodin `getEdgesFromNode`, joka hakee tämän hetkiseltä 
pysäkiltä lähtevät yhteydet sekä mahdolliset kävelyetäisyydellä olevat pysäkit.

`AStar` toteuttaa reitin haun verkossa. `AStar` on toteutettu [Wikipedian esimerkin](https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode) 
mukaan, mutta se sisältää tiettyjä muutoksia, jotta sen käyttö toimii myös joukkoliikenneverkossa. A*-algoritin heuristiikkafunktio laskee etäisyyden tällä hetkellä tutkittavalta 
pysäkiltä päätepysäkille ja laskee kauanko tämän etäisyyden kulkemiseen menisi 30m/s nopeudella. Heuristiikkafunktio ei ole mitenkään paras mahdollinen ja suorituskykyvertailusta 
huomataan, että suorituskykyero Dijkstran algoritmiin ei ole suuri.

Lisäksi ohjelma sisältää toteutuksen Dijkstran algoritmista, joka on tehty asettamalla A*-algoritmin heuristiikkafunktioksi nolla.

## Saavutetut aikavaativuudet 

Saavutettujen aikavaativuuksien arviointi oli hankalaa, koska joukkoliikennedatasta muodostettu verkko on hankala tallentaa 
esimerkiksi vieruslistoina, eikä verkon kaarille löydy selkeää määrää, koska jokaisesta solmusta lähtevät kaaret riippuvat 
ajanhetkestä, jolla solmuun saavutaan. 
Koodista tehdyn karkean analyysin perusteella sekä A*-algoritmin ja Dijkstran algoritmin pahimman tapauksen aikavaatimukseksi tulee `O(|V|^2log|V|)`, jossa `|V|` on pysäkkien määrä verkossa. Käytännössä keskimääräinen aikavaativuus pitäisi kuitenkin olla huomattavasti pienempi. 

## Työn puutteita ja kehitysideoita

* Ohjelman käyttöliittymä ei ole kovin helppokäyttöinen
* Ohjelma voisi antaa käyttäjän valita ainakin lähtöajan, jolloin reittiä haetaan, sekä mahdollisesti muita parametrejä reitinhaulle (esim. turhien vaihtojen välttämiseksi)
* Algoritmi usein löytää reitin, joka sisältää "turhia" pysähdyksiä (esim. *Ruoholahti -> Kamppi -> Rautatientori*, kun parempi reitti olisi *Ruoholahti -> Rautatientori*)
  * Tämän voisi luultavasti estää niin, että algoritmi pitää kirjaa linjoista, joilla tämän hetkiselle pysäkille on saavuttu ja hylkäämällä myöhemmin reitit, jotka käyttäisivät tätä samaa linjaa
  * ...tai yksinkertaisemmin filtteröimällä ohjelman tulostuksesta turhat pysähdykset
* Algoritmien toimintaa voisi nopeuttaa esikäsittelemällä dataa paremmin
  * Esim. tällä hetkellä algoritmi hakee jokaiselta pysäkiltä 500m etäisyydellä olevat vaihtopysäkit ja tämä operaatio vie pahimmassa tapauksessa `O(|V|)` ajan. Esikäsittelyssä pysäkeille voisi tehdä staattisen listan vaihtopysäkeistä, jonka voisi hakea ajassa `O(1)`.

## Lähteet

### Algoritmit
* https://en.wikipedia.org/wiki/A*_search_algorithm

### Tietorakenteet
* https://en.wikipedia.org/wiki/Hash_table
* https://en.wikipedia.org/wiki/Binary_heap

### Muu 
* https://developers.google.com/transit/gtfs/reference/
