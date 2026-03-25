# Cave Fog Stabilizer

When Mojang added environmental fog to the game, the time of day started affecting how fog looks underground. I thought this didn't make sense, so I made this mod.

This mod prevents fog from being affected by the time of day **only when the player is underground**. This lets you have the new environmental fog on the surface while keeping cave fog unaffected.

Check out the Gallery tab for comparison images between this mod, vanilla after environmental fog, and vanilla before environmental fog.

## Compatibility

- Compatible with data packs using environmental attributes introduced in 1.21.11. If a cave biome has a custom fog color different from black (common in modded cave biomes), the mod will give some brightness to the fog in order to allow the color to go through.
- Compatible with Sodium, although the "Use Fog Occlusion" option, which is enabled by default, may cause some distant chunks in large caves to pop out of existence in certain cases, leaving holes in the distance. If this bothers you, you can disable "Use Fog Occlusion" in Sodium video settings.

## Configuration

The mod provides a configuration when Sodium is installed, but it's **not required** for the mod to function.

![config](https://cdn.modrinth.com/data/cached_images/8c069fa4d08f302552ca4cc3a72dcf75c409eb80.png)
