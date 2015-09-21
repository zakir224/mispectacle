LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_MODULE    := native_sample
FILE_LIST := $(wildcard $(LOCAL_PATH)/stasm/*.cpp) \
 $(wildcard $(LOCAL_PATH)/stasm/MOD_1/*.cpp) \
 $(wildcard $(LOCAL_PATH)/jnipart/*.cpp)

LOCAL_SRC_FILES := $(FILE_LIST:$(LOCAL_PATH)/%=%)

LOCAL_LDLIBS +=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)
