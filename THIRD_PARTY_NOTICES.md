# Third-party notices

## Minecraft 1.9.4 elytra textures

The following files are extracted unchanged from the official Minecraft 1.9.4
client jar:

| File in this repository | File in the 1.9.4 client jar |
| --- | --- |
| `src/main/resources/assets/retrolytra/textures/entity/elytra.png` | `assets/minecraft/textures/entity/elytra.png` |
| `src/main/resources/assets/retrolytra/textures/items/elytra.png` | `assets/minecraft/textures/items/elytra.png` |
| `src/main/resources/assets/retrolytra/textures/items/elytra_broken.png` | `assets/minecraft/textures/items/broken_elytra.png` |

These textures are property of Mojang Studios / Microsoft. They are **not**
covered by Retrolytra's LGPL-3.0 license. They are included so that the
backport can use the original elytra artwork, matching the approach used by
Et Futurum Requiem.

The extraction is reproducible with:

```sh
python tools/extract_vanilla_textures.py /path/to/1.9.4-client.jar
```

The script verifies the official 1.9.4 client jar SHA-1
`4a61c873be90bb1196d68dac7b29870408c56969` before writing any file.

## Minecraft 1.9 elytra flight sound

`src/main/resources/assets/retrolytra/sound/elytra_loop.ogg` is the 1.9 asset
`minecraft/sounds/item/elytra/elytra_loop.ogg`, fetched from Mojang's asset
store (SHA-1 `90d599a1760137a7c4d7a52c77f5c65c02f137d4`). It is property of
Mojang Studios / Microsoft and is not covered by the LGPL-3.0 license.

Reproduce it with:

```sh
python tools/fetch_vanilla_elytra_sound.py
```

## Everything else

All other assets in this repository (lang files, model code and any
project-authored assets) are original project assets unless stated otherwise.
