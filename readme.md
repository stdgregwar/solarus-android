# Solarus Android

This repo contains a AndroidStudio/gradle project building solarus and its dependencies with ndkBuild and make a SDLActity based appplication.

## Things that works

* Video
* Audio
* Quest data loading
* Save files, write directory
* plain lua

## Things that don't works

* Joystick seems broken
* Audio on android virtual devices
* Asset access is rather slow
* LuaJIT

## Not tested

* Shaders
* Extended play

## Building

Simplest way to build is to use android studio. You may succeed using gradlew directly. But this was not tested. All dependencies are already included in the jni folder.
