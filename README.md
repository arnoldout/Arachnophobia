# Arachnophobia 
## A project for a Course on Artificial Intelligence

###### An AI controlled game, where our hero, Leonidas has to navigate through terrifying tribal spider colonies, to get back home

## Neural Networks

### The Spartan

The input layer consists of the following booleans:
  - Sword nearby
  - Help Marker nearby
  - Bomb nearby
  - H-Bomb nearby
  - Spiders near the nearest bomb
  - Spiders near the nearest H-bomb
  - Does he have the map to the exit
  
The output layer:
 - Get the nearest sword 
 - Get the nearest help marker
 - Detonate the nearest bomb
 - Detonate the nearest H-Bomb
 - Wander in a random direction
 - Run to the exit

The Spartan will tend to be goal oriented and opportunistic. Instead of spending his time hunting down spiders and inevitably die a painful death, he will only engage spiders in range of attack. He will however seek out a bomb if there are spiders near it, because they make easy kills. Swords will increase his attack power, which determines how much damage he does to spiders. The bombs, which act more like [Raid pest killer](http://www.raidkillsbugs.com.au/en-au/products) and less like [something the United States would drop on a tunnel system in Afghanistan](https://en.wikipedia.org/wiki/GBU-43/B_Massive_Ordnance_Air_Blast). They only kill bugs, they dont remove walls or kill the Spartan, which is fortunate for him. He gets to not be vaporized, joy.

His primary goal is getting four help nodes, as each one provides a piece of a map that leads him to the exit. One he completes the map, he will head straight to the exit only attacking spiders in his path. Once he gets to the exit, there is no fanfare or win screen, no fireworks, he just stands there. So if you get bored of him blinking, you can just quit the game. This isn't a course on graphics, we're better than those vain and shallow graphic designers.

### Spiders

The input layer consists of the following booleans:
 - list
 - them
 - here
  
The output layer:
 - list
 - them
 - here


## Fuzzy Logic Systems

The linguistic variables
 - list 
 - them
 - here

## Combat

Combat is simplified down to whether or not an enemy is in an adjacent square. If so, they do damage to that entity until one of them dies. Spiders have some factions they belong to, and will attack and kill spiders of opposing factions that are nearby. 

## Path Finding
Path finding is done via a Best First Search. Due to the relatively open nature of the maze and the existence of numerous cycles, heuristics using the Euclidean distance work exceptionally well for path finding. Entities tend to move in logical pathways to their intended goal, and the algorithm tends to visit relatively few nodes given the number of possible nodes to search.
