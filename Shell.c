#include <string.h>
#include <unistd.h>
#include "Shell.h"

JNIEXPORT jint JNICALL Java_Shell_changeDirectory (JNIEnv *env, jclass cls, jstring s)
{
  const char *dir = (*env)->GetStringUTFChars(env, s, 0);
  char sdir[strlen(dir) + 1];

  strcpy(sdir, dir);
  (*env)->ReleaseStringUTFChars(env, s, dir);

  return chdir(sdir);
}
