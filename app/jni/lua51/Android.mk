LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := lua
LOCAL_SRC_FILES := $(subst $(LOCAL_PATH)/,, \
									$(wildcard $(LOCAL_PATH)/*.c))


LOCAL_C_INCLUDES := $(LOCAL_PATH)/include
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_C_INCLUDES)

LOCAL_CFLAGS := -O2 -g -ffast-math -fsigned-char -Wall -Wfatal-errors -Wno-deprecated-declarations -DANDROID_VERSION -D__ANDROID__
LOCAL_CXXFLAGS := $(LOCAL_CFLAGS) -Wno-write-strings

include $(BUILD_STATIC_LIBRARY)
