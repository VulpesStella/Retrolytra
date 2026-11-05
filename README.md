# Retrolytra

Elytra backport for **Minecraft 1.6.4 / Forge 9.11.1.1345**.

## Features

- Elytra item with chest-slot equipping, durability, broken state and leather repair.
- 1.9-style glide physics, eye height, third-person pose and wing model.
- Jump while falling to start gliding; look to climb, dive and steer.
- Firework rockets boost you while gliding.
- Looping flight sound, enchanted glint, and cape texture when one is available.
- Multiplayer flight-state synchronisation.
- Item-name and death-message translations for 1.6.4 locales with official
  text; fully untranslated locales fall back to English.
- Configurable survival recipe, optional Backlytra-style dyeing and optional flight sound.

## Requirements

- Minecraft 1.6.4
- Forge 9.11.1.1345
- [Retrospectum Mixin](https://github.com/DesideriumMC/RetrospectumMixin)
- Java 7 or 8 for the game

## Installation

1. Install Minecraft Forge 1.6.4.
2. Put [Retrospectum Mixin](https://github.com/DesideriumMC/RetrospectumMixin) and
   `retrolytra-retrospectum-1.6.4-<version>.jar` into the `mods/` directory.
3. Launch the game.

## Usage

- Right-click an elytra, or shift-click it from the inventory, to equip it.
- While falling, press jump to start gliding.
- Look up to climb, look down to dive; the wings follow your movement.
- Hold a firework rocket and right-click while gliding to boost.
- Repair a damaged elytra with leather in an anvil.

## Configuration

`config/retrolytra.cfg`:

| Key | Default | Description |
| --- | --- | --- |
| `itemId` | `5000` | Numeric item id. |
| `durability` | `432` | Damage before the elytra becomes unusable. |
| `dataWatcherFlag` | `7` | Entity flag bit used to sync flight. |
| `recipe` | `simple` | `none`, `simple`, `day_one` or `ridiculous`. |
| `enableDyeing` | `false` | Enables the Backlytra-style dye recipe and tinted wings. Existing dyed elytra keep their NBT colour but render undyed while disabled. |
| `enableFlightSound` | `true` | Enables the looping flight sound. |
| `enableFireworkBoost` | `true` | Enables firework-rocket boosting. |

## Building

Requirements: JDK 17 for Gradle, JDK 8 for compilation.

```powershell
./setup.ps1
./build.ps1 build
```

Linux:

```sh
./setup.sh
./build.sh build
```

`./build.ps1 genSources` generates decompiled sources and `./build.ps1 runClient`
starts a development client. The release jar is written to
`build/libs/retrolytra-retrospectum-1.6.4-<version>.jar`.

## API

Other mods can use
`io.github.vulpesstella.retrolytra.api.RetrolytraAPI`:

- `isElytraFlying(EntityPlayer)`
- `setElytraFlying(EntityPlayer, boolean)`
- `getTicksElytraFlying(EntityPlayer)`
- `getWornElytra(EntityPlayer)`

## Assets and license

The elytra textures and flight sound are extracted from Minecraft 1.9.4 and
embedded in the jar. They are Mojang assets and are not covered by the project's
LGPL-3.0 license; see [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
The extraction is reproducible with the scripts in `tools/`.

Project code is licensed under LGPL-3.0; see [LICENSE](LICENSE).

## Credits

- [Backlytra](https://github.com/unascribed-archive/Backlytra)
- [Et Futurum Requiem](https://github.com/Roadhog360/Et-Futurum-Requiem)
- [Retrospectum Loom](https://github.com/DesideriumMC/RetrospectumLoom)
