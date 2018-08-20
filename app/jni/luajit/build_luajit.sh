#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
host_os=`uname -s | tr "[:upper:]" "[:lower:]"`

SRCDIR=$DIR/LuaJIT-2.1.0-beta3
cd "$SRCDIR"

NDK=$ANDROID_NDK_ROOT
NDKABI=8
NDKVER=$NDK/toolchains/arm-linux-androideabi-4.9
NDKP=$NDKVER/prebuilt/${host_os}-x86_64/bin/arm-linux-androideabi-
NDKF="--sysroot $NDK/platforms/android-$NDKABI/arch-arm"

echo "NDK in : " $NDK

# Android/ARM, armeabi (ARMv5TE soft-float), Android 2.2+ (Froyo)
DESTDIR=$DIR/android/libs/armeabi
mkdir -p $DESTDIR
rm "$DESTDIR"/*.a
make clean
make HOST_CC="clang -m32" CROSS=$NDKP TARGET_SYS=Linux TARGET_FLAGS="$NDKF"

if [ -f $SRCDIR/src/libluajit.a ]; then
    mv $SRCDIR/src/libluajit.a $DESTDIR/libluajit.a
fi;

# Android/ARM, armeabi-v7a (ARMv7 VFP), Android 4.0+ (ICS)
NDKARCH="-march=armv7-a -mfloat-abi=softfp -Wl,--fix-cortex-a8"
DESTDIR=$DIR/android/libs/armeabi-v7a
mkdir -p $DESTDIR
rm "$DESTDIR"/*.a
make clean
make HOST_CC="clang -m32" CROSS=$NDKP TARGET_SYS=Linux TARGET_FLAGS="$NDKF $NDKARCH"

if [ -f $SRCDIR/src/libluajit.a ]; then
    mv $SRCDIR/src/libluajit.a $DESTDIR/libluajit.a
fi;

# Android/x86, x86 (i686 SSE3), Android 4.0+ (ICS)
NDKABI=14
DESTDIR=$DIR/android/libs/x86
NDKVER=$NDK/toolchains/x86-4.9
NDKP=$NDKVER/prebuilt/${host_os}-x86_64/bin/i686-linux-android-
NDKF="--sysroot $NDK/platforms/android-$NDKABI/arch-x86"
mkdir -p $DESTDIR
rm "$DESTDIR"/*.a
make clean
make HOST_CC="clang -m32" CROSS=$NDKP TARGET_SYS=Linux TARGET_FLAGS="$NDKF"

if [ -f $SRCDIR/src/libluajit.a ]; then
    mv $SRCDIR/src/libluajit.a $DESTDIR/libluajit.a
fi;

make clean
