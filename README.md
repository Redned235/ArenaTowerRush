BattleArena TowerRush
---
Originally created by the original author of [BattleArena](https://github.com/BattlePlugins/BattleArena), alkarin_v for the Minecraft server, [Pwing.net](mc.pwing.net). Due to his absence this plugin was discontinued and hasn't been updated since he left the BattleArena scene. After recieving permission from Tara (original owner of Pwing), I have decided to update and publish the source of this plugin.

### Objective
The objective of TowerRush is to "destroy" the other team's tower before they destroy yours. Each tower has a set amount of health. The team with the most amount of health remaining at the end of the game wins. If a team's tower runs out of health, they are eliminated.

Dependencies
---

- **Bukkit/Spigot API**
  * http://spigotmc.org
  * Requires Spigot, Craftbukkit, or any other server software that implements the Bukkit API.
- **BattleArena**
  * https://www.spigotmc.org/resources/battle-arena.2164/
  * TowerRush is an addon for BattleArena.
  * Required dependency

Arena Setup:
---
These commands will allow you to create a TowerRush arena.

`/tr create ArenaName` - Create arena
`/tr alter ArenaName wr 1` - Set waiting room
`/tr alter ArenaName wr2` - Set a second waiting room (if pleased)
`/tr alter ArenaName TeamIndex` - Sets the spawn point for a team
`/tr addBlock ArenaName TeamIndex` - Add a block for team a team (can add more than 1)
`/tr save` - Save the arena

**Extra Commands**
`/tr join` - Join a game
`/tr leave` - Leave a game
`/tr forcestart` - Force start a game
`/tr clearBlocks ArenaName` - Clears all the blocks for a team

Bug Reports
---
If you have any bugs or problems, please create a new issue [here](https://github.com/Redned/ArenaTowerRush/issues/new) or contact me on Discord at Redned#9473.