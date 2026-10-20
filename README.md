# Cave Fog Stabilizer

When Mojang added environmental fog to the game, the time of day started affecting how fog looks underground. I thought this didn't make sense, so I made this mod.

This mod prevents fog from being affected by the time of day **only when the player is underground**. This lets you have the new environmental fog on the surface while keeping cave fog unaffected.

Check out the Gallery tab for comparison images between this mod, vanilla after environmental fog, and vanilla before environmental fog.

## Compatibility

- Supports every version which has environmental fog (1.21.6 and newer).
- Compatible with data packs and mods adding custom fog colors to cave biomes, through biome fog colors before 1.21.11 and environment attributes since 1.21.11. A cave biome is any biome in the vanilla cave biome list or the `c:is_cave` / `c:is_underground` biome tags. Custom fog colors are kept as-is, only darkened by a configurable amount.
- Compatible with Sodium, although the "Use Fog Occlusion" option, which is enabled by default, may cause some distant chunks in large caves to pop out of existence in certain cases, leaving holes in the distance. If this bothers you, you can disable "Use Fog Occlusion" in Sodium video settings.

## Configuration

The mod is configured through [Fzzy Config](https://modrinth.com/mod/fzzy-config). On 1.21.11 and newer, the options are also available on the [Sodium](https://modrinth.com/mod/sodium) settings screen when it's installed.
