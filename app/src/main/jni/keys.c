#include <jni.h>
//#include <unistd.h>
//#include <stdio.h>
//#include <malloc.h>
//#include <android/log.h>
#include <stdbool.h>
//#include <fcntl.h>
#include <string.h>
//
const char debuggingKey[] = "225553436009633B3C4D225E5D4E2A14237B132F";
const char releaseKey[] = "1E290624612D765A5C7D3342022D1149714F4C3B";
//
//static char *blacklistedMountPaths[] = {
//        "/sbin/.magisk/",
//        "/sbin/.core/mirror",
//        "/sbin/.core/img",
//        "/sbin/.core/db-0/magisk.db",
//        "/data/data/com.topjohnwu.magisk",
//        "/data/user/0/com.topjohnwu.magisk",
//        "/data/user_de/0/com.topjohnwu.magisk",
//        "/sbin/magisk",
//        "/sbin/magiskhide",
//        "/init.magisk.rc",
//        "/system/addon.d/99-magisk.sh",
//        "/data/user/0/magisk.db",
//        "/data/user_de/0/magisk.db",
//        "/root/magisk",
//        "/magisk",
//        "/data/magisk"
//};
//
//static const char *suPaths[] = {
//        "/data/local/su",
//        "/data/local/bin/su",
//        "/data/local/xbin/su",
//        "/sbin/su",
//        "/su/bin/su",
//        "/system/bin/su",
//        "/system/bin/.ext/su",
//        "/system/bin/failsafe/su",
//        "/system/sd/xbin/su",
//        "/system/usr/we-need-root/su",
//        "/system/xbin/su",
//        "/cache/su",
//        "/data/su",
//        "/dev/su"
//};
//
//bool is_supath_detected();
//
//bool is_mountpaths_detected();
//
JNIEXPORT jstring JNICALL
Java_whiz_tss_sspark_s_1spark_1android_SSparkApp_getApiBaseURL(JNIEnv *env, jobject instance, jstring key) {
    const char * keyCPPString = (*env)->GetStringUTFChars(env, key, NULL);

    if (strcmp(debuggingKey, keyCPPString) == 0) {
        return (*env)->NewStringUTF(env, "https://us-central1-sample-api-4ed08.cloudfunctions.net/api/");
    } else if (strcmp(releaseKey, keyCPPString) == 0) {
        return (*env)->NewStringUTF(env, "https://auusparkapi-stg.azurewebsites.net/api/");
    }

    return (*env)->NewStringUTF(env, "");
}

JNIEXPORT jstring JNICALL
Java_whiz_tss_sspark_s_1spark_1android_SSparkApp_getApiKey(JNIEnv *env, jobject instance, jstring key) {
    const char * keyCPPString = (*env)->GetStringUTFChars(env, key, NULL);

    if (strcmp(debuggingKey, keyCPPString) == 0) {
        return (*env)->NewStringUTF(env, "vZ8F8Nc4GUXG3zD8f3um7WpbuaKBjU");
    } else if (strcmp(releaseKey, keyCPPString) == 0) {
        return (*env)->NewStringUTF(env, "YmG8guW8tdYXzBZK3p73NTpPX6x9ZA");
    }

    return (*env)->NewStringUTF(env, "");
}

JNIEXPORT jstring JNICALL
Java_whiz_tss_sspark_s_1spark_1android_SSparkApp_getApiBaseURLV3(JNIEnv *env, jobject instance, jstring key) {
    const char * keyCPPString = (*env)->GetStringUTFChars(env, key, NULL);

    if (strcmp(debuggingKey, keyCPPString) == 0) {
        return (*env)->NewStringUTF(env, "https://us-central1-sample-api-4ed08.cloudfunctions.net/api/");
    } else if (strcmp(releaseKey, keyCPPString) == 0) {
        return (*env)->NewStringUTF(env, "https://auusparkservice-stg.azurewebsites.net/api/");
    }

    return (*env)->NewStringUTF(env, "");
}

JNIEXPORT jstring JNICALL
Java_whiz_tss_sspark_s_1spark_1android_SSparkApp_getCollaborationSocketURL(JNIEnv *env,jobject thiz, jstring key) {
    const char * keyCPPString = (*env)->GetStringUTFChars(env, key, NULL);

    if (strcmp(debuggingKey, keyCPPString) == 0) {
        return (*env)->NewStringUTF(env, "https://auusparkconnect-stg.azurewebsites.net/post"); //TODO change this url when there is confirmation from API Team
    } else if (strcmp(releaseKey, keyCPPString) == 0) {
        return (*env)->NewStringUTF(env, "https://auusparkconnect.azurewebsites.net/post");
    }

    return (*env)->NewStringUTF(env, "");
}

//
////jboolean
////Java_whiz_tss_sspark_s_1spark_1android_L(
////        JNIEnv *env,
////        jobject this) {
////    bool bRet = false;
////    do {
////        bRet = is_supath_detected();
////        if (bRet)
////            break;
////        bRet = is_mountpaths_detected();
////        if (bRet)
////            break;
////    } while (false);
////
////    if(bRet)
////        return JNI_TRUE;
////    else
////        return JNI_FALSE;
////}
//
//__attribute__((always_inline))
//bool is_mountpaths_detected() {
//    int len = sizeof(blacklistedMountPaths) / sizeof(blacklistedMountPaths[0]);
//
//    bool bRet = false;
//
//    FILE *fp = fopen("/proc/self/mounts", "r");
//    if (fp == NULL)
//        goto exit;
//
//    fseek(fp, 0L, SEEK_END);
//    long size = ftell(fp);
//    /* For some reason size comes as zero */
//    if (size == 0)
//        size = 20000;  /*This will differ for different devices */
//    char *buffer = malloc(size * sizeof(char));
//    if (buffer == NULL)
//        goto exit;
//
//    size_t read = fread(buffer, 1, size, fp);
//    int count = 0;
//    for (int i = 0; i < len; i++) {
//        char *rem = strstr(buffer, blacklistedMountPaths[i]);
//        if (rem != NULL) {
//            count++;
//            break;
//        }
//    }
//    if (count > 0)
//        bRet = true;
//
//    exit:
//
//    if (buffer != NULL)
//        free(buffer);
//    if (fp != NULL)
//        fclose(fp);
//
//    return bRet;
//}
//
//__attribute__((always_inline))
//JNIEXPORT bool is_supath_detected() {
//    int len = sizeof(suPaths) / sizeof(suPaths[0]);
//
//    bool bRet = false;
//    for (int i = 0; i < len; i++) {
//        if (open(suPaths[i], O_RDONLY) >= 0) {
//            bRet = true;
//            break;
//        }
//        if (0 == access(suPaths[i], R_OK)) {
//            bRet = true;
//            break;
//        }
//    }
//
//    return bRet;
//}