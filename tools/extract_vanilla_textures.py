#!/usr/bin/env python3
"""Extracts the elytra textures from a local Minecraft 1.9.4 client jar.

The textures are Mojang assets and are intentionally not covered by this
project's LGPL-3.0 license. See THIRD_PARTY_NOTICES.md.

Usage:
    python tools/extract_vanilla_textures.py /path/to/1.9.4-client.jar

The script verifies the known SHA-1 of the official 1.9.4 client jar before
writing anything.
"""

import hashlib
import sys
import zipfile
from pathlib import Path

EXPECTED_SHA1 = "4a61c873be90bb1196d68dac7b29870408c56969"
ROOT = Path(__file__).resolve().parent.parent
OUT = ROOT / "src/main/resources/assets/retrolytra/textures"

MAPPING = {
    "assets/minecraft/textures/entity/elytra.png": "entity/elytra.png",
    "assets/minecraft/textures/items/elytra.png": "items/elytra.png",
    "assets/minecraft/textures/items/broken_elytra.png": "items/elytra_broken.png",
}


def sha1(path):
    digest = hashlib.sha1()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def main():
    if len(sys.argv) != 2:
        print(__doc__)
        return 2

    jar = Path(sys.argv[1])
    if not jar.is_file():
        print("No such file:", jar)
        return 1

    actual = sha1(jar)
    if actual != EXPECTED_SHA1:
        print("Unexpected client jar.")
        print("  expected SHA-1:", EXPECTED_SHA1)
        print("  actual SHA-1:  ", actual)
        return 1

    OUT.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(jar) as archive:
        for source, relative in MAPPING.items():
            data = archive.read(source)
            destination = OUT / relative
            destination.parent.mkdir(parents=True, exist_ok=True)
            destination.write_bytes(data)
            print("wrote", destination)

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
