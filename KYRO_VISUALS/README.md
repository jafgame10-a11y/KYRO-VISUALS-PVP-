# KYRO VISUALS (PVP) — starter test build

Target: Minecraft Java 26.1.2, Fabric, Java 25.

This is a **source project**, not a prebuilt `.jar`. It has not been compiled or tested in-game in this environment, so treat it as an early test version.

## Included in this starter
- Blue/black KYRO HUD panel
- FPS display
- Ping display when connected to a server
- CPS display (basic polling prototype; may need adjustment)
- Press `0` to toggle the HUD
- Client-side only; no combat cheats or server-side changes

## Build on Windows
1. Install a Java 25 JDK.
2. Install IntelliJ IDEA or another Gradle-capable Java IDE.
3. Open this folder as a Gradle project and let Gradle sync/download dependencies.
4. Run the Gradle task `build`.
5. If the build succeeds, the mod JAR will be in `build/libs/`.
6. Put that JAR in the Fabric profile's `mods` folder, with Fabric API for Minecraft 26.1.2 installed.

If it fails, send the **first red error** from the Gradle output and I can help fix it.

## Easy build on Windows
1. Extract the whole ZIP to a normal folder.
2. Double-click `BUILD_KYRO.bat`.
3. It downloads Gradle automatically (internet required) and runs the build.
4. If successful, the `.jar` will be in `build\libs\`.
5. Copy the JAR into the `mods` folder for your Norisk Minecraft 26.1.2 Fabric instance. Fabric API must also be installed.

If the build fails, send the full error or a screenshot. This starter project is not precompiled or guaranteed to build until tested against your exact Minecraft/Fabric setup.
