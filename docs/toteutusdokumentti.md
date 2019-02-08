## Rakenne

Ohjelman päätoiminnallisuus on toteutettu luokissa 
[GTFSGraph](https://github.com/mjaakko/gtfs-route-finder/blob/master/src/main/java/xyz/malkki/gtfsroutefinder/gtfs/graph/GTFSGraph.java) ja 
[AStar](https://github.com/mjaakko/gtfs-route-finder/blob/master/src/main/java/xyz/malkki/gtfsroutefinder/graph/algorithms/AStar.java).

`GTFSGraph` kuvaa GTFS-syötteestä muodostettua joukkoliikennereittien verkkoa ja sisältää metodin `getEdgesFromNode`, joka hakee tämän hetkiseltä 
pysäkiltä lähtevät yhteydet sekä mahdolliset kävelyetäisyydellä olevat pysäkit.

`AStar` toteuttaa reitin haun verkossa. `AStar` on toteutettu [Wikipedian esimerkin](https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode) 
mukaan, mutta se sisältää tiettyjä muutoksia, jotta sen käyttö toimii myös joukkoliikenneverkossa.

Lisäksi ohjelma sisältää toteutuksen Dijkstran algoritmista, joka on tehty asettamalla A*-algoritmin heuristiikkafunktioksi nolla.

## Saavutetut aika- ja tilavaativuudet 

TODO

## Suorituskykyvertailu

TODO

## Lähteet

### Algoritmit
* https://en.wikipedia.org/wiki/A*_search_algorithm

### Tietorakenteet
* https://en.wikipedia.org/wiki/Hash_table
* https://en.wikipedia.org/wiki/Binary_heap

### Muu 
* https://developers.google.com/transit/gtfs/reference/
