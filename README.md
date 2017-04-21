# Arachnophobia 
## A project for a Course on Artificial Intelligence

###### An AI controlled game, where our hero, Leonidas has to navigate through terrifying AI controlled tribal spider colonies, to get back home

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
The Spiders are broken up into groups when they spawn based on their colour. Red spiders will never attack each other, but will attack a blue, black or any other colour spider if they see one. About a third of each coloured spider group will be controlled by a neural network, allowing those spiders to have much more knowledge of the map, and hence make better decisions. The other 2 thirds are fuzzy controlled and make decisions based on whats visible within a sight radius.

## Neural Spider System
The input layer consists of the following booleans:
 - Nearest pickup
 - Nearest Friendly Spider (Same Colour)
 - Closeness to the Spartan
 - Nearby Enemy Spider (Different Colour)
 - How Healthy it is
  
The output layer:
 - Defend the Pickup
 - Go to Friend
 - Attack the Spartan
 - Attack an Enemy Spider
 - Wander in Random Direction
 
 The Neural Spider will always look out for himself first, when he's been hurt he's always going to try and escape, avoiding any combat he can, and should make his way back to a spider of the same colour. Same colour spiders can heal each other up, so he can go back out hunting after a while. When healthy, he and his friends will always attack enemy spiders, or the Spartan. When everything is calm, they'll group around pickups, hoping to pounce on the Spartan if he comes near them, otherwise they'll just wander around aimlessly, hopefully coming into contact with a character or pickup. 
 
 


## Spider Fuzzy Logic Systems

The linguistic variables
 - Spartan System
    - Spartan Pos - Euclidean Distance between the spider and the spartan, determines how much a threat he is to the spider
    - Health - How healthy the spider is
    - Risk - How risky it would be to attack the Spartan right now
 - BombSystem 
    - Spartan Pos To Bomb - The euclidean distance between the Spartan and the pickup  
    - Spider Pos To Bomb - The euclidean distance between the Spider and the pickup
    - Health - How healthy the spider is
    - Aggressiveness - Will return how aggressive the spider should behave in regards to the pickup
 - FriendHeal
    - Friend Distance - The euclidean distance between the spider and his friend
    - Health - How healthy the spider is
    - Attraction - Will return how attractive a prospect his friend is, will be high if spider is hurt
  
  The Spider will wage war with the spartan, provided he's not too injured, and will attempt to fight him for pickups whenever he can. 
  When injured, the Spider will always try and get to the safety of a friend to give him a health boost.

## Combat

Combat is simplified down to whether or not an enemy is in an adjacent square. If so, they do damage to that entity until one of them dies. Spiders have some factions they belong to, and will attack and kill spiders of opposing factions that are nearby. 

## Path Finding
Path finding is done via a Best First Search. Due to the relatively open nature of the maze and the existence of numerous cycles, heuristics using the Euclidean distance work exceptionally well for path finding. Entities tend to move in logical pathways to their intended goal, and the algorithm tends to visit relatively few nodes given the number of possible nodes to search.
