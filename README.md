# Berry Aura (Fabric 1.21.4) — Client Mod

Right-click aura that harvests **fully-grown sweet berry bushes** around you (up to 8 per tick) **while holding a Blaze Rod**. Toggle with **Right Shift**.

## Requirements
- **Java 21** (Minecraft 1.21+ requires it)
- **Fabric Loader** for **1.21.4**
- **Fabric API** for **1.21.4**
- Gradle (or use a Gradle installed on your system)

## Build
```bash
# Inside the project folder
./gradlew build    # macOS/Linux
# or
gradlew.bat build  # Windows (if you add a wrapper)
```

The built JAR will appear under `build/libs/`.
Drop it into your `.minecraft/mods/` folder (with Fabric Loader + Fabric API for 1.21.4).

## Controls & Behavior
- **Toggle key:** Right Shift (changeable in Controls → Key Binds → "Berry Aura: Toggle").
- **Condition:** Main-hand item must be **Blaze Rod**.
- **Targets:** Fully grown sweet berry bushes (`age=3`) within a radius of 5 blocks.
- **Per tick limit:** 8 interactions.

## Tweaks
Edit constants in `BerryAuraMod.java`:
- `RADIUS` (default 5)
- `MAX_PER_TICK` (default 8)
- `REQUIRE_BLAZE_ROD` (true)
- `ONLY_AGE_THREE` (true)

## Notes
This is a **client-side** mod that sends normal right-click interactions for nearby bushes.
Servers may rate-limit or block excessive interactions.
Use responsibly and follow server rules.
