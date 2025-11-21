# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Koin
-keep class org.koin.** { *; }
-keep class org.koin.core.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler
-keepclassmembers class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# Compose
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }

# Coil
-keep class coil.** { *; }
-dontwarn coil.**

# Lazypay Elevate
-keep class com.lazypay.android.elevate.** { *; }
-dontwarn com.lazypay.android.elevate.**

# Keep data classes
-keep class com.payu.finance.data.model.** { *; }
-keep class com.payu.finance.domain.model.** { *; }
-keep class com.payu.finance.ui.model.** { *; }

