# Legendary Shrines README

This mod adds shrines in the Overworld that allow you to choose where to respawn. Just as Marika's statues in Elden Ring
They can't be broken, and they don't work in hardcore.

## Config

Server config, `legendaryshrines-server.toml`:

The mod provides some configurations for more customization. Ask the author for any additions request you would like to see.

- `bindRadius`: distance at which a shrine binds to a player (default 16)
- `breakDistance`: distance at which the link breaks, 0 to disable (default 1000 blocks)
- `bindOnUse`: bind by right clicking instead of proximity (default false)
- `cooldownSeconds`: delay before a shrine can be used again, 0 to disable (default 0)
- `overrideVanillaRespawn`: the vanilla Respawn button sends bound players to their shrine (default false)
- `soundsEnabled`: play shrine sounds (default true)

## Commands

- `/shrine status`: show your current link
- `/shrine unlink`: break your link
- `/shrine unlink <players>`: break other players' links (op only)

## License

All Rights Reserved.