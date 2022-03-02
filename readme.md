# LabyAddons-1.8
Better LabyMod Addon Store

[![Build Addon](https://github.com/l3nnartt/LabyAddons-1.8/actions/workflows/main.yml/badge.svg?branch=main)](https://github.com/l3nnartt/LabyAddons-1.8/actions/workflows/main.yml)

## Setup Workspace
```
gradlew setupDecompWorkspace 
```
#### Setup for InteliJ
```
gradlew idea
```
#### Setup for Eclipse
```
gradlew eclipse
```
#### Build Addon
```
gradlew build 
```
#### Debug Addon
```
-Dfml.coreMods.load=net.labymod.core.asm.LabyModCoreMod -DdebugMode=true -Daddonresources=addon.json
```