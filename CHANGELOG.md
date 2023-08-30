CHANGES TO PA03


- modified Coord constructor to only take in x and y position instead of taking in x and y positions and cell status
  - for ease of creating new Coords
- fixed all player classes to correctly store their previous shots
  - for fixing implementation of the game
- added fields to and improved takeShots in the FakePlayer class (and added helper methods)
  - for improving our AI calculated shots
- modified reportDamage in FakePlayer 
  - for fixing implementation of the game and to work with the improved takeShots method