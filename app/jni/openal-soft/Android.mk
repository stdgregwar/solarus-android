LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := openal
LOCAL_LDLIBS := -llog

LOCAL_SRC_FILES := \
  $(LOCAL_PATH)/OpenAL/Alc/android.c              \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alAuxEffectSlot.c \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alBuffer.c        \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alDatabuffer.c    \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alEffect.c        \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alError.c         \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alExtension.c     \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alFilter.c        \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alListener.c      \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alSource.c        \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alState.c         \
  $(LOCAL_PATH)/OpenAL/OpenAL32/alThunk.c         \
  $(LOCAL_PATH)/OpenAL/Alc/ALc.c                  \
  $(LOCAL_PATH)/OpenAL/Alc/alcConfig.c            \
  $(LOCAL_PATH)/OpenAL/Alc/alcEcho.c              \
  $(LOCAL_PATH)/OpenAL/Alc/alcModulator.c         \
  $(LOCAL_PATH)/OpenAL/Alc/alcReverb.c            \
  $(LOCAL_PATH)/OpenAL/Alc/alcRing.c              \
  $(LOCAL_PATH)/OpenAL/Alc/alcThread.c            \
  $(LOCAL_PATH)/OpenAL/Alc/ALu.c                  \
  $(LOCAL_PATH)/OpenAL/Alc/bs2b.c                 \
  $(LOCAL_PATH)/OpenAL/Alc/null.c                 \
  $(LOCAL_PATH)/OpenAL/Alc/panning.c              \
  $(LOCAL_PATH)/OpenAL/Alc/mixer.c                \
  $(LOCAL_PATH)/OpenAL/Alc/audiotrack.c           \
  $(LOCAL_PATH)/OpenAL/Alc/opensles.c

LOCAL_SRC_FILES := $(LOCAL_SRC_FILES:$(LOCAL_PATH)/%=%)

LOCAL_C_INCLUDES := \
  $(LOCAL_PATH)/OpenAL \
  $(LOCAL_PATH)/OpenAL/include \
  $(LOCAL_PATH)/OpenAL/OpenAL32/Include

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_C_INCLUDES)

LOCAL_CFLAGS += -DAL_ALEXT_PROTOTYPES -DVERDE_USE_REAL_FILE_IO -DANDROID "-DRELEASE_LOG(...)=do{}while(0);"
LOCAL_CFLAGS += -fpic -ffunction-sections -funwind-tables -fstack-protector -fno-short-enums -DHAVE_GCC_VISIBILITY -O3

MAX_SOURCES_LOW ?= 4
MAX_SOURCES_START ?= 8
MAX_SOURCES_HIGH ?= 64

LOCAL_CFLAGS += -DMAX_SOURCES_LOW=$(MAX_SOURCES_LOW) -DMAX_SOURCES_START=$(MAX_SOURCES_START) -DMAX_SOURCES_HIGH=$(MAX_SOURCES_HIGH)
LOCAL_CFLAGS += -DPOST_FROYO

include $(BUILD_SHARED_LIBRARY)
