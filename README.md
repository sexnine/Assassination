first shot at actually making something decent in java xd

**This is a game requiring at least 3 players, you load into a randomly generated world with the task to prepare and hunt down your target which you can track using a compass.
But beware, as someone will be hunting you down too!
Track your targets down, steal their shit and be the last one standing in the deathmatch to win :)**

### Running the game
Follow the below steps for setting up the server and then run `/assassination start` in-game to start.

ℹ You can skip the below steps if you download the preconfigured server pack under the releases tab.

### Dependencies
These are plugins you must have in your server for this to function.  Your server requires to be running on [PaperMC](https://papermc.io/) (preferred) or Spigot in 1.17.
- [MultiverseCore](https://www.spigotmc.org/resources/multiverse-core.390/)
- A Permissions manager (such as [Luckperms](https://www.spigotmc.org/resources/luckperms.28140/))

Make sure to also put the plugin for Assassination in your plugins folder as well along with the other dependencies.

### Configuration
To ensure the game runs correctly, make sure to set the following configuration:
1. In `server.properties` set the following:
    - `allow-flight=true` This is to stop the server from kicking players that may be in the air while lagging.
    - `allow-nether=false` Assassination at this point doesn't support players going into the nether and isn't intended to allow players to do so.  This disabled players from going through nether portals.
2. In your permissions manager, grant everyone the permission `mv.bypass.gamemode.*`  You can do this with Luckperms by running `lp group default permission set mv.bypass.gamemode.* true` in the console.
3. By default, anyone is allowed to control the game, if you want to only allow certain people to control the game, set `needs-op=true` in the Assassination configuration.  You can then make yourself a server operator by running `op YOUR_USERNAME` in the console.  After that, only people that are server operators can control the game.

### Encounter any bugs?
Open an issue or contact me on Discord (username in profile), and I'll check it out :)

### Want to contribute?
I don't see why you would want to, but sure.  Feel free to message me prior to making a pull request. Forks are welcome to spice up the rules if you'd like :)
