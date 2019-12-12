/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class mpjdev_natmpjdev_Group */

#ifndef _Included_mpjdev_natmpjdev_Group
#define _Included_mpjdev_natmpjdev_Group
#ifdef __cplusplus
extern "C" {
#endif
#undef mpjdev_natmpjdev_Group_NO_RANK
#define mpjdev_natmpjdev_Group_NO_RANK -1L
#undef mpjdev_natmpjdev_Group_IDENT
#define mpjdev_natmpjdev_Group_IDENT 0L
#undef mpjdev_natmpjdev_Group_CONGRUENT
#define mpjdev_natmpjdev_Group_CONGRUENT 3L
#undef mpjdev_natmpjdev_Group_SIMILAR
#define mpjdev_natmpjdev_Group_SIMILAR 1L
#undef mpjdev_natmpjdev_Group_UNEQUAL
#define mpjdev_natmpjdev_Group_UNEQUAL 2L
#undef mpjdev_natmpjdev_Group_UNDEFINED
#define mpjdev_natmpjdev_Group_UNDEFINED -1L
/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_mpjdev_natmpjdev_Group_init
  (JNIEnv *, jclass);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    GetGroup
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_mpjdev_natmpjdev_Group_GetGroup
  (JNIEnv *, jobject, jint);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeFree
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_mpjdev_natmpjdev_Group_nativeFree
  (JNIEnv *, jobject);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_mpjdev_natmpjdev_Group_nativeSize
  (JNIEnv *, jobject);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeRank
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_mpjdev_natmpjdev_Group_nativeRank
  (JNIEnv *, jobject);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeIncl
 * Signature: ([I)J
 */
JNIEXPORT jlong JNICALL Java_mpjdev_natmpjdev_Group_nativeIncl
  (JNIEnv *, jobject, jintArray);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeExcl
 * Signature: ([I)J
 */
JNIEXPORT jlong JNICALL Java_mpjdev_natmpjdev_Group_nativeExcl
  (JNIEnv *, jobject, jintArray);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeRangeIncl
 * Signature: (I[I)J
 */
JNIEXPORT jlong JNICALL Java_mpjdev_natmpjdev_Group_nativeRangeIncl
  (JNIEnv *, jobject, jint, jintArray);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeRangeExcl
 * Signature: (I[I)J
 */
JNIEXPORT jlong JNICALL Java_mpjdev_natmpjdev_Group_nativeRangeExcl
  (JNIEnv *, jobject, jint, jintArray);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativetransRanks
 * Signature: (JJ[I)[I
 */
JNIEXPORT jintArray JNICALL Java_mpjdev_natmpjdev_Group_nativetransRanks
  (JNIEnv *, jclass, jlong, jlong, jintArray);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeCompare
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_mpjdev_natmpjdev_Group_nativeCompare
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeUnion
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_mpjdev_natmpjdev_Group_nativeUnion
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeIntersection
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_mpjdev_natmpjdev_Group_nativeIntersection
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     mpjdev_natmpjdev_Group
 * Method:    nativeDifference
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_mpjdev_natmpjdev_Group_nativeDifference
  (JNIEnv *, jclass, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif
