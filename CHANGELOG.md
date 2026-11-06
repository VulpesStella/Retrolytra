# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] - 2026-11-06

### Added

- Elytra item for Minecraft 1.6.4 with chest-slot equipping, durability,
  broken state, leather anvil repair and dispenser equipping.
- 1.9-style glide physics, eye height, third-person pose, animated wing model
  and wing-rotation smoothing.
- 1.15+ takeoff logic by default: jump again while airborne to deploy the
  elytra, including while still ascending.
- Firework-rocket boosting while gliding and the optional QuickTakeoff mode
  for deploying and boosting with a single right-click while airborne.
- Looping flight sound, enchanted glint, cape texture override and optional
  Backlytra-style dyeing.
- Multiplayer flight-state synchronisation, smoother tracker velocity updates
  and an exemption from the vanilla `allow-flight=false` floating kick while
  gliding.
- `flyIntoWall` damage and death message, plus item-name and death-message
  translations for 1.6.4 locales with official text.
- Configurable survival recipe, item id, data-watcher flag, flight sound and
  firework boost settings.
- `RetrolytraAPI` for querying and controlling elytra flight from other mods.
- Embedded Minecraft 1.9.4 elytra textures and flight sound, with reproducible
  extraction scripts under `tools/`.
- Automatic GitHub Actions build workflow and distributable artifact upload.

### Changed

- Retooled the build to Retrospectum Loom 0.2.1 and Retrospectum Mixin 0.2.0,
  resolved from the Retrospectum Maven repository.
- Generated `BuildConstants` now provide mod metadata instead of source-token
  expansion.
- Setup records local JDK paths in `gradle.local.properties`.
- Moved the project package to `io.github.vulpesstella.retrolytra`.
- Reworked the README for players and external developers, and documented the
  server requirements for elytra flight.

### Removed

- Bundled Loom and Mixin artifacts under `libs/`.
- Fully untranslated language files that only duplicated English.

### Fixed

- Elytra takeoff no longer requires downward motion, matching modern versions.
- Leaving creative flight no longer starts gliding automatically.
- Firework boosting now requires the player to be airborne, and QuickTakeoff
  predicts the client-side flight state so the boost is not overwritten by
  normal fall movement.
- Corrected the untranslated Welsh `flyIntoWall` death message.
- Removed duplicate translator stubs and stale documentation.

[Unreleased]: https://github.com/VulpesStella/Retrolytra/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/VulpesStella/Retrolytra/releases/tag/v0.1.0
