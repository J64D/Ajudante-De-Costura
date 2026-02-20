# Add project specific ProGuard rules here.
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# Se você mantiver a informação do número da linha, descomente isto para
# esconder o nome original do arquivo fonte.
#-renamesourcefileattribute SourceFile

# --- REGRAS PARA OPENCV ---
-keep class org.opencv.** { *; }
-keepclassmembers class org.opencv.** { *; }
-keep class com.scarletstudio.ajudantedecostura.ui.screens.FabricEdgeAnalyzer { *; }
-keepclassmembers class com.scarletstudio.ajudantedecostura.ui.screens.FabricEdgeAnalyzer { *; }
-keep class com.scarletstudio.ajudantedecostura.ui.screens.ImageProxyExtKt { *; }

# Ignorar avisos de bibliotecas que podem causar falha no build com R8
-dontwarn org.opencv.**