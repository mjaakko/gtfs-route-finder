## Yksikkötestit

Ohjelma sisältää runsaasti yksikkötestejä, jotka testaavat yksittäisten luokkien ja metodien toimivan oikein.

### Yksikkötestien suoritus

```gradle test``` 

## Suorituskykytestaus

1. Suorita ohjelma komennolla `gradle run`
2. Valitse toiminto `1 - algorithm comparison`
3. Valitse toistojen määrä

Ohjelma laskee halutun määrän satunnaisia reittejä ja tulostaa ajan, jota reittien hakemiseen käytettiin tietyllä algoritimilla.
Esimerkki tulosteesta:
```
Dijkstra found 15 random routes in 327s
AStar (pessimistic, unoptimal routes) found 15 random routes in 316s
AStar found 15 random routes in 312s
```
