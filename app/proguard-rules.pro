# Preserve line numbers for useful crash reports while allowing class/member obfuscation.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# OpenCV's generated Java wrappers bind to JNI symbols using their Java class and
# native method names. Keep only those names; all other OpenCV code can be optimized.
-keepclasseswithmembernames,includedescriptorclasses class org.opencv.** {
    native <methods>;
}
-dontwarn org.opencv.**
