# Velocity Player Limit
[Velocity](https://velocitypowered.com) plugin to limit the amount of players allowed on a network.

## Configuration
- `show-in-player-list`: Whether to show the player limit in the server list.
- `player-limit`: The amount of concurrent players allowed on the server.
- `kick-message`: The kick message to display to players connecting to the server when it is full.

## Commands
- `/playerlimit [limit]` Set the player limit. This command will automatically update the configuration file
with the new limit. If no limit argument is provided, the current limit will be sent to the sender.

## Permissions
- `velocityplayerlimit.bypass`: Allows joining even if the server is full.
- `velocityplayerlimit.command`: Allows using the `/playerlimit` command.
