#!/usr/bin/env python3
"""Fetches the Minecraft 1.9 elytra loop sound from Mojang's asset store.

The sound is a Mojang asset; see THIRD_PARTY_NOTICES.md.

Usage:
    python tools/fetch_vanilla_elytra_sound.py
"""

import hashlib
import sys
import urllib.request
from pathlib import Path

EXPECTED_SHA1 = "90d599a1760137a7c4d7a52c77f5c65c02f137d4"
URL = "https://resources.download.minecraft.net/90/" + EXPECTED_SHA1
ROOT = Path(__file__).resolve().parent.parent
OUT = ROOT / "src/main/resources/assets/retrolytra/sound/elytra_loop.ogg"


def main():
    print("fetching", URL)
    with urllib.request.urlopen(URL, timeout=60) as response:
        data = response.read()

    actual = hashlib.sha1(data).hexdigest()
    if actual != EXPECTED_SHA1:
        print("SHA-1 mismatch: expected", EXPECTED_SHA1, "got", actual)
        return 1

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_bytes(data)
    print("wrote", OUT, "(", len(data), "bytes )")
    return 0


if __name__ == "__main__":
    sys.exit(main())
