# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\AndroidStudio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-dontskipnonpubliclibraryclasses
-forceprocessing
-optimizationpasses 5
-keep class * extends android.support.v7.app.ActionBarActivity
-assumenosideeffects class android.util.Log {
   public static boolean isLoggable(java.lang.String, int);
       public static int v(...);
       public static int i(...);
       public static int w(...);
       public static int d(...);
}

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-libraryjars /libs/Parse-1.5.1.jar
-dontwarn com.parse.**
-keepclasseswithmembernames class * {
    native <methods>;
}